package be.shad.tl.ui.view;

import java.util.concurrent.TimeUnit;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import be.shad.tl.service.TimeLoggerDao;
import be.shad.tl.service.model.TimeLoggerEntry;
import be.shad.tl.service.model.TimeLoggerTask;

public class RootLayoutController {
    private TimeLoggerDao timeLoggerDao;

    public void setTimeLoggerDao(TimeLoggerDao timeLoggerDao) {
        this.timeLoggerDao = timeLoggerDao;
    }

    @FXML
    private void handleExport() {
        StringBuilder exportSB = new StringBuilder();
        long logTotal = 0;
        for(TimeLoggerTask task: timeLoggerDao.getTasks()) {
            long taskTotal = 0;
            for(TimeLoggerEntry entry: timeLoggerDao.getTaskEntries(task.getId())) {
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

    private String toTimeString(Long duration) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration - TimeUnit.MINUTES.toMillis(minutes));
        return String.format("%dm %2ds", minutes, seconds);
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
