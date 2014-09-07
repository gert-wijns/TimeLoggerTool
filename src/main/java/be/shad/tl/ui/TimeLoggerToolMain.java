package be.shad.tl.ui;

import java.io.File;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import be.shad.tl.service.TimeLogger;
import be.shad.tl.service.TimeLoggerData;
import be.shad.tl.service.TimeLoggerDataImpl;
import be.shad.tl.service.TimeLoggerImpl;
import be.shad.tl.service.TimeLoggerPersistenceImpl;
import be.shad.tl.service.model.TimeLoggerTask;
import be.shad.tl.ui.form.TimeLoggerController;
import be.shad.tl.ui.form.TimeLoggerModel;
import be.shad.tl.ui.form.TimeLoggerView;

public class TimeLoggerToolMain extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private TimeLoggerData timeLoggerData;
    private TimeLogger timeLogger;

    private TimeLoggerController controller;
    private TimeLoggerModel model;
    private TimeLoggerView view;

    @Override
    public void start(Stage primaryStage) throws Exception {
        File file = new File("save.tl");
        if (!file.exists()) {
            file.createNewFile();
        }

        TimeLoggerPersistenceImpl persistence = new TimeLoggerPersistenceImpl(file);
        this.timeLoggerData = new TimeLoggerDataImpl();
        this.timeLogger = new TimeLoggerImpl(persistence, timeLoggerData);
        persistence.loadSaveFile(timeLogger);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TimeLoggerTool");

        model = new TimeLoggerModel(timeLoggerData);
        controller = new TimeLoggerController(timeLoggerData, timeLogger, model);
        view = new TimeLoggerView(model, controller, primaryStage);

        // Set the application icon.
        this.primaryStage.getIcons().add(new Image("images/application_icon.png"));

        rootLayout = view.loadRootFormControl();
        initRootLayout();
        rootLayout.setCenter(view.loadTimeLoggerFormControl());

        this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                TimeLoggerTask activeTask = timeLoggerData.getActiveTask();
                if (activeTask != null) {
                    timeLogger.stopTask(activeTask.getId());
                }
            }
        });
    }

    /**
     * Initializes the root layout and tries to load the last opened
     * person file.
     */
    public void initRootLayout() {
        // Show the scene containing the root layout.
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        File f = new File("time-logger-tool.css");
        if (f.exists()) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
        } else {
            scene.getStylesheets().add(getClass().getResource("time-logger-tool.css").toExternalForm());
        }
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
