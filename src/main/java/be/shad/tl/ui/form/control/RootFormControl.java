package be.shad.tl.ui.form.control;

import static be.shad.tl.util.TimeLoggerUtils.toTimeString;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import be.shad.tl.service.model.TimeLoggerEntry;
import be.shad.tl.service.model.TimeLoggerTask;

public class RootFormControl extends AbstractFormControl {

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleShowSettings() {
        Stage dialog = new Stage(StageStyle.DECORATED);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(view.getPrimaryStage());
        Scene scene = new Scene(view.loadSettingsFormControl());
        dialog.setTitle("Settings");
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    @FXML
    private void handleExport() {
        StringBuilder exportSB = new StringBuilder();
        long logTotal = 0;
        for(TimeLoggerTask task: model.getTimeLoggerData().getTasks()) {
            long taskTotal = 0;
            for(TimeLoggerEntry entry: model.getTimeLoggerData().getTaskEntries(task.getId())) {
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
        dialog.setTitle("Export");
        TextArea textArea = new TextArea(exportSB.toString());
        Scene scene = new Scene(new AnchorPane(textArea));
        AnchorPane.setTopAnchor(textArea, 0d);
        AnchorPane.setBottomAnchor(textArea, 0d);
        AnchorPane.setLeftAnchor(textArea, 0d);
        AnchorPane.setRightAnchor(textArea, 0d);
        dialog.setScene(scene);
        dialog.show();
    }
}
