package app.objs;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.util.concurrent.ScheduledExecutorService;

public class objQueries {
    public int milliseconds;
    public String query;
    public Label lbl;
    public ScheduledExecutorService scheduler;
    public TableView<ObservableList<String>> tableView;
    public objQueries(String query, int milliseconds) {
        this.milliseconds = milliseconds;
        this.query = query;
    }
}
