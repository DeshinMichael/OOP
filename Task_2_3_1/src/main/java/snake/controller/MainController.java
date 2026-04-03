package snake.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public final class MainController {

    @FXML
    private StackPane gameHost;

    @FXML
    private void initialize() {
    }

    public StackPane getGameHost() {
        return gameHost;
    }
}
