package app;

import javafx.application.Application;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Routes.loadMainView(stage);
        Routes.loadView(1);

        stage.setAlwaysOnTop(true);
        stage.show();
    }

}