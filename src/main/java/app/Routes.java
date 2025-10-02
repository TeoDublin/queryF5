package app;

import app.views.viewMain;
import app.views.viewQuery;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class Routes {
    @FXML private static AnchorPane root;
    public static viewMain viewMain;

    private static final List<String> views = List.of(
            "views/viewMain.fxml",
            "views/viewQueries.fxml",
            "views/viewQuery.fxml"
    );


    public static void loadView(int index) throws IOException {
        FXMLLoader loader = new FXMLLoader(Routes.class.getResource(views.get(index)));
        VBox vbox = loader.load();

        AnchorPane anchor = (AnchorPane) root.lookup("#contentPane");
        anchor.getChildren().setAll(vbox);

        AnchorPane.setTopAnchor(vbox, 0.0);
        AnchorPane.setBottomAnchor(vbox, 0.0);
        AnchorPane.setLeftAnchor(vbox, 0.0);
        AnchorPane.setRightAnchor(vbox, 0.0);
    }

    public static  void loadMainView(Stage stage) throws IOException {
        stage.setTitle("queryF5");
        stage.getIcons().add(new Image(
                Objects.requireNonNull(Routes.class.getResourceAsStream("img/logo.png"))
        ));
        FXMLLoader loader = new FXMLLoader(Routes.class.getResource(views.getFirst()));
        root = loader.load();
        viewMain = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public static void dialog(int index) {
        try {
            FXMLLoader loader = new FXMLLoader(Routes.class.getResource(views.get(index)));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setAlwaysOnTop(true);
            dialogStage.setTitle("New Query Watch");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (IOException e) {
            error(e);
        }
    }

    public static void viewQuery(String query, int milliseconds, String uniq) {
        try {
            FXMLLoader loader = new FXMLLoader(Routes.class.getResource(views.get(2)));
            Parent root = loader.load();

            viewQuery controller = loader.getController();
            controller.textArea1.setText(query);
            controller.spinner1.getValueFactory().setValue(milliseconds);
            controller.isEdit  = true;
            controller.uniq = uniq;

            Stage dialogStage = new Stage();
            dialogStage.setAlwaysOnTop(true);
            dialogStage.setTitle("Edit Query");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (IOException e) {
            error(e);
        }
    }


    public static void error(Exception e) {
        viewMain.appendTerminalOutput("error: "+e.toString());
    }

}
