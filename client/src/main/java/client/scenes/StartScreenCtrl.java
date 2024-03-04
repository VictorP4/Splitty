package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import commons.Event;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.text.Text;

import static client.Main.switchLocale;

import java.util.*;

/**
 * Controller class for the start screen scene.
 */
public class StartScreenCtrl implements Main.UpdatableUI {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private List<Event> recentlyAccessed;
    public Text createNewEvent;
    @FXML
    public Text joinEvent;
    @FXML
    public Button createButton;
    @FXML
    public Button joinButton;
    @FXML
    public Text recentViewedEvents;
    @FXML
    public MenuButton langButton;
    private List<Hyperlink> recentlyViewed;
    @FXML
    private TextField eventTitle;
    @FXML
    private TextField eventCode;
    @FXML
    private Hyperlink link1;
    @FXML
    private Hyperlink link2;
    @FXML
    private Hyperlink link3;
    @FXML
    private Hyperlink link4;

    /**
     * Constructs a new instance of StartScreenCtrl.
     *
     * @param server   The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public StartScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize() {
        // initializing the recent event list and the hyperlink list
        recentlyAccessed = new LinkedList<>();
        recentlyViewed = new ArrayList<>();

        // adding the hyperlinks. This makes it easier to update them later
        recentlyViewed.add(link1);
        recentlyViewed.add(link2);
        recentlyViewed.add(link3);
        recentlyViewed.add(link4);
    }

    /**
     * Creates a new event
     */
    public void createEvent() { // :)
        Event createdEvent = new Event();

        if (eventTitle.getText().isEmpty()) {
            noValidEventError("Why no title? (0_0) <-- this is supposed to be mad");
            return;
        }

        createdEvent.setTitle(eventTitle.getText());
        try {
            createdEvent = server.addEvent(createdEvent);
            mainCtrl.showEventOverview(createdEvent);
            updateMostRecent(createdEvent);
        } catch (WebApplicationException e) {
            noValidEventError(e.getMessage());
        }
        clearField();
    }

    /**
     * Has a participant join an existing event either through an invite code or a
     * link
     */
    public void joinEvent(ActionEvent event) {
        // checks if one of the hyperlinks was clicked, if not, will take the text from the eventCode
        Long eventId = null;
        if (event.getSource() instanceof Hyperlink) {
            Hyperlink clicked = (Hyperlink) event.getSource();
            eventId = recentlyAccessed.get(recentlyViewed.indexOf(clicked)).getId();
        }
        else eventId = Long.decode(eventCode.getText());

        try {
            Event fetchedEvent = server.getEvent(eventId);
            System.out.println(fetchedEvent);
            mainCtrl.showEventOverview(fetchedEvent);
            updateMostRecent(fetchedEvent);
        } catch (WebApplicationException e) {
            noValidEventError(e.getMessage());
        }
        clearField();
        // --> did I initialize a database?
    }

    /**
     * Creates an error if the user tries to access an event that isn't valid.
     *
     * @param message the message displayed to the user
     */
    public void noValidEventError(String message) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * updates the list with most recent events and updates the hyperlinks
     * 
     * @param event The current event that has either been created or joined by the
     *              user
     */
    private void updateMostRecent(Event event) {
        if (recentlyAccessed.contains(event)) {
            recentlyAccessed.remove(event);
        }

        recentlyAccessed.addFirst(event);

        if (recentlyAccessed.size() > 4) {
            recentlyAccessed.removeLast();
        }

        // updates the text shown in the most recently viewed list
        for (int i = 0; i < recentlyAccessed.size(); i++) {
            recentlyViewed.get(i).setText(recentlyAccessed.get(i).getTitle());
        }

        if (recentlyAccessed.size() >= 4)
            return;
        // if there were no 4 recently viewed events, it will show nothing
        for (int i = recentlyAccessed.size(); i < recentlyViewed.size(); i++) {
            recentlyViewed.get(i).setText("");
        }
    }

    /**
     * Clears all the text fields.
     */
    private void clearField() {
        eventTitle.clear();
        eventCode.clear();
    }

    /**
     * Shows the overview scene.
     */
    public void showOverview(Event event) {
        mainCtrl.showEventOverview(event);
    }

    @Override
    public void updateUI() {
        createNewEvent.setText(Main.getLocalizedString("NewEvent"));
        joinEvent.setText(Main.getLocalizedString("joinEvent"));
        createButton.setText(Main.getLocalizedString("createEventButton"));
        joinButton.setText(Main.getLocalizedString("joinEventButton"));
        recentViewedEvents.setText(Main.getLocalizedString("recentEvents"));
        langButton.setText(Main.getLocalizedString("langButton"));
    }

    public void switchToDutch(ActionEvent actionEvent) {
        switchLocale("nl");
    }

    public void switchToEnglish(ActionEvent actionEvent) {
        switchLocale("en");
    }

    /**
     * Refreshes the start scene
     */
    public void refresh() {
        // refreshes the most recent event's depending on user -> need to know how user
        // is stored
    }
}
