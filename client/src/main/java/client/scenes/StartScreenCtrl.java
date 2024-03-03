package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Controller class for the start screen scene.
 */
public class StartScreenCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private List<Event> recentlyAccessed;
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
        recentlyAccessed = new ArrayList<>();
        recentlyViewed = new ArrayList<>();

        // adding the hyperlinks. This makes it easier to update them later
        recentlyViewed.add(link4);
        recentlyViewed.add(link3);
        recentlyViewed.add(link2);
        recentlyViewed.add(link1);
    }

    /**
     * Creates a new event
     */
    public void createEvent() { // :)
        Event createdEvent = new Event();
        createdEvent.setTitle(eventTitle.getText());
        updateMostRecent(createdEvent);
        try {
            createdEvent = server.addEvent(createdEvent);
            mainCtrl.showEventOverview(createdEvent);
        } catch(WebApplicationException e) {
            noValidEventError(e.getMessage());
        }
        clearField();
    }

    /**
     * Has a participant join an existing event either through an invite code or a link
     */
    public void joinEvent() {
        // checks if one of the hyperlinks was clicked, if not, will take the text from the eventCode
        long eventId;
        if (link1.isPressed()) eventId = recentlyAccessed.get(0).getId();
        else if (link2.isPressed()) eventId = recentlyAccessed.get(1).getId();
        else if (link3.isPressed()) eventId = recentlyAccessed.get(2).getId();
        else if (link4.isPressed()) eventId = recentlyAccessed.get(3).getId();
        else eventId = Long.decode(eventCode.getText());

        try {
            Event fetchedEvent = server.getEvent(eventId);
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
     * @param event The current event that has either been created or joined by the user
     */
    private void updateMostRecent(Event event) {
        recentlyAccessed.add(event);
        if (recentlyAccessed.size() > 4) {
            recentlyAccessed.removeFirst();
        }

        // updates the text shown in the most recently viewed list
        for (int i = recentlyAccessed.size(); i > 0; i--) {
            recentlyViewed.get(i).setText(recentlyAccessed.get(i).getTitle());
        }

        // if there were no 4 recently viewed events, it will
        if (recentlyAccessed.size() < 4) {
            int linksLeft = recentlyViewed.size() - recentlyAccessed.size();
            for (int i = linksLeft; i > 0 ; i--) {
                recentlyViewed.get(i).setText("");
            }
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

    /**
     * Refreshes the start scene
     */
    public void refresh() {
        // refreshes the most recent event's depending on user -> need to know how user is stored
    }
}
