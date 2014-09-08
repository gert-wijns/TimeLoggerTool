package be.shad.tl.ui.form.control;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;


public class TimeLoggerFormControl extends AbstractFormControl {
    @FXML Accordion accordion;
    @FXML TitledPane entriesOverviewPane;

    @Override
    protected void initalizeControl() {
        accordion.setExpandedPane(entriesOverviewPane);
        SimpleIntegerProperty expandedCount = new SimpleIntegerProperty(1);
        for(TitledPane pane: accordion.getPanes()) {
            pane.expandedProperty().addListener((prop, o, n) -> {
                expandedCount.set(expandedCount.get() + (n ? 1: -1));
                if (expandedCount.get() == 0) {
                    Platform.runLater(() -> accordion.setExpandedPane(entriesOverviewPane));
                }
            });
        }
    }
}
