package app.views;

import app.Sql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class viewQuery implements Initializable {
    private Sql sql;
    public TextArea textArea;
    public TableView<ObservableList<String>> tableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textArea.textProperty().addListener((_, _, newText) -> {
            try {
                createRandomTable(newText);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void createRandomTable(String query) throws SQLException {
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
                e.printStackTrace();
            }
        }
        return null;
    }
}
