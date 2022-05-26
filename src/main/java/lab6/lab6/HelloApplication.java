package lab6.lab6;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import lab6.domain.*;
import lab6.domain.validator.FriendshipValidator;
import lab6.domain.validator.UserValidator;
import lab6.repository.Repository;
import lab6.repository.db.*;
import lab6.service.Service;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Principal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 235, 235);
        stage.setTitle("SocialNetwork");
        stage.setScene(scene);
        stage.show();


        stage.setOnCloseRequest(event -> {
            event.consume();
            Exit(stage);
        });
    }

    /**
     * METODA DE EXIT DE LA BUTONUL ROSU
     * @param stage: Stage
     */
    public void Exit(Stage stage) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("You're about to exit");
        alert.setContentText("If you want to exit, press OK");

        if(alert.showAndWait().get() == ButtonType.OK) {


            System.out.println("Goodbye");
            stage.close();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}