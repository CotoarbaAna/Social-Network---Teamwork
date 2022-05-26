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
import lab6.repository.Repository;
import lab6.repository.db.*;
import lab6.service.Service;
import org.w3c.dom.ls.LSOutput;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class FriendReqController implements Initializable {

    @FXML
    private Button Back_fr;
    @FXML
    private TableView TableView;
    @FXML
    private TableColumn<FrReqUsername, String> ColumnUsername;
    @FXML
    private TableColumn<FrReqUsername, RequestStatus> ColumnStatus;
    @FXML
    private TableColumn<FrReqUsername, LocalDate> ColumnDate;

    @FXML
    private TableView TableViewSent;
    @FXML
    private TableColumn<FrReqUsername, String> ColumnUsernameSent;
    @FXML
    private TableColumn<FrReqUsername, RequestStatus> ColumnStatusSent;
    @FXML
    private TableColumn<FrReqUsername, LocalDate> ColumnDateSent;

    @FXML
    private TextField EnterUser;
    @FXML
    private Button Accept;
    @FXML
    private Button Reject;

    @FXML
    private TextField EnterUserSent;
    @FXML
    private Button Cancel;



    private Service service;

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

    String username;
    /**
     * Function which passes the input user to another page
     * @param string : String
     */
    public void passUser1(String string) {
        setService();
        this.username = string;

        ColumnUsername.setCellValueFactory(new PropertyValueFactory<FrReqUsername, String>("username"));
        ColumnStatus.setCellValueFactory(new PropertyValueFactory<FrReqUsername, RequestStatus>("status"));
        ColumnDate.setCellValueFactory(new PropertyValueFactory<FrReqUsername, LocalDate>("data"));

        TableView.setItems(getFR());

        ColumnUsernameSent.setCellValueFactory(new PropertyValueFactory<FrReqUsername, String>("username"));
        ColumnStatusSent.setCellValueFactory(new PropertyValueFactory<FrReqUsername, RequestStatus>("status"));
        ColumnDateSent.setCellValueFactory(new PropertyValueFactory<FrReqUsername, LocalDate>("data"));

        TableViewSent.setItems(getFRSent());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setService();
    }

    private ObservableList<FrReqUsername> getFR() {
        setService();

        ObservableList<FrReqUsername> list = FXCollections.observableArrayList();
        List<FriendRequest> list_p= service.getPendingFriendRequests(service.findUsername(username));
        List<FriendRequest> list_a= service.getApprovedFriendRequests(service.findUsername(username));
        List<FriendRequest> list_r= service.getRejectedFriendRequests(service.findUsername(username));

        for(FriendRequest req : list_p)
            if(req.getId_us2().equals(service.findUsername(username)))
                list.add(new FrReqUsername(service.findUsernameFromUser(req.getId_us1()), req.getStatus(), req.getData()));

        for(FriendRequest req1 : list_a)
            if(req1.getId_us2().equals(service.findUsername(username)))
                list.add(new FrReqUsername(service.findUsernameFromUser(req1.getId_us1()), req1.getStatus(),req1.getData()));

        for(FriendRequest req2 : list_r)
            if(req2.getId_us2().equals(service.findUsername(username)))
                list.add(new FrReqUsername(service.findUsernameFromUser(req2.getId_us1()), req2.getStatus(),req2.getData()));


        return list;
    }
    /**
     * Gets the FriendRequests list as a TableView item
     * @return ObservableList<FrReqUsername>
     */
    private ObservableList<FrReqUsername> getFRSent() {
        setService();

        ObservableList<FrReqUsername> list = FXCollections.observableArrayList();
        List<FriendRequest> list_s= service.getSentFriendRequests(service.findUsername(username));

        for(FriendRequest req : list_s)
            if(req.getId_us1().equals(service.findUsername(username)))
                list.add(new FrReqUsername(service.findUsernameFromUser(req.getId_us2()), req.getStatus(), req.getData()));

        return list;
    }
    /**
     * Switches between Meniu and FriendRequest pages
     * @throws Exception
     */
    public void onBackButtonClicked() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Meniu.fxml"));
        Parent root = loader.load();
        MeniuController meniuController = loader.getController();
        meniuController.passUser(username);
        Stage stage = (Stage) Back_fr.getScene().getWindow();
        stage.setScene(new Scene(root, 548, 432));
    }
    /**
     * Function for getting a username from Friend Requests Sent
     * @param actionEvent
     */
    public void enterUserSent(ActionEvent actionEvent) {
        EnterUserSent.setPromptText("Enter username");
        EnterUserSent.getText();
    }
    /**
     * Cancels a friend request sent
     * @param actionEvent
     */
    public void onCancelButtonClicked(ActionEvent actionEvent) {
        Integer ok = 0;
        if(EnterUserSent.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No username entered");
            alert.setContentText("Please try again");
            alert.show();
        }
        else
        {
            for (Username username1 : this.service.getUsernames())
                if (username1.getUsername().equals(EnterUserSent.getText())) {
                    ObservableList<FrReqUsername> list = getFRSent();
                    for(FrReqUsername fr : list)
                        if(fr.getUsername().equals(EnterUserSent.getText()))
                        {
                            service.deleteSentFriendRequest(service.findUsername(username),service.findUsername(EnterUserSent.getText()));
                            TableViewSent.setItems(getFRSent());
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Friend Request Canceled");
                            alert.setHeaderText("You succesfully canceled the friend request sent");
                            alert.setContentText("Press OK to continue");
                            alert.showAndWait();
                            ok = 1;
                        }
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
     * Function for getting a username from Friend Requests
     * @param actionEvent
     */
    public void enterUser(ActionEvent actionEvent) {
        EnterUser.setPromptText("Enter username");
        EnterUser.getText();
    }
    /**
     * Accepts a friend request
     * @param actionEvent
     */
    public void onAcceptButtonClicked(ActionEvent actionEvent) {
        Integer ok = 0;
        if(EnterUser.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No username entered");
            alert.setContentText("Please try again");
            alert.show();
        }
        else
        {
            for (Username username1 : this.service.getUsernames())
                if (username1.getUsername().equals(EnterUser.getText())) {
                    ObservableList<FrReqUsername> list = getFR();
                    for(FrReqUsername fr : list)
                        if(fr.getUsername().equals(EnterUser.getText()))
                            if(fr.getStatus().equals(RequestStatus.PENDING))
                            {
                                List<FriendRequest> lista = service.getPendingFriendRequests(service.findUsername(username));
                                for(FriendRequest x : lista)
                                    if(x.getId_us2().equals(service.findUsername(username)) && x.getId_us1().equals(service.findUsername(EnterUser.getText())))
                                    {
                                        service.acceptFriendRequest(x.getId());
                                        TableView.setItems(getFR());
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Friend Request Accepted");
                                        alert.setHeaderText("You succesfully accepted the friend request");
                                        alert.setContentText("Press OK to continue");
                                        alert.showAndWait();
                                        ok = 1;}
                            }
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
     * Rejects a friend request
     * @param actionEvent
     */
    public void onRejectButtonClicked(ActionEvent actionEvent) {
        Integer ok = 0;
        if(EnterUser.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No username entered");
            alert.setContentText("Please try again");
            alert.show();
        }
        else
        {
            for (Username username1 : this.service.getUsernames())
                if (username1.getUsername().equals(EnterUser.getText())) {
                    ObservableList<FrReqUsername> list = getFR();
                    for(FrReqUsername fr : list)
                        if(fr.getUsername().equals(EnterUser.getText()))
                            if(fr.getStatus().equals(RequestStatus.PENDING))
                            {
                                List<FriendRequest> lista = service.getPendingFriendRequests(service.findUsername(username));
                                for(FriendRequest x : lista)
                                    if(x.getId_us2().equals(service.findUsername(username)) && x.getId_us1().equals(service.findUsername(EnterUser.getText())))
                                    {
                                        service.rejectFriendRequest(x.getId());
                                        TableView.setItems(getFR());
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Friend Request Rejected");
                                        alert.setHeaderText("You succesfully rejected the friend request");
                                        alert.setContentText("Press OK to continue");
                                        alert.showAndWait();
                                        ok = 1;}
                            }
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
