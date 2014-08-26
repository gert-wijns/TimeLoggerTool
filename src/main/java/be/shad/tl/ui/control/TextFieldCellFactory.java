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
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class TextFieldCellFactory<T, S> implements Callback<TableColumn<T, S>, TableCell<T, S>> {
    //StringConverter<T>
    public static <T> Callback<TableColumn<T, String>, TableCell<T, String>> forTableColumn() {
        return new TextFieldCellFactory<T, String>(new DefaultStringConverter());
    }

    public static <T, S> Callback<TableColumn<T, S>, TableCell<T, S>> forTableColumn(
            final StringConverter<S> converter) {
        return new TextFieldCellFactory<T, S>(converter);
    }

    private StringConverter<S> converter;

    public TextFieldCellFactory(StringConverter<S> converter) {
        this.converter = converter;
    }

    @Override
    public TableCell<T, S> call(TableColumn<T, S> param) {
        return new TextFieldCell<>(converter);
    }

    public static class TextFieldCell<T, S> extends TableCell<T, S> {
        private final StringConverter<S> converter;
        private TextField textField;

        public TextFieldCell(StringConverter<S> converter) {
            this.converter = converter;
            this.textField = new TextField();
            // padding to 0 so the 'graphics' and 'text' are equal in size
            this.textField.setStyle("-fx-padding: 0;");
            this.setGraphic(this.textField);

            hoverProperty().addListener((property, oldValue, newValue) -> updateContentDisplay());
            this.textField.focusedProperty().addListener((property, oldValue, newValue) -> onFocusChanged(newValue));
            this.textField.textProperty().addListener((property, oldValue, newValue) -> setText(newValue));
            this.textField.addEventFilter(KeyEvent.ANY, (event) -> {
                if (event.getCode() == KeyCode.ENTER
                        || event.getCode() == KeyCode.ESCAPE) {
                    this.textField.getParent().requestFocus();
                }
            });
        }

        /**
         * Reset text and textfield value to the backing property value
         */
        @Override
        protected void updateItem(S item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                textField.setText(converter.toString(getCellProperty().getValue()));
                setText(textField.getText());
            } else {
                setText(null);
            }
            updateContentDisplay();
        }

        private void onFocusChanged(Boolean focused) {
            updateContentDisplay();
            if (!focused) {
                CellEditEvent<T, S> editEvent = new CellEditEvent<>(
                    getTableView(),
                    new TablePosition<>(getTableView(), getIndex(), getTableColumn()),
                    TableColumn.editCommitEvent(),
                    converter.fromString(getText())
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

        private Property<S> getCellProperty() {
            return (Property<S>) getTableColumn().getCellObservableValue(getIndex());
        }
    }

}
