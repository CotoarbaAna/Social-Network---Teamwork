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

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class MessagesController implements Initializable {

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
    @FXML
    private TextField msgField;
    @FXML
    private Button SendMsg;
    @FXML
    private TextField BeginDay;
    @FXML
    private TextField BeginMonth;
    @FXML
    private TextField BeginYear;
    @FXML
    private TextField EndDay;
    @FXML
    private TextField EndMonth;
    @FXML
    private TextField EndYear;
    @FXML
    private Button FilterMsg;

    private Service service;

    private String us1;
    private String us2;

    public void fillTable() {
        UserCol.setCellValueFactory(new PropertyValueFactory<MessageDto,String>("username"));
        DataCol.setCellValueFactory(new PropertyValueFactory<MessageDto,LocalDate>("date"));
        Msg.setCellValueFactory(new PropertyValueFactory<MessageDto,String>("message"));

        showMessages.setItems(getMessages());
    }

    public ObservableList<MessageDto> getMessages() {
        setService();
        ObservableList<MessageDto> messages = FXCollections.observableArrayList();
        List<MessageDto> messageDtos = service.findUserMessages(service.findUsername(us1), service.findUsername(us2))
                .stream()
                .map(x -> new MessageDto(service.findUsernameById(x.getFrom()), x.getDate(), x.getMessage()))
                .toList();
        for(MessageDto msg : messageDtos)
            messages.add(new MessageDto(msg.getUsername(), msg.getDate(), msg.getMessage()));
        return messages;
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
    /**
     * Function for getting the message to send to a friend
     */
    public void enterMsg() {
        msgField.setPromptText("Enter message");
        msgField.getText();
    }

    public void refresh() {
        setService();
        fillTable();
    }

    /**
     * Switches between SendMessage and Messages pages
     * @throws Exception
     */
    public void goBack() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SendMessage.fxml"));
        Parent root = loader.load();
        SendMessageController sendMessageController = loader.getController();
        sendMessageController.passUser1(us1);
        Stage stage = (Stage) Back.getScene().getWindow();
        stage.setScene(new Scene(root, 548, 432));
    }

    public void filterMsg() throws Exception {
        int bd = Integer.parseInt(BeginDay.getText());
        int bm = Integer.parseInt(BeginMonth.getText());
        int by = Integer.parseInt(BeginYear.getText());

        LocalDate beginDate = LocalDate.of(by, bm, bd);

        int ed = Integer.parseInt(EndDay.getText());
        int em = Integer.parseInt(EndMonth.getText());
        int ey = Integer.parseInt(EndYear.getText());

        LocalDate endDate = LocalDate.of(ey, em, ed);

        ObservableList<MessageDto> msg = getMessages();
        List<MessageDto> msgList = msg.stream()
                .filter(x -> (beginDate.isBefore(x.getDate()) && endDate.isAfter(x.getDate())))
                .toList();
        ObservableList<MessageDto> filteredMsg = FXCollections.observableArrayList();
        for (MessageDto m : msgList) {
            filteredMsg.add(m);
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FilterMessages.fxml"));
        Parent root = loader.load();
        FilterMessagesController filterMessagesController = loader.getController();
        filterMessagesController.passUser1(us1);
        filterMessagesController.passUser2(us2);
        filterMessagesController.fillTable(filteredMsg);
        Stage stage = (Stage) FilterMsg.getScene().getWindow();
        stage.setScene(new Scene(root, 548, 432));
    }

    /**
     * Sends a message to a friend
     * @throws Exception
     */
    public void sendMsg() throws Exception {
        setService();
        Integer ok = 0;
        if(msgField.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No message entered");
            alert.setContentText("Please try again");
            alert.show();
        }
        else {
            service.addMessage(service.getAvailableMessageId(), service.findUsername(us2), service.findUsername(us1), msgField.getText());
            fillTable();
        }
    }

}
