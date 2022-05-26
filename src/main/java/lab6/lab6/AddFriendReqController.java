package lab6.lab6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.*;
import lab6.domain.*;
import lab6.domain.validator.FriendshipValidator;
import lab6.domain.validator.UserValidator;
import lab6.domain.validator.ValidationException;
import lab6.repository.Repository;
import lab6.repository.db.*;
import lab6.service.Service;
import org.w3c.dom.ls.LSOutput;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AddFriendReqController implements Initializable {

    @FXML
    private Button Back;
    @FXML
    private TextField sendToField;

    private Service service;

    private String username;

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

    public void passUser1(String string) {
        setService();
        this.username = string;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setService();
    }
    /**
     * Function for getting the username to add as a Friend
     */
    public void enterUser() {
        sendToField.setPromptText("Enter username");
        sendToField.getText();
    }
    /**
     * Switches between Meniu and AddFriendReq pages
     * @throws Exception
     */
    public void goBack() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Meniu.fxml"));
        Parent root = loader.load();
        MeniuController meniuController = loader.getController();
        meniuController.passUser(username);
        Stage stage = (Stage) Back.getScene().getWindow();
        stage.setScene(new Scene(root, 548, 432));
    }
    /**
     * Adds a friend
     * @throws Exception
     */
    public void onAddClicked() throws Exception {
        setService();
        Integer ok = 0;
        if(sendToField.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No username entered");
            alert.setContentText("Please try again");
            alert.show();
        }
        else {
            for (Username user : this.service.getUsernames())
                if (user.getUsername().equals(sendToField.getText())) {
                    ok = 1;
                    Long id_us1 = service.findUsername(username);
                    Long id_us2 = service.findUsername(sendToField.getText());
                    Long fr_id = service.getAvailableFriendRequestId();
                    try {
                        service.sendFriendRequest(fr_id, id_us1, id_us2);
                    }
                    catch (ValidationException ve) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(ve.getMessage());
                        alert.setContentText("Please try again");
                        alert.show();
                        continue;
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Request sent");
                    alert.setHeaderText("You have successfully sent the friend request");
                    alert.setContentText("Press OK to continue");
                    alert.showAndWait();

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
}
