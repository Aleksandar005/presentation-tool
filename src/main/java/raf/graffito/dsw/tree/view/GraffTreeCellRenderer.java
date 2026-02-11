package raf.graffito.dsw.tree.view;

import raf.graffito.dsw.model.implementation.slide.ImageSlideElement;
import raf.graffito.dsw.model.implementation.slide.LogoSlideElement;
import raf.graffito.dsw.model.implementation.slide.TextSlideElement;
import raf.graffito.dsw.tree.model.GraffTreeItem;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.model.implementation.slide.Slide;
import raf.graffito.dsw.model.implementation.Workspace;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.net.URL;

// Ova klasa sluzi za prikazivanje GraffNode-ova

public class GraffTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row, hasFocus);

        URL imageURL = null;
        GraffNode node = ((GraffTreeItem)value).getGraffNode();
        setText(node.getTitle());

        if(node instanceof Workspace){
            imageURL = getClass().getResource("/images/workspace.png");
        } else if(node instanceof Project){
            imageURL = getClass().getResource("/images/project.png");
        } else if(node instanceof Presentation){
            imageURL = getClass().getResource("/images/presentation.png");
        } else if(node instanceof Slide){
            imageURL = getClass().getResource("/images/slide.png");
        } else if(node instanceof TextSlideElement){
            imageURL = getClass().getResource("/images/textElement.png");
        } else if(node instanceof ImageSlideElement){
            imageURL = getClass().getResource("/images/imageElement.png");
        } else if(node instanceof LogoSlideElement){
            imageURL = getClass().getResource("/images/logoElement.png");
        }

        Icon icon = null;
        if(imageURL != null){
            ImageIcon originalIcon = new ImageIcon(imageURL);
            Image scaled = originalIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaled);
        }

        setIcon(icon);
        return this;
    }

}
