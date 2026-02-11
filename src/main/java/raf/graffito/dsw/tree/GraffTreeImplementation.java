package raf.graffito.dsw.tree;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.model.implementation.slide.SlideElement;
import raf.graffito.dsw.tree.model.GraffTreeItem;
import raf.graffito.dsw.tree.view.GraffTreeView;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageGenerator;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.graff.GraffNodeComposite;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.model.implementation.Workspace;
import raf.graffito.dsw.model.repository.GraffRepository;
import raf.graffito.dsw.model.repository.NodeType;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import lombok.Getter;

@Getter
public class GraffTreeImplementation implements GraffTree{
    private GraffTreeView treeView;
    private DefaultTreeModel treeModel;

    @Override
    public GraffTreeView generateTree() {
        GraffTreeItem workspace = new GraffTreeItem(repo().getRoot());
        treeModel = new DefaultTreeModel(workspace);
        treeView = new GraffTreeView(treeModel);
        new TreeRefresher(treeModel);

        return treeView;
    }

    @Override
    public void addChild(GraffTreeItem parentItem, NodeType type) {
        GraffNode parent = parentItem.getGraffNode();
        if (!(parent instanceof GraffNodeComposite)) {
            mg().notify(new Message("THIS NODE CANNOT CONTAIN CHILDREN", MessageType.ERROR, parent));
            return;
        }

        // prvo validacija po tipovima (pre kreiranja)
        if (!canContain(parent, type)) {
            mg().notify(new Message("THIS NODE CANNOT HAVE GIVEN TYPE AS A CHILD", MessageType.ERROR, parent));
            return;
        }

        String base  = suggestTitle(type);
        String title = nextIndexedTitle((GraffNodeComposite) parent, base, type);

        if(parent.findByName(title) != null){
            mg().notify(new Message("NODE WITH THAT NAME IN " + parent.getTitle() + " ALREADY EXISTS", MessageType.ERROR, parent));
            return;
        }

        GraffNode child = repo().getNodeFactory(type).create(parent, title);

        try {
            ((GraffNodeComposite) parent).addChild(child);
        } catch (IllegalArgumentException e) {
            mg().notify(new Message("FAILED TO ADD CHILD: " + e.getMessage(), MessageType.ERROR, parent));
            return;
        }

        GraffTreeItem childItem = new GraffTreeItem(child);
        parentItem.add(childItem);
        treeModel.nodeStructureChanged(parentItem);

        TreePath path = new TreePath(childItem.getPath());
        treeView.setSelectionPath(path);
        treeView.scrollPathToVisible(path);
        treeView.expandPath(path);

        mg().notify(new Message("NEW CHILD "  + child.getTitle() + " ADDED", MessageType.INFO, parent));
    }

    @Override
    public void addChild(GraffTreeItem parentItem){
        if(parentItem == null) parentItem = getSelectedNode();
        if(parentItem == null) {
            mg().notify(new Message("PATH FOR NODE IS NOT SELECTED", MessageType.WARNING, null));
            return;
        }

        GraffNode parent = parentItem.getGraffNode();

        if(parent instanceof Workspace){
            addChild(parentItem, NodeType.PROJECT);
        } else if(parent instanceof Presentation){
            addChild(parentItem, NodeType.SLIDE);
        } else if(parent instanceof Slide){
            mg().notify(new Message("YOU CANNOT ADD CHILDREN TO SLIDE NODE VIA ADDNODE BUTTON", MessageType.ERROR, parent));
            return;
        } else if(parent instanceof Project){
            // nista ne radi, za ovo ce se psotarati akcija koja poziva JOption komponentu, jer zelimo da view i model budu odvojeni
            return;
        }

    }

    @Override
    public void removeSelected() {
        GraffTreeItem selected = getSelectedNode();
        if (selected == null) {
            mg().notify(new Message("NO NODE SELECTED FOR REMOVE", MessageType.WARNING, null));
            return;
        }

        // ne dozvoli brisanje root-a
        if (nodeTypeOf(selected.getGraffNode()) == NodeType.WORKSPACE) {
            mg().notify(new Message("WORKSPACE CANNOT BE DELETED", MessageType.ERROR, selected.getGraffNode()));
            return;
        }

        if(nodeTypeOf(selected.getGraffNode()) == NodeType.SLIDEELEMENT){
            mg().notify(new Message("SLIDE ELEMENT CANNOT BE DELETED VIA REMOVENODE BUTTON", MessageType.ERROR, selected.getGraffNode()));
            return;
        }

        GraffTreeItem parentItem = (GraffTreeItem) selected.getParent();
        GraffNode node   = selected.getGraffNode();
        GraffNode parent = parentItem.getGraffNode();

        if(parent instanceof Project && ((GraffNodeComposite) parent).getChildren().size() == 1){
            mg().notify(new Message("PROJECT MUST HAVE AT LEAST ONE CHILD", MessageType.WARNING, parent));
            return;
        }

        if (node instanceof Project) {
            ApplicationFramework.getInstance()
                    .getColorPallete()
                    .remove((Project) node);
            for(GraffNode graffNode : ((Project) node).getChildren()){
                if(graffNode instanceof Slide){
                    ApplicationFramework.getInstance().getCommandManager().clearHistoryForSlide((Slide) graffNode);
                }
                else if(graffNode instanceof Presentation){
                    for(GraffNode presentationChild : ((Presentation) graffNode).getChildren()){
                        ApplicationFramework.getInstance().getCommandManager().clearHistoryForSlide((Slide) presentationChild);
                    }
                }
            }
        }

        if (parent instanceof GraffNodeComposite) {
            ((GraffNodeComposite) parent).removeChild(node);
            node.setParent(null);
        }

        if(node instanceof Slide){
            ApplicationFramework.getInstance().getCommandManager().clearHistoryForSlide((Slide) node);
        }

        treeModel.removeNodeFromParent(selected);

        TreePath parentPath = new TreePath(parentItem.getPath());
        treeView.setSelectionPath(parentPath);
        treeView.scrollPathToVisible(parentPath);

        mg().notify(new Message("NODE " + node.getTitle() + " DELETED SUCCESSFULLY", MessageType.INFO, node));
        mg().notify(new Message("CHILD_REMOVED", MessageType.INFO, parent));
    }

    @Override
    public void renameSelected(String newTitle) {
        GraffTreeItem selected = getSelectedNode();
        if (selected == null) {
            mg().notify(new Message("NO NODE SELECTED FOR RENAME", MessageType.WARNING, null));
            return;
        }

        GraffNode node = selected.getGraffNode();
        String oldTitle = node.getTitle();

        // Workspace se ne preimenuje – no-op
        if (node instanceof Workspace) {
            mg().notify(new Message("WORKSPACE CANNOT BE RENAMED", MessageType.WARNING, node));
        }

        if (newTitle == null || (newTitle = newTitle.trim()).isEmpty()) {
            mg().notify(new Message("NAME CANNOT BE EMPTY", MessageType.ERROR, node));
            return;
        }

        if (node.getParent() != null && node.getParent().findByName(newTitle) != null && !node.getParent().findByName(newTitle).equals(node)) {
            mg().notify(new Message("NODE WITH THAT NAME IN " + node.getParent().getTitle() + " ALREADY EXISTS", MessageType.ERROR, node));
            return;
        }

        node.setTitle(newTitle);
        treeModel.nodeChanged(selected);

        if(!newTitle.equals(oldTitle))
            mg().notify(new Message("NAME CHANGED SUCCESSFULLY", MessageType.INFO, node));
    }

    @Override
    public GraffTreeItem getSelectedNode() {
        Object selected = treeView.getLastSelectedPathComponent();

        if(selected instanceof GraffTreeItem){
            return (GraffTreeItem) selected;
        }

        return null;
    }

    @Override
    public void setAuthor(String author){
        if(author == null) {
            mg().notify(new Message("AUTHOR CANNOT BE EMPTY", MessageType.WARNING, null));
            removeSelected();
            return;
        }

        ((Project)getSelectedNode().getGraffNode()).setAuthor(author);
    }

    @Override
    public void refreshTree() {
        GraffNodeComposite root = repo().getRoot(); // Workspace
        GraffTreeItem rootItem = new GraffTreeItem(root);

        buildTree(rootItem, root);

        treeModel.setRoot(rootItem);
        treeModel.reload();
    }

    // Ovo ispod su pomocne metode

    private void buildTree(GraffTreeItem parentItem, GraffNodeComposite parentNode) {
        for (GraffNode child : parentNode.getChildren()) {
            GraffTreeItem childItem = new GraffTreeItem(child);
            parentItem.add(childItem);

            if (child instanceof GraffNodeComposite) {
                buildTree(childItem, (GraffNodeComposite) child);
            }
        }
    }

    private boolean canContain(GraffNode parent, NodeType childType) {
        NodeType p = nodeTypeOf(parent);
        return switch (p) {
            case WORKSPACE -> childType == NodeType.PROJECT;
            case PROJECT -> childType == NodeType.PRESENTATION || childType == NodeType.SLIDE;
            case PRESENTATION -> childType == NodeType.SLIDE;
            default -> false;
        };
    }

    private NodeType nodeTypeOf(GraffNode node) {
        if (node instanceof Workspace)    return NodeType.WORKSPACE;
        if (node instanceof Project)      return NodeType.PROJECT;
        if (node instanceof Presentation) return NodeType.PRESENTATION;
        if (node instanceof Slide)        return NodeType.SLIDE;
        if(node instanceof SlideElement) return NodeType.SLIDEELEMENT;

        return null;
    }

    private String suggestTitle(NodeType type) {
        switch (type) {
            case PROJECT:      return "New Project";
            case PRESENTATION: return "New Presentation";
            case SLIDE:        return "New Slide";
            default:           return "New";
        }
    }

    private String nextIndexedTitle(GraffNodeComposite parent, String base, NodeType type) {
        String name = base;
        int index = 1;

        GraffNode found;
        while ((found = parent.findByName(name)) != null && nodeTypeOf(found) == type) {
            name = base + index;
            index++;
        }

        return name;
    }


    private GraffRepository repo() {
        return ApplicationFramework.getInstance().getGraffRepository();
    }

    private MessageGenerator mg() {
        return ApplicationFramework.getInstance().getMessageGenerator();
    }
}
