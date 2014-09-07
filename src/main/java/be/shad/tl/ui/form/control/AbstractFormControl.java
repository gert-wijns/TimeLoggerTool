package be.shad.tl.ui.form.control;

import javafx.fxml.FXML;
import be.shad.tl.ui.form.TimeLoggerController;
import be.shad.tl.ui.form.TimeLoggerModel;
import be.shad.tl.ui.form.TimeLoggerView;

public abstract class AbstractFormControl {
    protected TimeLoggerController controller;
    protected TimeLoggerModel model;
    protected TimeLoggerView view;

    public void setController(TimeLoggerController controller) {
        this.controller = controller;
    }

    public void setModel(TimeLoggerModel model) {
        this.model = model;
    }

    public void setView(TimeLoggerView view) {
        this.view = view;
    }

    @FXML
    public final void initialize() {
        initalizeControl();
        registerEventBusListener();
        initializeDone();
    }

    protected void initializeDone() {
        refresh();
    }

    protected void refresh() {
        // refresh and so on
    }

    protected void registerEventBusListener() {
        model.getEventBus().register(this);
    }

    protected void initalizeControl() {
        // nothing by default;
    }
}
