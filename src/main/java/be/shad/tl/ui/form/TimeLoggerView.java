package be.shad.tl.ui.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import be.shad.tl.ui.form.control.AbstractFormControl;

public class TimeLoggerView {

    private final TimeLoggerController controller;
    private final TimeLoggerModel model;
    private final Stage primaryStage;

    public TimeLoggerView(TimeLoggerModel model,
            TimeLoggerController controller,
            Stage primaryStage) {
        this.controller = controller;
        this.model = model;
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public BorderPane loadRootFormControl() {
        return load(BorderPane.class, "RootFormControl");
    }

    public AnchorPane loadTasksFormControl() {
        return load(AnchorPane.class, "TasksFormControl");
    }

    public AnchorPane loadTaskDetailsControl() {
        return load(AnchorPane.class, "TaskDetailsFormControl");
    }

    public AnchorPane loadEntriesOverviewControl() {
        return load(AnchorPane.class, "EntriesOverviewFormControl");
    }

    public AnchorPane loadTaskEntriesControl() {
        return load(AnchorPane.class, "TaskEntriesFormControl");
    }

    public AnchorPane loadTimeLoggerFormControl() {
        return load(AnchorPane.class, "TimeLoggerFormControl");
    }

    public AnchorPane loadSettingsFormControl() {
        return load(AnchorPane.class, "SettingsFormControl");
    }

    @SuppressWarnings("unchecked")
    private <N extends Node> N load(Class<N> nodeType, String name) {
        String resource = "/be/shad/tl/ui/form/control/" + name + ".fxml";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TimeLoggerView.class.getResource(resource));
            loader.setControllerFactory((p) -> createController(p));
            return (N) loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Object createController(Class<?> controllerClass) {
        try {
            Object controllerObj = controllerClass.newInstance();
            if (controllerObj instanceof AbstractFormControl) {
                AbstractFormControl control = (AbstractFormControl) controllerObj;
                control.setController(controller);
                control.setModel(model);
                control.setView(this);
            }
            return controllerObj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
