package snake.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import snake.config.AppConfig;

public class SnakeApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                SnakeApplication.class.getResource("/snake/view/MainView.fxml")
        );
        Scene scene = new Scene(loader.load(), 600, 600);

        stage.setTitle(AppConfig.APP_TITLE);
        stage.setScene(scene);
        stage.setMinWidth(400);
        stage.setMinHeight(400);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
