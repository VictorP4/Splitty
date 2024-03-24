package client.scenes;

import client.Main;
import client.UserConfig;
import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

import static client.Main.switchLocale;

/**
 * Controller class for the start screen scene.
 */
public class StartScreenCtrl implements Main.UpdatableUI {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    public Text createNewEvent;
    @FXML
    public Button settingsPage;
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
    @FXML
    private TextField eventTitle;
    @FXML
    private TextField eventCode;
    @FXML
    private CheckBox alreadyJoined;
    @FXML
    private ListView<Event> recentlyAccessed;
    private ObservableList<Event> listViewItems;
    private final UserConfig userConfig = new UserConfig();

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

    /**
     * Initialized the start screen. It sets the cell factory for the listview,
     * allowing it to be populated with events.
     * Furthermore, the method contain keyPress events for ease of use for the user.
     */
    public void initialize() {
        listViewItems = FXCollections.observableArrayList();

        // setting the server to a default.
        server.setSERVER(userConfig.getServerURLConfig());
        alreadyJoined.setDisable(true);

        eventCode.textProperty().addListener((observable, oldValue, newValue) -> {
            alreadyJoined.setDisable(newValue == null || newValue.isBlank());
        });
        settingsPage.setOnAction(event -> {
            mainCtrl.showSettingsPage();
        });
        eventCode.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                joinEvent();
            }
        });
        eventTitle.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createEvent();
            }
        });

        recentlyAccessed.setCellFactory(lv -> {
            ListCell<Event> lc = new ListCell<>();
            lc.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null) {
                    lc.setText(newItem.getTitle());
                } else {
                    lc.setText(null);
                }
            });
            lc.setOnMouseClicked(me -> {
                if (lc.getItem() != null)
                    mainCtrl.showEventOverview(lc.getItem());
            });
            return lc;
        });
    }

    /**
     * Creates a new event. Has checks for if the event does not have a title due to
     * stop back-end throwing errors.
     */
    public void createEvent() { // :)
        Event createdEvent = new Event();

        if (eventTitle.getText().isBlank()) {
            noValidEventError("Why no title? (>_<) <-- this is supposed to be mad");
            return;
        }

        createdEvent.setTitle(eventTitle.getText());
        try {
            createdEvent = server.addEvent(createdEvent);
            Participant creator = new Participant();
            mainCtrl.refreshEventOverview(createdEvent);
            mainCtrl.showContactDetails(creator, createdEvent);
            updateMostRecent(createdEvent);
        } catch (WebApplicationException e) {
            noValidEventError(e.getMessage());
        }
    }

    /**
     * Has a participant join an existing event either through an invite code or
     * through pressing
     * a title in the list. Will throw error in case of the event-code not being a
     * long or the event not existing.
     */
    public void joinEvent() {
        boolean newMember = !alreadyJoined.isSelected();
        try {
            String inviteCode = eventCode.getText().trim();
            if (inviteCode.isBlank()) {
                noValidEventError("This is not a valid event code :(");
                return;
            }
            Event fetchedEvent = server.getEventByInviteCode(inviteCode);
            mainCtrl.refreshEventOverview(fetchedEvent);
            if (newMember) {
                Participant joined = new Participant();
                mainCtrl.showContactDetails(joined, fetchedEvent);
            } else {
                mainCtrl.showEventOverview(fetchedEvent);
            }
            updateMostRecent(fetchedEvent);
        } catch (WebApplicationException e) {
            noValidEventError(e.getMessage());
        }
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
     * updates the list with most recent events.
     * 
     * @param event The current event that has either been created or joined by the
     *              user
     */
    private void updateMostRecent(Event event) {
        listViewItems.removeIf(recent -> recent.getId().equals(event.getId()));
        listViewItems.addFirst(event);
        recentlyAccessed.setItems(listViewItems);
    }

    /**
     * Updates the UI based on the language chosen by the user.
     */
    @Override
    public void updateUI() {
        createNewEvent.setText(Main.getLocalizedString("NewEvent"));
        joinEvent.setText(Main.getLocalizedString("joinEvent"));
        createButton.setText(Main.getLocalizedString("createEventButton"));
        joinButton.setText(Main.getLocalizedString("joinEventButton"));
        recentViewedEvents.setText(Main.getLocalizedString("recentEvents"));
        langButton.setText(Main.getLocalizedString("langButton"));
        settingsPage.setText(Main.getLocalizedString("settings"));
    }

    /**
     * Allows the used to switch to Dutch
     *
     * @param actionEvent The event that caused this method to be called
     */
    public void switchToDutch(ActionEvent actionEvent) {
        switchLocale("nl");
    }

    /**
     * Allows the used to switch to English
     *
     * @param actionEvent The event that caused this method to be called
     */
    public void switchToEnglish(ActionEvent actionEvent) {
        switchLocale("en");
    }

    /**
     * Updates all events in listviewItems to keep up with recent updates.
     */
    private void updateAllEvents() {
        listViewItems.replaceAll(event -> server.getEvent(event.getId()));
    }

    /**
     * Refreshes the startScreen
     */
    public void refresh() {
        eventTitle.clear();
        eventCode.clear();
        alreadyJoined.setDisable(true);
        alreadyJoined.setSelected(false);
        updateAllEvents();
        recentlyAccessed.setItems(listViewItems);
    }

    /**
     * Directs users to the settings page. Here they can fill in a server of their
     * choice & login as an admin
     */
    public void toSettings() {
        mainCtrl.showSettingsPage();
    }
}
