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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

public class UserEventsController implements Initializable {

    @FXML
    public TableView showEvents;
    @FXML
    private TableColumn<Event,String> NameCol;
    @FXML
    private TableColumn<Event, LocalDate> DataCol;
    @FXML
    private Button Back;
    @FXML
    private Button AllEvents;

    private Service service;

    private String us1;

    public void fillTable() {
        NameCol.setCellValueFactory(new PropertyValueFactory<Event,String>("name"));
        DataCol.setCellValueFactory(new PropertyValueFactory<Event,LocalDate>("date"));

        showEvents.setItems(getEvents());

        List<Event> events = getEvents().stream()
                .filter(x -> (LocalDate.now().until(x.getDate(), ChronoUnit.DAYS) <= 5 && LocalDate.now().until(x.getDate(), ChronoUnit.DAYS) >= 0))
                .toList();

        if (events.isEmpty())
            return;

        String alertString = "";

        for (Event ev: events) {
            alertString += LocalDate.now().until(ev.getDate(), ChronoUnit.DAYS);
            alertString += " days until " + ev.getName() + "\n";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert!");
        alert.setHeaderText(alertString);
        alert.setContentText("Press OK to continue");
        alert.showAndWait();
    }

    public ObservableList<Event> getEvents() {
        setService();
        ObservableList<Event> ev = FXCollections.observableArrayList();
        List<Event> events = service.getEventsForUser(service.findUsername(us1));
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

    public void showAllEvents() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AllEvents.fxml"));
        Parent root = loader.load();
        AllEventsController allEventsController = loader.getController();
        allEventsController.passUser1(us1);
        allEventsController.fillTable();
        Stage stage = (Stage) AllEvents.getScene().getWindow();
        stage.setScene(new Scene(root, 548, 432));
    }

    public void leaveEvent() {
        ObservableList<Event> randSelectate;
        randSelectate = showEvents.getSelectionModel().getSelectedItems();

        for(Event ev : randSelectate) {
            service.removeEventParticipation(ev.getId(), service.findUsername(us1));
        }


        showEvents.setItems(getEvents());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event Removed");
        alert.setHeaderText("You successfully canceled the event participation");
        alert.setContentText("Press OK to continue");
        alert.showAndWait();
    }

    /**
     * Switches between SendMessage and Messages pages
     * @throws Exception
     */
    public void goBack() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Meniu.fxml"));
        Parent root = loader.load();
        MeniuController meniuController = loader.getController();
        meniuController.passUser(us1);
        Stage stage = (Stage) Back.getScene().getWindow();
        stage.setScene(new Scene(root, 548, 432));
    }

}
