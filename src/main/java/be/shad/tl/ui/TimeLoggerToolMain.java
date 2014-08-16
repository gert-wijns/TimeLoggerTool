package be.shad.tl.ui;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import be.shad.tl.service.TimeLoggerDao;
import be.shad.tl.service.TimeLoggerDaoImpl;
import be.shad.tl.service.model.TimeLoggerEntry;
import be.shad.tl.service.model.TimeLoggerTag;
import be.shad.tl.service.model.TimeLoggerTask;
import be.shad.tl.ui.view.TimeLoggerFormController;

public class TimeLoggerToolMain extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private TimeLoggerDao timeLoggerDao;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.timeLoggerDao = new TimeLoggerDaoImpl();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TimeLoggerTool");

        insertMockData();
        // Set the application icon.
        //this.primaryStage.getIcons().add(new Image("file:resources/images/address_book_32.png"));

        initRootLayout();
        showTasksForm();
    }

    private void insertMockData() {
        TimeLoggerTask task = new TimeLoggerTask();
        task.setId(UUID.randomUUID().toString());
        task.setName("Task1");
        task.setDescription("Task description");
        timeLoggerDao.saveTask(task);

        TimeLoggerEntry entry = new TimeLoggerEntry();
        entry.setId(UUID.randomUUID().toString());
        entry.setStartDate(new Date());
        timeLoggerDao.saveEntry(entry);

        TimeLoggerTag tag = new TimeLoggerTag();
        tag.setCode("Jira");
        timeLoggerDao.saveTag(tag);

        timeLoggerDao.addEntry(task, entry);
        timeLoggerDao.addTag(task, tag);
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
            controller.setTimeLoggerDao(timeLoggerDao);
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
            //RootLayoutController controller = loader.getController();

            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
