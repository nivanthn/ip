package biscuit;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Biscuit using FXML.
 */
public class Main extends Application {

    private Biscuit biscuit = new Biscuit();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setBiscuit(biscuit); // inject the Biscuit instance
            stage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load /view/MainWindow.fxml", e);
        }
    }
}
