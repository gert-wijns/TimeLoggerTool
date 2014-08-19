package be.shad.tl.ui.control;

import static be.shad.tl.util.TimeLoggerUtils.toTimeString;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import be.shad.tl.ui.model.TimeLoggerViewTask;

public class TaskListCell extends ListCell<TimeLoggerViewTask> {
    private final CheckBox active = new CheckBox();
    private final Label task = new Label();
    private final Label duration = new Label();
    private final AnchorPane graphic;

    public TaskListCell() {
        HBox hbox = new HBox(5d);
        active.disableProperty().set(true);
        hbox.getChildren().add(active);
        hbox.getChildren().add(task);

        graphic = new AnchorPane();
        graphic.getChildren().add(hbox);
        graphic.getChildren().add(duration);

        AnchorPane.setLeftAnchor(hbox, 0d);
        AnchorPane.setRightAnchor(duration, 0d);
    }

    @Override
    protected void updateItem(TimeLoggerViewTask item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            long duration = item.getDuration();
            this.duration.setText(toTimeString(duration));
            active.setSelected(item.isActive());
            task.setText(item.getName());
            setGraphic(graphic);
            if (item.isActive()) {
                setActiveStyle();
                return;
            }
        }
        setInactiveStyle();
    }

    private void setInactiveStyle() {
        getStyleClass().remove("active-task");
    }

    private void setActiveStyle() {
        if (!getStyleClass().contains("active-task")) {
            getStyleClass().add("active-task");
        }
    }
}
