package app.views;


import app.Queries;
import app.objs.objQueries;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class viewQuery implements Initializable {
    public boolean isEdit = false;
    public String uniq;
    public TextArea textArea1;
    public Spinner<Integer> spinner1;
    public Button btnGo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        spinner1.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60000, 1000, 500)
        );

        btnGo.setOnAction(_ -> {
           String query = textArea1.getText();
           int milliSeconds = spinner1.getValue();
           if(query.isEmpty()||milliSeconds==0){
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("Error");
               alert.setHeaderText(null);
               alert.setContentText("Please enter a query and a milli seconds");
               alert.showAndWait();
           }
           else{
               if(!isEdit){
                   Queries.addQuery(query, milliSeconds);
               }
               else{
                   Queries.queriesList.put(uniq, new objQueries(query,milliSeconds));
               }
               Stage stage = (Stage) btnGo.getScene().getWindow();
               stage.close();
           }

        });
    }

}
