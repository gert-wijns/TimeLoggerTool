package be.shad.tl.ui;

import static java.util.concurrent.TimeUnit.HOURS;

import java.util.Date;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import be.shad.tl.ui.control.TimeChart;
import be.shad.tl.ui.control.TimeChartEntry;
import be.shad.tl.ui.control.TimeChartRow;

public class TempTimeChartMockup {

    public static final TimeChart createTimeChart() {
        Date startDate = new Date();
        TimeChart timeChart = new TimeChart();
        timeChart.initialize();
        createRow(timeChart, 0, startDate, HOURS.toMillis(1));
        createRow(timeChart, 1, new Date(startDate.getTime() + HOURS.toMillis(1)), HOURS.toMillis(2));
        createRow(timeChart, 2, new Date(startDate.getTime() + HOURS.toMillis(3)), HOURS.toMillis(4));
        createRow(timeChart, 3, new Date(startDate.getTime() + HOURS.toMillis(7)), HOURS.toMillis(1));
        return timeChart;
    }

    private static void createRow(TimeChart timeChart, int rowIdx, Date startDate, long duration) {
        TimeChartRow row = new TimeChartRow(timeChart);
        TimeChartEntry entry = new TimeChartEntry();
        entry.setRow(row);
        entry.setStartDate(startDate);
        entry.setEndDate(new Date(startDate.getTime() + duration));
        entry.getStyleClass().add("chart-entry-" + ((rowIdx%12)+1));
        entry.setSkin(new TimeChartEntryskin(entry));
        row.addEntry(entry);
        timeChart.addRow(rowIdx, row);
    }

    private static class TimeChartEntryskin2 extends SkinBase<TimeChartEntry> {
        private StackPane leftPane;
        private StackPane rightPane;

        protected TimeChartEntryskin2(TimeChartEntry control) {
            super(control);

            Region leftRegion = new Region();
            leftRegion.getStyleClass().addAll("graphic");
            StackPane leftPane = new StackPane(leftRegion);
            leftPane.getStyleClass().addAll("clear-button"); //styleClass + "-button"); //$NON-NLS-1$
            leftPane.setStyle("-fx-background-color: black;");
            leftPane.setOpacity(1.0);
            leftPane.setCursor(Cursor.DEFAULT);
            this.leftPane = new StackPane(leftPane);
            this.leftPane.setAlignment(Pos.CENTER_LEFT);
            this.leftPane.getStyleClass().add("left-pane");
            this.leftPane.getStyleClass().add("clearable-field");
            this.leftPane.setPrefWidth(20);
            this.leftPane.setPrefHeight(20);

            Region rightRegion = new Region();
            rightRegion.getStyleClass().addAll("graphic");
            StackPane rightPane = new StackPane(rightRegion);
            rightPane.getStyleClass().addAll("clear-button"); //styleClass + "-button"); //$NON-NLS-1$
            rightPane.setStyle("-fx-background-color: black;");
            rightPane.setOpacity(1.0);
            rightPane.setCursor(Cursor.DEFAULT);
            this.rightPane = new StackPane(rightPane);
            this.rightPane.setAlignment(Pos.CENTER_RIGHT);
            this.rightPane.getStyleClass().add("right-pane");
            this.rightPane.getStyleClass().add("clearable-field");
            this.rightPane.setPrefWidth(20);
            this.rightPane.setPrefHeight(20);

            AnchorPane wrapper = new AnchorPane();
            wrapper.getStyleClass().add("chart-entry");
            wrapper.getChildren().add(this.leftPane);
            wrapper.getChildren().add(this.rightPane);
            getChildren().add(wrapper);
        }
    }

    private static class TimeChartEntryskin extends SkinBase<TimeChartEntry> {
        private StackPane leftPane;
        private StackPane rightPane;

        protected TimeChartEntryskin(TimeChartEntry control) {
            super(control);

            Region leftRegion = new Region();
            leftRegion.getStyleClass().addAll("graphic");
            StackPane leftPane = new StackPane(leftRegion);
            leftPane.getStyleClass().addAll("clear-button", "chart-entry");
            leftPane.setStyle("-fx-background-color: black;");
            leftPane.setOpacity(1.0);
            leftPane.setCursor(Cursor.DEFAULT);
            this.leftPane = new StackPane(leftPane);
            this.leftPane.setAlignment(Pos.CENTER_LEFT);
            this.leftPane.getStyleClass().add("left-pane");
            this.leftPane.getStyleClass().add("clearable-field");
            this.leftPane.setPrefWidth(20);
            this.leftPane.setPrefHeight(20);
            getChildren().add(this.leftPane);

            Region rightRegion = new Region();
            rightRegion.getStyleClass().addAll("graphic");
            StackPane rightPane = new StackPane(rightRegion);
            rightPane.getStyleClass().addAll("clear-button", "chart-entry");
            rightPane.setStyle("-fx-background-color: black;");
            rightPane.setOpacity(1.0);
            rightPane.setCursor(Cursor.DEFAULT);
            this.rightPane = new StackPane(rightPane);
            this.rightPane.setAlignment(Pos.CENTER_RIGHT);
            this.rightPane.getStyleClass().add("right-pane");
            this.rightPane.getStyleClass().add("clearable-field");
            this.rightPane.setPrefWidth(20);
            this.rightPane.setPrefHeight(20);
            getChildren().add(this.rightPane);
        }

        @Override protected void layoutChildren(double x, double y, double w, double h) {
            final double fullHeight = h + snappedTopInset() + snappedBottomInset();

            final double leftWidth = leftPane == null ? 0.0 : snapSize(leftPane.prefWidth(fullHeight));
            final double rightWidth = rightPane == null ? 0.0 : snapSize(rightPane.prefWidth(fullHeight));

            final double textFieldStartX = snapPosition(x) + snapSize(leftWidth);
            final double textFieldWidth = w - snapSize(leftWidth) - snapSize(rightWidth);

            super.layoutChildren(textFieldStartX, 0, textFieldWidth, fullHeight);

            if (leftPane != null) {
                final double leftStartX = 0;
                leftPane.resizeRelocate(leftStartX, 0, leftWidth, fullHeight);
            }

            if (rightPane != null) {
                final double rightStartX = rightPane == null ? 0.0 : w - rightWidth + snappedLeftInset();
                rightPane.resizeRelocate(rightStartX, 0, rightWidth, fullHeight);
            }
        }

        @Override
        protected double computePrefWidth(double h, double topInset, double rightInset, double bottomInset, double leftInset) {
            final double pw = super.computePrefWidth(h, topInset, rightInset, bottomInset, leftInset);
            final double leftWidth = leftPane == null ? 0.0 : snapSize(leftPane.prefWidth(h));
            final double rightWidth = rightPane == null ? 0.0 : snapSize(rightPane.prefWidth(h));

            return pw + leftWidth + rightWidth + leftInset + rightInset;
        }

        @Override
        protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
            return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
        }

    }

}
