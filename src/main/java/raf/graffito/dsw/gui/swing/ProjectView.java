package raf.graffito.dsw.gui.swing;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.observer.Subscriber;

import javax.swing.*;

public class ProjectView implements Subscriber {
    private Project project;
    private RightPanel rightPanel;   // da bismo zvali buildTabsFor / setProject
    private JTabbedPane tabs;        // zbog brzih izmena naslova taba
    private JLabel lblProject;       // azuriranje imena projekta

    public ProjectView(Project project, RightPanel rightPanel, JTabbedPane tabs, JLabel lblProject) {
        this.project = project;
        this.rightPanel = rightPanel;
        this.tabs = tabs;
        this.lblProject = lblProject;

        ApplicationFramework.getInstance()
                .getMessageGenerator()
                .addSubscriber(this);

        // inicijalni header
        lblProject.setText("Project: " + project.getTitle());
    }

    public void dispose() {
        ApplicationFramework.getInstance()
                .getMessageGenerator()
                .removeSubscriber(this);
    }

    @Override
    public void update(Message message) {
        Object src = message.getSource();
        if (src == null) return;

        if (src == project && project.getParent() == null) {
            SwingUtilities.invokeLater(() -> {
                rightPanel.buildTabsFor(null);
                rightPanel.setProject(null);
                rightPanel.setAuthor(null);
                tabs.removeAll();
                dispose();
            });
            return;
        }

        boolean projectTouched =
                src == project ||
                        (src instanceof Presentation && ((Presentation) src).getParent() == project);

        if (!projectTouched) return;

        SwingUtilities.invokeLater(() -> {
            // Ako je rename projekta – osveži header
            if (src == project) {
                lblProject.setText("Project: " + project.getTitle());
                // add/remove prezentacije: najjednostavnije i najstabilnije – rebuild tabova
                rightPanel.buildTabsFor(project);
                rightPanel.setAuthor(project);
            }

            // Ako je rename konkretne prezentacije – promeni naziv njenog taba
            if (src instanceof Presentation p) {
                for (int i = 0; i < tabs.getTabCount(); i++) {
                    // Koristi novu metodu getPresentationViewAt
                    PresentationView pv = rightPanel.getPresentationViewAt(i);
                    if (pv != null && pv.getPresentation() == p) {
                        tabs.setTitleAt(i, p.getTitle());
                        if (tabs.getSelectedIndex() == i) {
                            rightPanel.setPresentation(p);
                        }
                        return;
                    }
                }
                rightPanel.buildTabsFor(project);
            }
        });
    }
}