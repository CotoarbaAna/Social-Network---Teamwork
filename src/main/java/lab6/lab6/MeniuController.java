package lab6.lab6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lab6.domain.*;
import lab6.domain.validator.FriendshipValidator;
import lab6.domain.validator.UserValidator;
import lab6.repository.Repository;
import lab6.repository.db.*;
import lab6.service.Service;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class MeniuController implements Initializable {

    @FXML
    private TableView showFriends;
    @FXML
    private TableColumn<FriendshipDto,String> NumeCol;
    @FXML
    private TableColumn<FriendshipDto, LocalDate> DataCol;
    @FXML
    private TableColumn<FriendshipDto,String> PrenumeCol;
    @FXML
    private Button Logout;
    @FXML
    private TextField username_field;
    @FXML
    private Button FriendRequest;
    @FXML
    private Button AddFriend;
    @FXML
    private Button SendMessage;
    @FXML
    private Button RemoveFriend;
    @FXML
    private Button ShowEvents;
    @FXML
    private TextField EnterFriend;

    private Service service;

    public void passUser(String string) {
        setService();
        username_field.setText(string);
        NumeCol.setCellValueFactory(new PropertyValueFactory<FriendshipDto,String>("nume"));
        PrenumeCol.setCellValueFactory(new PropertyValueFactory<FriendshipDto,String>("prenume"));
        DataCol.setCellValueFactory(new PropertyValueFactory<FriendshipDto,LocalDate>("date"));

        showFriends.setItems(getFriends());
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setService();

        showFriends.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    /**
     * Returns the list of friends as a TableView item for the given username
     * @return ObservableList<FriendshipDto>
     */
    public ObservableList<FriendshipDto> getFriends() {
        setService();
        ObservableList<FriendshipDto> friends = FXCollections.observableArrayList();
        List<FriendshipDto> friendshipDtos = service.showUserFriends(service.findUsername(username_field.getText()));
        for(FriendshipDto fr : friendshipDtos)
            friends.add(new FriendshipDto(fr.getNume(), fr.getPrenume(), fr.getDate()));
        return friends;
    }
    /**
     * Switches between Login and Meniu pages
     * @throws Exception
     */
    public void Logout() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout");
        alert.setContentText("If you want to logout, press OK");
        if(alert.showAndWait().get() == ButtonType.OK) {
            Stage stage = (Stage) Logout.getScene().getWindow();
            stage.setScene(new Scene(root, 270, 280));
        }
    }
    /**
     * Switches between FriendRequests and Meniu pages
     * @throws Exception
     */
    public void onFriendReqButtonClicked() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FriendRequests.fxml"));
        Parent root = loader.load();
        FriendReqController friendReqController = loader.getController();
        friendReqController.passUser1(username_field.getText());
        Stage stage = (Stage) FriendRequest.getScene().getWindow();
        stage.setScene(new Scene(root, 760, 400));
    }
    /**
     * Switches between Messages and Meniu pages
     * @throws Exception
     */
    public void onSendMessageButtonClicked() throws Exception {
        System.out.println("Send message");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SendMessage.fxml"));
        Parent root = loader.load();
        SendMessageController sendMessageController = loader.getController();
        sendMessageController.passUser1(username_field.getText());
        Stage stage = (Stage) AddFriend.getScene().getWindow();
        stage.setScene(new Scene(root, 600, 400));
    }
    /**
     * Switches between AddFriendReq and Meniu pages
     * @throws Exception
     */
    public void onAddFriendButtonClicked() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddFriendReq.fxml"));
        Parent root = loader.load();
        AddFriendReqController addFriendReqController = loader.getController();
        addFriendReqController.passUser1(username_field.getText());
        Stage stage = (Stage) AddFriend.getScene().getWindow();
        stage.setScene(new Scene(root, 600, 400));
    }

    public void EnterFriend(ActionEvent actionEvent) {
        EnterFriend.setPromptText("Enter username");
        EnterFriend.getText();
    }

    public void onShowEventsButtonClicked() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UserEvents.fxml"));
        Parent root = loader.load();
        UserEventsController userEventsController = loader.getController();
        userEventsController.passUser1(username_field.getText());
        userEventsController.fillTable();
        Stage stage = (Stage) ShowEvents.getScene().getWindow();
        stage.setScene(new Scene(root, 600, 400));
    }
    /**
     * Removes a friend from TableView
     */
    public void OnRemoveFriendClick() {
        ObservableList<FriendshipDto> randSelectate;
        randSelectate = showFriends.getSelectionModel().getSelectedItems();

        for(FriendshipDto fr : randSelectate) {
            User del = service.findUserFromNume(fr.getNume(), fr.getPrenume());
            Long id_us = service.findUsername(username_field.getText());
            Friendship f = service.findFriendship(del.getId(), id_us);
            service.deleteFriend(f.getId());
        }


        showFriends.setItems(getFriends());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Friend Removed");
        alert.setHeaderText("You successfully canceled the friend request sent");
        alert.setContentText("Press OK to continue");
        alert.showAndWait();

    }
}
