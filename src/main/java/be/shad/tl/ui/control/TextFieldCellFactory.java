package be.shad.tl.ui.control;

import javafx.beans.property.Property;
import javafx.event.Event;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class TextFieldCellFactory<T> implements Callback<TableColumn<T, String>, TableCell<T, String>> {

    @Override
    public TableCell<T, String> call(TableColumn<T, String> param) {
        return new TextFieldCell<>();
    }

    public static class TextFieldCell<T> extends TableCell<T, String> {
        private TextField textField;

        public TextFieldCell() {
            textField = new TextField();
            // padding to 0 so the 'graphics' and 'text' are equal in size
            textField.setStyle("-fx-padding: 0;");
            this.setGraphic(textField);

            hoverProperty().addListener((property, oldValue, newValue) -> updateContentDisplay());
            textField.focusedProperty().addListener((property, oldValue, newValue) -> onFocusChanged(newValue));
            textField.textProperty().addListener((property, oldValue, newValue) -> setText(newValue));
            textField.addEventFilter(KeyEvent.ANY, (event) -> {
                if (event.getCode() == KeyCode.ENTER
                        || event.getCode() == KeyCode.ESCAPE) {
                    textField.getParent().requestFocus();
                }
            });
        }

        /**
         * Reset text and textfield value to the backing property value
         */
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                textField.setText(getCellProperty().getValue());
                setText(textField.getText());
            } else {
                setText(null);
            }
            updateContentDisplay();
        }

        private void onFocusChanged(Boolean focused) {
            updateContentDisplay();
            if (!focused) {
                CellEditEvent<T, String> editEvent = new CellEditEvent<>(
                    getTableView(),
                    new TablePosition<>(getTableView(), getIndex(), getTableColumn()),
                    TableColumn.editCommitEvent(),
                    getText()
                );
                Event.fireEvent(getTableColumn(), editEvent);
            }
        }

        /**
         * switch to 'text' mode when empty.
         * switch to 'graphic' mode when focused or hovering.
         */
        private void updateContentDisplay() {
            if (!isEmpty() && (textField.isFocused() || isHover())) {
                this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                this.setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }

        private Property<String> getCellProperty() {
            return (Property<String>) getTableColumn().getCellObservableValue(getIndex());
        }
    }

}
