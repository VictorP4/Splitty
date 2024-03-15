package client.scenes;

import client.Main;
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
     * Initialized the start screen and the listview
     */
    public void initialize() {
        // initializing the recent event list and the hyperlink list
        recentlyAccessed = new ListView<>();
        listViewItems = FXCollections.observableArrayList();

        alreadyJoined.setDisable(true);
        eventCode.textProperty().addListener((observable, oldValue, newValue) -> handleTextChange(newValue));
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
            if (lc.getItem() != null) lc.setText(lc.getItem().getTitle());
            lc.setOnMouseClicked(me -> {
                if (lc.getItem() != null) mainCtrl.showEventOverview(lc.getItem());
            });
            return lc;
        });
    }

    /**
     * Creates a new event
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
            mainCtrl.showEventOverview(createdEvent);
            mainCtrl.showContactDetails(creator, createdEvent);
            updateMostRecent(createdEvent);
        } catch (WebApplicationException e) {
            noValidEventError(e.getMessage());
        }
        refresh();
    }

    /**
     * Has a participant join an existing event either through an invite code or the list
     */

    public void joinEvent(ActionEvent event) {
        // checks if one of the hyperlinks was clicked, if not, will take the text from the eventCode
        boolean newMember = !(event.getSource() instanceof Hyperlink) && !alreadyJoined.isSelected();

        String inviteCode = getEventSource(event.getSource());

        try {
            Event fetchedEvent = server.getEventbyInviteCode(inviteCode);
            if(fetchedEvent==null) throw new WebApplicationException("Non-existing event");
            mainCtrl.refreshEventOverview(fetchedEvent);
            if (newMember) {
                Participant joined = new Participant();
                mainCtrl.showContactDetails(joined, fetchedEvent);
            }

            updateMostRecent(fetchedEvent);
        } catch (WebApplicationException e) {
            noValidEventError(e.getMessage());
        }
        refresh();
    }

    /**
<<<<<<< HEAD
=======
     * Checks whether the source of the event is a hyperlink or an invite code and returns the correct code for the event
     *
     * @param eventSource The source that has caused the event to take place (a button press or hyperlink)
     * @return a long which is the invite code for an event
     */
    private String getEventSource(Object eventSource) {
        if (eventSource instanceof Hyperlink clicked) {
            // the link has no event
            if (clicked.getText().isEmpty()) {
                noValidEventError("Event does not exist");
                throw new IllegalArgumentException();
            }
            return recentlyAccessed.get(recentlyViewed.indexOf(clicked)).getInviteCode();
        }

        String eventCodeText = eventCode.getText();
        // check if there is a code at all
        if (eventCodeText.isBlank() || alreadyJoined.isDisable()) {
            noValidEventError("Event does not exist");
            throw new IllegalArgumentException();
        }


            // check if the code is a long and if so, return it
            return eventCodeText;

    }

    /**
>>>>>>> 8c0a942cf334f178aabd08140398c70cab06a725
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
     * Will check whether an invite code has been filled in the inviteCode text area. If it is, the checkbox will be
     * made enables and if not, will stay disabled.
     *
     * @param inputtedCode The text contained in the inviteCode textArea
     */
    private void handleTextChange(String inputtedCode) {
        // Disable CheckBox when text is empty
        alreadyJoined.setDisable(inputtedCode == null || inputtedCode.isBlank()); // Enable CheckBox when text is entered
    }

    /**
     * updates the list with most recent events and updates the hyperlinks
     * 
     * @param event The current event that has either been created or joined by the
     *              user
     */
    private void updateMostRecent(Event event) {
        listViewItems.removeIf(recent -> recent.getId().equals(event.getId()));
        listViewItems.addFirst(event);
        recentlyAccessed.setItems(listViewItems);
//        recentlyAccessed.getItems().removeIf(recent -> recent.getId().equals(event.getId()));
//        recentlyAccessed.getItems().addFirst(event);
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
     * Refreshes the startScreen
     */
    public void refresh() {
        eventTitle.clear();
        eventCode.clear();
        alreadyJoined.setDisable(true);
        alreadyJoined.setSelected(false);
        recentlyAccessed = new ListView<>();
        recentlyAccessed.setItems(listViewItems);
    }
}
