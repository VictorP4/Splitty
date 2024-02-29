package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import com.google.inject.Inject;

/**
 * Controller class for the start screen scene.
 */
public class StartScreenCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

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
    public void create() {
        Event event = new Event();
        event.setTitle(eventTitle.getText());

        try {
            server.addEvent(event);
        } catch(WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearField();
        mainCtrl.showEventOverview(); // the right one
    }

    /**
     * Has a participant join an existing event
     */
    public void join() {
        try {
            server.getEvent(Long.decode(eventCode.getText()));
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearField();
        mainCtrl.showEventOverview(); // of the right one
    }

    private void clearField() {
        eventTitle.clear();
        eventCode.clear();
    }

    /**
     * Shows the overview scene.
     */
    public void showOverview() {
        mainCtrl.showEventOverview();
    }
}
