package be.shad.tl.ui.control;

import static be.shad.tl.util.TimeLoggerUtils.setAnchor;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

public class TimeChartRow extends AnchorPane {
    private final TimeChart chart;
    private ObservableList<TimeChartEntry> entries;

    public TimeChartRow(TimeChart chart) {
        this.entries = FXCollections.observableArrayList();
        this.entries.addListener((ListChangeListener.Change<? extends TimeChartEntry> change) -> updateEntries(change));
        this.chart = chart;
    }

    private void updateEntries(Change<? extends TimeChartEntry> change) {
        getChildren().clear();
        TimeLine timeLine = chart.getTimeLine();
        double timeScale = timeLine.getTimeScale();
        long startTime = timeLine.getStartDate().getTime();
        for(TimeChartEntry entry: entries) {
            getChildren().add(entry);
            double xOffset = (entry.getStartDate().getTime() - startTime) / timeScale;
            double duration = (entry.getEndDate().getTime() - entry.getStartDate().getTime()) / timeScale;
            entry.setPrefWidth(duration);
            entry.setPrefHeight(20);
            setAnchor(entry, xOffset, null, null, 0d);
        }
    }

    public void addEntry(TimeChartEntry entry) {
        entries.add(entry);
    }
}
