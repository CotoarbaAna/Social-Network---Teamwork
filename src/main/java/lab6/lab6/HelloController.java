package lab6.lab6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import lab6.domain.*;
import lab6.domain.validator.FriendshipValidator;
import lab6.domain.validator.UserValidator;
import lab6.repository.Repository;
import lab6.repository.db.*;
import lab6.service.Service;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class HelloController {

    @FXML
    private Button Login;
    @FXML
    private Button Back;
    @FXML
    private Button Connect;
    @FXML
    private AnchorPane ScenePane;
    @FXML
    public TextField typeUser;



    private Service service;

    Stage stage;

    public void setService() {
        Repository<Long, User> userRepository;
        Repository<Long, Friendship> friendshipRepository;
        Repository<Long, Message> messageRepository;
        Repository<Long, FriendRequest> requestRepository;
        Repository<Long, Username> usernameRepository;
        Repository<Long, Event> eventRepository;
        Repository<Long, EventParticipation> epRepository;

        userRepository = new UserDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres",
                "017778", new UserValidator());
        friendshipRepository = new FriendshipDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres",
                "017778", new FriendshipValidator());
        messageRepository = new MessageDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres",
                "017778");
        requestRepository = new FriendRequestDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres",
                "017778");
        usernameRepository = new UsernameDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres",
                "017778");
        eventRepository = new EventsDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres",
                "017778");
        epRepository = new EventParticipationDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres",
                "017778");


        Network<Long> network = new Network<Long>();

        Service service1 = new Service(userRepository, friendshipRepository, messageRepository, requestRepository, usernameRepository, eventRepository, epRepository, network);

        this.service = service1;

    }
     /**
      * Switches between Principal and Login pages
     * @throws Exception
     */
    public void onLoginButtonClick() throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

        Stage stage = (Stage) Login.getScene().getWindow();
        stage.setScene(new Scene(root, 270, 280));
    }
    /**
     * Switches between Principal and Login pages
     * @throws Exception
     */
    public void goBack() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Principal.fxml"));
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Back");
        alert.setHeaderText("You're about to go back to principal");
        alert.setContentText("If you want to go back, press OK");
        if(alert.showAndWait().get() == ButtonType.OK) {
            Stage stage = (Stage) Back.getScene().getWindow();
            stage.setScene(new Scene(root, 235, 235));
        }
    }
    /**
     * Function for connecting to Meniu with input username
     */
    public void enterUser()  {
        typeUser.setPromptText("Enter username");
        typeUser.getText();
    }

    ObservableList<FriendRequest> observableList;
    /**
     * Verifies if the input username is correct and logs in/ shows error message
     * @throws Exception
     */
    public void onConnectButtonClick() throws Exception {
        setService();
        Integer ok = 0;
        if(typeUser.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No username entered");
            alert.setContentText("Please try again");
            alert.show();


        }
        else {
            for (Username username : this.service.getUsernames())
                if (username.getUsername().equals(typeUser.getText())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Logged in");
                    alert.setHeaderText("You succesfully logged in");
                    alert.setContentText("Press OK to continue");
                    alert.showAndWait();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("Meniu.fxml"));
                    Parent root = loader.load();
                    MeniuController meniuController = loader.getController();
                    meniuController.passUser(typeUser.getText());
                    Stage stage = (Stage) Connect.getScene().getWindow();
                    stage.setScene(new Scene(root, 548, 432));
                    ok = 1;

                }
            if(ok==0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid Username");
                alert.setContentText("Please try again");
                alert.show();
            }
        }
    }

    /**
     * Exits the application
     * @param event
     */
    public void onExitButtonClick(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("You're about to exit");
        alert.setContentText("If you want to exit, press OK");

        if(alert.showAndWait().get() == ButtonType.OK) {

            stage = (Stage) ScenePane.getScene().getWindow();
            System.out.println("Goodbye");
            stage.close();
        }
    }
}