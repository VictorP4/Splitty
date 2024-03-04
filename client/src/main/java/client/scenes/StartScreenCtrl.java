package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import commons.Event;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.text.Text;

import static client.Main.switchLocale;

/**
 * Controller class for the start screen scene.
 */
public class StartScreenCtrl implements Main.UpdatableUI {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
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
    private Event event;
    @FXML
    private TextField eventTitle;
    @FXML
    private TextField eventCode;

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
     * Creates a new event
     */
    public void createEvent() { // :)
        this.event = new Event();
        this.event.setTitle(eventTitle.getText());
        try {
            this.event = server.addEvent(event);
            mainCtrl.showEventOverview(event);
        } catch(WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        clearField();
    }

    /**
     * Has a participant join an existing event
     */
    public void joinEvent() {
        try {
            Event fetchedEvent = server.getEvent(Long.decode(eventCode.getText()));
            if(fetchedEvent!=null) mainCtrl.showEventOverview(fetchedEvent);
            else clearField();
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearField();
    }

    private void clearField() {
        eventTitle.clear();
        eventCode.clear();
    }

    /**
     * Shows the overview scene.
     */
    public void showOverview() {
        mainCtrl.showEventOverview(event);
    }

    @FXML

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
}
