package be.shad.tl.ui.control;

import static be.shad.tl.util.TimeLoggerUtils.setAnchor;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

public class TimeChart extends AnchorPane {

    private ScrollPane scrollPane = new ScrollPane();
    private DoubleProperty rowScale = new SimpleDoubleProperty(20d);

    private ObservableList<TimeChartRow> rows = FXCollections.observableArrayList();
    private AnchorPane rowsContent = new AnchorPane();
    private ObservableList<Node> rowsContentList = rowsContent.getChildren();
    private TimeLine timeLine = new TimeLine();

    public void initialize() {
        getChildren().add(scrollPane);
        setAnchor(scrollPane, 0d, 0d, 0d, 0d);
        scrollPane.setContent(rowsContent);
        rows.addListener((ListChangeListener.Change<? extends TimeChartRow> change) -> updateRows(change));
    }

    public void addRow(int rowIndex, TimeChartRow row) {
        rows.add(Math.min(rows.size(), rowIndex), row);
    }

    private void updateRows(Change<? extends TimeChartRow> change) {
        rowsContentList.clear();
        double rowIndex = 0;
        for(TimeChartRow row: rows) {
            rowsContentList.add(row);
            row.setPrefHeight(rowScale.get());
            row.setPrefWidth(5000);
            setAnchor(row, 0d, null, rowIndex * rowScale.get(), null);
            rowIndex++;
        }
    }

    public TimeLine getTimeLine() {
        return timeLine;
    }
}
