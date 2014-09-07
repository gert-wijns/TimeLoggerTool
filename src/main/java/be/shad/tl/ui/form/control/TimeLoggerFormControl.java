package be.shad.tl.ui.form.control;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;


public class TimeLoggerFormControl extends AbstractFormControl {
    @FXML Accordion accordion;
    @FXML TitledPane entriesOverviewPane;

    @Override
    protected void initalizeControl() {
        accordion.setExpandedPane(entriesOverviewPane);
    }
}
