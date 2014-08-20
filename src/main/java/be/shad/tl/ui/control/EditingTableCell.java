package be.shad.tl.ui.control;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class EditingTableCell<T> extends TableCell<T, String> {

    private TextField textField;

    public EditingTableCell() {
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
            textField.requestFocus();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
        setGraphic(null);
        textField = null;
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
            return;
        }
        if (isEditing()) {
            if (textField != null) {
                textField.setText(getItem());
            }
            setText(null);
            setGraphic(textField);
        } else {
            setText(getItem());
            setGraphic(null);
        }
    }

    private void createTextField() {
        textField = new TextField(getItem());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        ChangeListener<Boolean> focusListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                if (!arg2) {
                    changeValue();
                }
            }
        };
        textField.focusedProperty().addListener(focusListener);
        textField.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    event.consume();
                    textField.focusedProperty().removeListener(focusListener);
                    cancelEdit();
                } else if (event.getCode() == KeyCode.ENTER) {
                    changeValue();
                }
            }
        });
    }

    private void changeValue() {
        String value = textField.getText();
        cancelEdit();
        observedValue().setValue(value);
    }
    
    private StringProperty observedValue() {
        return (StringProperty) getTableColumn().getCellObservableValue(getIndex());
    }
}
