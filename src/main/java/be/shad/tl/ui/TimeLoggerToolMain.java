package be.shad.tl.ui;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import be.shad.tl.service.TimeLogger;
import be.shad.tl.service.TimeLoggerDao;
import be.shad.tl.service.TimeLoggerDaoImpl;
import be.shad.tl.service.TimeLoggerImpl;
import be.shad.tl.ui.view.RootLayoutController;
import be.shad.tl.ui.view.TimeLoggerFormController;

public class TimeLoggerToolMain extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private TimeLoggerDao timeLoggerDao;
    private TimeLogger timeLogger;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.timeLoggerDao = new TimeLoggerDaoImpl();
        this.timeLogger = new TimeLoggerImpl(timeLoggerDao);
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TimeLoggerTool");

        //insertMockData();
        // Set the application icon.
        //this.primaryStage.getIcons().add(new Image("file:resources/images/address_book_32.png"));

        initRootLayout();
        showTasksForm();
    }

    private void insertMockData() {
        String task1 = timeLogger.createTask("Task1");
        timeLogger.setTaskDescription(task1, "A magnificent description");
        timeLogger.startTask(task1, new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(4)));
        timeLogger.stopTask(task1, new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(3)));
        timeLogger.startTask(task1, new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(2)));
        timeLogger.stopTask(task1, new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)));
        timeLogger.tagTask(task1, "Jira");
        String task2 = timeLogger.createTask("Task2");
        timeLogger.startTask(task2, new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(3)));
        timeLogger.stopTask(task2, new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(2)));
        timeLogger.startTask(task2, new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)));
        timeLogger.stopTask(task2, new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(0)));
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
            controller.setTimeLoggerDao(timeLoggerDao, timeLogger);
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

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setTimeLoggerDao(timeLoggerDao);

            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
