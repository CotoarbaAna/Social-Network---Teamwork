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

public class AllEventsController implements Initializable {

    @FXML
    public TableView showEvents;
    @FXML
    private TableColumn<Event,String> NameCol;
    @FXML
    private TableColumn<Event, LocalDate> DataCol;
    @FXML
    private Button Back;
    @FXML
    private Button Join;
    @FXML
    private Button AddEvent;
    @FXML
    private TextField EventName;
    @FXML
    private TextField BeginDay;
    @FXML
    private TextField BeginMonth;
    @FXML
    private TextField BeginYear;

    private Service service;

    private String us1;

    public void fillTable() {
        NameCol.setCellValueFactory(new PropertyValueFactory<Event,String>("name"));
        DataCol.setCellValueFactory(new PropertyValueFactory<Event,LocalDate>("date"));

        showEvents.setItems(getEvents());
    }

    public ObservableList<Event> getEvents() {
        setService();
        ObservableList<Event> ev = FXCollections.observableArrayList();
        List<Event> events = service.getEvents();
        for(Event e : events)
            ev.add(new Event(e.getId(), e.getName(), e.getDate()));
        return ev;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setService();
    }

    public void addEvent() {
        int bd = Integer.parseInt(BeginDay.getText());
        int bm = Integer.parseInt(BeginMonth.getText());
        int by = Integer.parseInt(BeginYear.getText());

        LocalDate eventDate = LocalDate.of(by, bm, bd);

        String evName = EventName.getText();

        service.addEvent(service.getAvailableEventId(), evName, eventDate);

        showEvents.setItems(getEvents());
    }

    public void joinEvent() {
        ObservableList<Event> randSelectate;
        randSelectate = showEvents.getSelectionModel().getSelectedItems();

        for(Event ev : randSelectate) {
            try {
                service.addEventParticipation(service.getAvailableEventParticipationId(), ev.getId(), service.findUsername(us1));

                showEvents.setItems(getEvents());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Event Joined");
                alert.setHeaderText("You successfully joined the event");
                alert.setContentText("Press OK to continue");
                alert.showAndWait();
            }
            catch (ValidationException ex) {
                showEvents.setItems(getEvents());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Event Removed");
                alert.setHeaderText(ex.getMessage());
                alert.setContentText("Press OK to continue");
                alert.showAndWait();
            }
        }
    }

    /**
     * Switches between SendMessage and Messages pages
     * @throws Exception
     */
    public void goBack() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UserEvents.fxml"));
        Parent root = loader.load();
        UserEventsController userEventsController = loader.getController();
        userEventsController.passUser1(us1);
        userEventsController.fillTable();
        Stage stage = (Stage) Back.getScene().getWindow();
        stage.setScene(new Scene(root, 548, 432));
    }

}
