package be.shad.tl.ui.control;

import static be.shad.tl.ui.form.control.TasksFormControl.LOCKED_TAG;
import static be.shad.tl.util.TimeLoggerUtils.toTimeString;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import be.shad.tl.ui.form.TimeLoggerController;
import be.shad.tl.ui.model.TimeLoggerViewTask;

public class TaskListCell extends ListCell<TimeLoggerViewTask> {
    private static final String LOCKED_TASK_STYLE_CLASS = "locked-task";
    private static final String ACTIVE_TASK_STYLE_CLASS = "active-task";
    private final CheckBox active = new CheckBox();
    private final Label task = new Label();
    private final Label duration = new Label();
    private final AnchorPane graphic;
    private final StringProperty lockMenuText;

    public TaskListCell(TimeLoggerController controller) {
        HBox hbox = new HBox(5d);
        active.disableProperty().set(true);
        hbox.getChildren().add(active);
        hbox.getChildren().add(task);

        graphic = new AnchorPane();
        graphic.getChildren().add(hbox);
        graphic.getChildren().add(duration);

        AnchorPane.setLeftAnchor(hbox, 0d);
        AnchorPane.setRightAnchor(duration, 0d);

        ContextMenu rowMenu = new ContextMenu();
        MenuItem lockMenuItem = new MenuItem("Unlock");
        lockMenuText = lockMenuItem.textProperty();
        lockMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (getItem().isLocked()) {
                    controller.removeTag(getItem().getId(), LOCKED_TAG);
                } else {
                    controller.addTag(getItem().getId(), LOCKED_TAG);
                }
            }
        });
        rowMenu.getItems().setAll(lockMenuItem);

        contextMenuProperty().bind(
            Bindings.when(Bindings.isNotNull(itemProperty()))
            .then(rowMenu)
            .otherwise((ContextMenu) null));
    }

    @Override
    protected void updateItem(TimeLoggerViewTask item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
            unsetTaskStyle(ACTIVE_TASK_STYLE_CLASS);
            unsetTaskStyle(LOCKED_TASK_STYLE_CLASS);
        } else {
            long duration = item.getDuration();
            this.duration.setText(toTimeString(duration));
            active.setSelected(item.isActive());
            task.setText(item.getName());
            setGraphic(graphic);
            if (item.isActive()) {
                setTaskStyle(ACTIVE_TASK_STYLE_CLASS);
            } else if (item.isLocked()) {
                lockMenuText.set("Unlock");
                setTaskStyle(LOCKED_TASK_STYLE_CLASS);
            } else {
                lockMenuText.set("Lock");
                unsetTaskStyle(ACTIVE_TASK_STYLE_CLASS);
                unsetTaskStyle(LOCKED_TASK_STYLE_CLASS);
            }
        }
    }

    private void unsetTaskStyle(String styleClass) {
        getStyleClass().remove(styleClass);
    }

    private void setTaskStyle(String styleClass) {
        if (!getStyleClass().contains(styleClass)) {
            getStyleClass().add(styleClass);
        }
    }
}
