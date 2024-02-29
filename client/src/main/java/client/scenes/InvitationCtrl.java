package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;

import java.awt.*;

public class InvitationCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextArea inviteEmails;

    /**
     * Constructs a new instance of an InivitationCtrl.
     *
     * @param server The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public InvitationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Directs a used back to the event overview scene
     */
    public void backToOverview() {
        clearFields();
        mainCtrl.showEventOverview();
    }

    /**
     * Clears the fields on the expense page
     */
    private void clearFields() {
        if (inviteEmails != null) {
            inviteEmails.setText(""); // couldnt use .clear()
        }
    }
}
