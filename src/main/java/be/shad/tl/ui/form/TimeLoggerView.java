package be.shad.tl.ui.form;

import java.io.IOException;
import java.util.function.Consumer;

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

    public AnchorPane loadTaskChangeFormControl(Consumer<AbstractFormControl> controlInitializer) {
        return load(AnchorPane.class, "TaskChangeFormControl", controlInitializer);
    }

    private <N extends Node> N load(Class<N> nodeType, String name) {
        return load(nodeType, name, null);
    }

    @SuppressWarnings("unchecked")
    private <N extends Node> N load(Class<N> nodeType, String name, Consumer<AbstractFormControl> controlInitializer) {
        String resource = "/be/shad/tl/ui/form/control/" + name + ".fxml";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TimeLoggerView.class.getResource(resource));
            loader.setControllerFactory((p) -> createController(p, controlInitializer));
            return (N) loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Object createController(Class<?> controllerClass,
            Consumer<AbstractFormControl> controlInitializer) {
        try {
            Object controllerObj = controllerClass.newInstance();
            if (controllerObj instanceof AbstractFormControl) {
                AbstractFormControl control = (AbstractFormControl) controllerObj;
                control.setController(controller);
                control.setModel(model);
                control.setView(this);
                if (controlInitializer != null) {
                    controlInitializer.accept(control);
                }
            }
            return controllerObj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
