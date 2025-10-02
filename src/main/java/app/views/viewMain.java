package app.views;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class viewMain implements Initializable {

    public VBox terminalPanel;
    public Button btnPauseTerminal;
    public Button btnToggleTerminal;
    public TextArea terminalOutput;
    public AnchorPane rootPane;
    public AnchorPane contentPane;
    public Integer countMsg = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        terminalPanel.setVisible(false);
        terminalPanel.setManaged(false);
        terminalOutput.setVisible(false);
        terminalOutput.setManaged(false);

        btnToggleTerminal.setOnAction(_ -> {
            boolean showing = !terminalPanel.isVisible();
            terminalPanel.setVisible(showing);
            terminalPanel.setManaged(showing);
            terminalOutput.setVisible(showing);
            terminalOutput.setManaged(showing);
            btnToggleTerminal.setText(showing ? "⬆" : "⬇");
        });

    }

    public void appendTerminalOutput(String text){
        if(countMsg > 20){
            terminalOutput.clear();
            countMsg = 0;
        }
        Platform.runLater(()->terminalOutput.appendText(text + "\n"));
        countMsg++;
    }
}
