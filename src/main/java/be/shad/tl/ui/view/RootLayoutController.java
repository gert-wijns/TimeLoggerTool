package be.shad.tl.ui.view;

import static be.shad.tl.util.TimeLoggerUtils.toTimeString;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import be.shad.tl.service.TimeLogger;
import be.shad.tl.service.TimeLoggerData;
import be.shad.tl.service.model.TimeLoggerEntry;
import be.shad.tl.service.model.TimeLoggerTask;
import be.shad.tl.ui.TimeLoggerToolMain;

public class RootLayoutController {
    private TimeLoggerData timeLoggerData;
    private TimeLogger timeLogger;
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setTimeLogger(TimeLogger timeLogger) {
        this.timeLogger = timeLogger;
    }

    public void setTimeLoggerData(TimeLoggerData timeLoggerData) {
        this.timeLoggerData = timeLoggerData;
    }

    @FXML
    private void handleExport() {
        StringBuilder exportSB = new StringBuilder();
        long logTotal = 0;
        for(TimeLoggerTask task: timeLoggerData.getTasks()) {
            long taskTotal = 0;
            for(TimeLoggerEntry entry: timeLoggerData.getTaskEntries(task.getId())) {
                if (entry.getEndDate() == null) {
                    taskTotal += System.currentTimeMillis() - entry.getStartDate().getTime();
                } else {
                    taskTotal += entry.getEndDate().getTime() - entry.getStartDate().getTime();
                }
            }
            exportSB.append(task.getName()).append(": ").append(toTimeString(taskTotal)).append("\n");
            logTotal += taskTotal;
        }
        exportSB.append("\nTotal: ").append(toTimeString(logTotal));

        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        Scene scene = new Scene(new Group(new TextArea(exportSB.toString())));
        dialog.setScene(scene);
        dialog.show();
    }

    @FXML
    private void handleShowEditEntriesList() {
        Stage dialog = new Stage(StageStyle.DECORATED);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(primaryStage);
        Scene scene = new Scene(loadEditEntriesListFXML());
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private Parent loadEditEntriesListFXML() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TimeLoggerToolMain.class.getResource("view/TimeLoggerEntriesForm.fxml"));
            Parent entriesPane = (Parent) loader.load();

            // Give the controller access to the main app.
            TimeLoggerEntriesFormController controller = loader.getController();
            controller.setTimeLoggerData(timeLoggerData);
            controller.setTimeLogger(timeLogger);
            controller.refresh();

            return entriesPane;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
