package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.EmailRequestBody;
import commons.Event;
import java.util.ArrayList;

import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

/**
 * Controller class for the invitation scene.
 */
public class InvitationCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private final Event event;
    @FXML
    private TextArea emailTextArea;
    @FXML
    private Text inviteCodeText;
    private String inviteCode;

    /**
     * Constructs a new instance of InvitationCtrl.
     *
     * @param server   The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     * @param e        The event for which invitations are being sent.
     */
    @Inject
    public InvitationCtrl(ServerUtils server, MainCtrl mainCtrl, Event e) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.event = e;
    }

    /**
     * Initializes the controller.
     */
    public void initialize() {
        setInviteCode();
    }

    /**
     * Sets the invite code in the UI.
     */
    public void setInviteCode() {
        String inviteCode = event.getInviteCode();
        inviteCodeText.setText(inviteCode);
    }

    /**
     * Extracts email addresses from the email text area.
     *
     * @return List of email addresses.
     */
    public ArrayList<String> getEmails() {
        ArrayList<String> emails = new ArrayList<>();
        String[] lines = emailTextArea.getText().split("\n");
        for (String line : lines) {
            String email = line.trim();
            if (!email.isEmpty()) {
                emails.add(email);
            }
        }
        return emails;
    }

    /**
     * Sends invitations to the email addresses specified in the UI.
     */
    @FXML
    public void sendInvites() {
        ArrayList<String> emails = getEmails();
        EmailRequestBody requestBody = new EmailRequestBody(emails, inviteCode);
        Response response = server.sendInvites(requestBody);
        if (response.getStatus() == 200) {
            System.out.println("Invites sent successfully.");
        } else {
            System.out.println("Failed to send invites. Status code: " + response.getStatus());
        }
        mainCtrl.showEventOverview();
    }
}
