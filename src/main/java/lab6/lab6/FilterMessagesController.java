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

public class FilterMessagesController implements Initializable {

    @FXML
    public TableView showMessages;
    @FXML
    private TableColumn<MessageDto,String> UserCol;
    @FXML
    private TableColumn<MessageDto, LocalDate> DataCol;
    @FXML
    private TableColumn<MessageDto,String> Msg;
    @FXML
    private Button Back;

    private Service service;

    private String us1;
    private String us2;

    public void fillTable(ObservableList<MessageDto> msg) {
        UserCol.setCellValueFactory(new PropertyValueFactory<MessageDto,String>("username"));
        DataCol.setCellValueFactory(new PropertyValueFactory<MessageDto,LocalDate>("date"));
        Msg.setCellValueFactory(new PropertyValueFactory<MessageDto,String>("message"));

        showMessages.setItems(msg);
    }

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
     * Function for passing the input username
     * @param string
     */
    public void passUser1(String string) {
        setService();
        this.us1 = string;
    }
    /**
     * Function for passing the username of a friend
     * @param string
     */
    public void passUser2(String string) {
        setService();
        this.us2 = string;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setService();
    }

    public void savePdf() {
        ObservableList<MessageDto> msg = showMessages.getItems();

    }

    /**
     * Switches between SendMessage and Messages pages
     * @throws Exception
     */
    public void goBack() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Messages.fxml"));
        Parent root = loader.load();
        MessagesController messagesController = loader.getController();
        messagesController.passUser1(us1);
        messagesController.passUser2(us2);
        messagesController.fillTable();
        Stage stage = (Stage) Back.getScene().getWindow();
        stage.setScene(new Scene(root, 548, 432));
    }

}

