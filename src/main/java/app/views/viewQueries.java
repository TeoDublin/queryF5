package app.views;

import app.Queries;
import app.Routes;
import app.Sql;
import app.objs.objQueries;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class viewQueries implements Initializable {

    public StackPane firstQueryWatch;
    public String firstQueryWatchBaseStyle;
    public VBox root;
    public HBox vboxNewQuery;
    public Button btnNewQuery;
    public Sql sql;

    private final VBox queriesBox = new VBox();
    private final Map<String, objQueries> queryLabels = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        events();
        vboxNewQuery.setVisible(false);
        vboxNewQuery.setManaged(false);
        root.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        schedule();
    }

    private void events() {
        if(firstQueryWatchBaseStyle==null){
            firstQueryWatchBaseStyle = firstQueryWatch.getStyle();
        }

        firstQueryWatch.setOnMouseEntered(_ -> firstQueryWatch.setStyle(firstQueryWatchBaseStyle+"-fx-background-color: #f0f0f0;"));
        firstQueryWatch.setOnMouseExited(_ -> firstQueryWatch.setStyle(firstQueryWatchBaseStyle));
        firstQueryWatch.setOnMouseClicked(_ -> Routes.dialog(2));
    }

    private void schedule() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                scheduler.scheduleAtFixedRate(() -> {
                    Routes.viewMain.appendTerminalOutput("viewQueries.scheduleAtFixedRate idle");
                    Platform.runLater(() -> {
                        for (String uniq : Queries.queriesList.keySet()) {
                            objQueries obj = Queries.queriesList.get(uniq);
                            if (!queryLabels.containsKey(uniq)) {
                                Routes.viewMain.appendTerminalOutput("add "+obj.query);
                                if (firstQueryWatch.isVisible()) {
                                    vboxNewQuery.setVisible(true);
                                    vboxNewQuery.setManaged(true);
                                    firstQueryWatch.setVisible(false);
                                    firstQueryWatch.setManaged(false);
                                    VBox.setVgrow(queriesBox, javafx.scene.layout.Priority.ALWAYS);
                                    queriesBox.setPrefWidth(Double.MAX_VALUE);
                                    queriesBox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
                                    queriesBox.setStyle("-fx-background-color: #f9f9f9; -fx-padding: 10;");
                                    root.getChildren().add(root.getChildren().indexOf(vboxNewQuery), queriesBox);
                                    btnNewQuery.setOnMouseClicked(_ -> Routes.dialog(2));
                                }
                                TableView<ObservableList<String>>  tbl = new TableView<>();
                                Label lbl = new Label(obj.query);
                                lbl.setOnMouseClicked(_->Routes.viewQuery(obj.query,obj.milliseconds,uniq));
                                lbl.setMaxWidth(Double.MAX_VALUE);
                                obj.lbl = lbl;
                                queriesBox.getChildren().add(lbl);
                                queriesBox.getChildren().add(tbl);
                                obj.tableView = tbl;
                                obj.scheduler = scheduleRefresh(tbl,obj.query,obj.milliseconds);
                                queryLabels.put(uniq, obj);
                            }
                            else{
                                objQueries objLbl = queryLabels.get(uniq);
                                if( !objLbl.query.equals(obj.query) || objLbl.milliseconds != obj.milliseconds ){
                                    objLbl.lbl.setText(obj.query);
                                    objLbl.query = obj.query;
                                    objLbl.milliseconds = obj.milliseconds;
                                    objLbl.scheduler.shutdown();
                                    objLbl.scheduler = scheduleRefresh(objLbl.tableView,obj.query,obj.milliseconds);
                                }
                            }
                        }
                    });
                }, 0, 500, TimeUnit.MILLISECONDS);
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void createTable(TableView<ObservableList<String>>  tableView, String query) throws SQLException {
        tableView.getColumns().clear();
        tableView.getItems().clear();

        Sql sql = _sql();
        assert sql != null;

        ResultSet rs = sql.select(query);

        int colCount = rs.getMetaData().getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(rs.getMetaData().getColumnName(i));
            int finalCIndex = i - 1;
            col.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get(finalCIndex)));
            tableView.getColumns().add(col);
        }

        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.add(rs.getString(i));
            }
            tableView.getItems().add(row);
        }

    }

    private ScheduledExecutorService scheduleRefresh(TableView<ObservableList<String>> tableview,String query,int milliseconds) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (query != null) {
                    javafx.application.Platform.runLater(() -> {
                        try {
                            createTable(tableview, query);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            } catch (Exception e) {
                Routes.error(e);
            }
        }, 0, milliseconds, TimeUnit.MILLISECONDS);
        return scheduler;
    }

    private Sql _sql() {
        if(sql != null){
            return sql;
        }
        else{
            try{
                Sql newSql = new Sql();
                sql  = newSql;
                return newSql;
            }
            catch(Exception e){
                Routes.error(e);
            }
        }
        return null;
    }
}
