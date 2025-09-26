package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class Routes {
    @FXML private static AnchorPane root;

    private static final List<String> views = List.of(
            "views/viewMain.fxml",
            "views/viewQuery.fxml"
    );

    private static final HashMap<String, Object> controllers = new HashMap<>();

    public static <T> void loadView(int index) throws IOException {
        FXMLLoader loader = new FXMLLoader(Routes.class.getResource(views.get(index)));
        VBox vbox = loader.load();

        T controller = loader.getController();
        controllers.put(controller.getClass().getName(), controller);

        root.getChildren().setAll(vbox);
        AnchorPane.setTopAnchor(vbox, 0.0);
        AnchorPane.setBottomAnchor(vbox, 0.0);
        AnchorPane.setLeftAnchor(vbox, 0.0);
        AnchorPane.setRightAnchor(vbox, 0.0);

    }

    public static <T> void loadMainView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Routes.class.getResource(views.getFirst()));
        root = loader.load();

        T controller = loader.getController();
        controllers.put(controller.getClass().getName(), controller);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
