package etude.app.fedelini;

import javafx.application.Application;
import javafx.stage.Stage;

public class Fedelini extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Fedelini");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }

    public static void main(String... args) {
        launch(args);
    }
}
