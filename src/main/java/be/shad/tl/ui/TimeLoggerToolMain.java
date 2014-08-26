package be.shad.tl.ui;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import be.shad.tl.service.TimeLogger;
import be.shad.tl.service.TimeLoggerData;
import be.shad.tl.service.TimeLoggerDataImpl;
import be.shad.tl.service.TimeLoggerImpl;
import be.shad.tl.service.TimeLoggerPersistenceImpl;
import be.shad.tl.ui.view.RootLayoutController;
import be.shad.tl.ui.view.TimeLoggerFormController;

public class TimeLoggerToolMain extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private TimeLoggerData timeLoggerData;
    private TimeLogger timeLogger;

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

        // Set the application icon.
        //this.primaryStage.getIcons().add(new Image("file:resources/images/address_book_32.png"));

        initRootLayout();
        showTasksForm();
    }

    private void showTasksForm() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TimeLoggerToolMain.class.getResource("view/TimeLoggerForm.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            TimeLoggerFormController controller = loader.getController();
            controller.setTimeLoggerData(timeLoggerData);
            controller.setTimeLogger(timeLogger);

            this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    controller.stopActiveTask();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the root layout and tries to load the last opened
     * person file.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TimeLoggerToolMain.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

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

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setTimeLoggerData(timeLoggerData);
            controller.setTimeLogger(timeLogger);

            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
