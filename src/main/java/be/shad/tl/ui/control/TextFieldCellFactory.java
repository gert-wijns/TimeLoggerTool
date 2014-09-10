package be.shad.tl.ui.control;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
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

import org.controlsfx.dialog.DialogStyle;
import org.controlsfx.dialog.Dialogs;

import be.shad.tl.ui.converter.DefaultStringConverter;
import be.shad.tl.ui.converter.StringConversionError;
import be.shad.tl.ui.converter.StringConverter;

public class TextFieldCellFactory<T, S, C extends StringConverter<S>> implements Callback<TableColumn<T, S>, TableCell<T, S>> {
    //StringConverter<T>
    public static <T> Callback<TableColumn<T, String>, TableCell<T, String>> forTableColumn() {
        return new TextFieldCellFactory<T, String, StringConverter<String>>(
                new SimpleObjectProperty<>(new DefaultStringConverter()));
    }

    /**
     * Create a callback to create table cells which will listen to the converter invalidation
     * to update items in case the converter configuration may change after the table was created.
     */
    public static <T, S, C extends StringConverter<S>> Callback<TableColumn<T, S>, TableCell<T, S>> forTableColumn(
            final ObjectProperty<C> converter) {
        return new TextFieldCellFactory<T, S, C>(converter);
    }

    private ObjectProperty<C> converter;

    public TextFieldCellFactory(ObjectProperty<C> converter) {
        this.converter = converter;
    }

    @Override
    public TableCell<T, S> call(TableColumn<T, S> param) {
        return new TextFieldCell<>(converter);
    }

    public static class TextFieldCell<T, S, C extends StringConverter<S>> extends TableCell<T, S> {
        private final ObjectProperty<C> converter;
        private InvalidationListener converterListener = ((o) -> onConverterChanged());
        private TextField textField;

        public TextFieldCell(ObjectProperty<C> converter) {
            this.converter = converter;
            this.converter.addListener(new WeakInvalidationListener(converterListener));
            this.textField = new TextField();
            // padding to 0 so the 'graphics' and 'text' are equal in size
            this.textField.getStyleClass().add("table-cell-text-field");
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

        private void onConverterChanged() {
            if (!isEmpty()) {
                updateIndex(getIndex());
            }
        }

        /**
         * Reset text and textfield value to the backing property value
         */
        @Override
        protected void updateItem(S item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                textField.setText(converter.get().toEditString(getCellProperty().getValue()));
                setText(converter.get().toDisplayString(getCellProperty().getValue()));
            } else {
                setText(null);
            }
            updateContentDisplay();
        }

        private void onFocusChanged(Boolean focused) {
            updateContentDisplay();
            if (!focused) {
                try {
                    S converted = converter.get().fromString(textField.getText());
                    CellEditEvent<T, S> editEvent = new CellEditEvent<>(
                        getTableView(),
                        new TablePosition<>(getTableView(), getIndex(), getTableColumn()),
                        TableColumn.editCommitEvent(),
                        converted
                    );
                    Event.fireEvent(getTableColumn(), editEvent);
                } catch (StringConversionError e) {
                    onConversionError(e);
                }
            }
        }

        private void onConversionError(StringConversionError e) {
            Dialogs.create().
                style(DialogStyle.CROSS_PLATFORM_DARK).
                title("Conversion failed").
                message("Couldn't convert value, illegal format.").
                showExceptionInNewWindow(e);
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
