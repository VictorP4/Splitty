package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.EmailRequestBody;
import commons.Event;
import java.util.ArrayList;

import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * Controller class for the invitation scene.
 */
public class InvitationCtrl implements Main.UpdatableUI {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextArea inviteEmails;

    private Event event;
    @FXML
    public Button sendInv;
    @FXML
    public Text title;
    @FXML
    public Text invEmail;
    @FXML
    public Text invCode;
    @FXML
    private TextArea emailTextArea;
    @FXML
    private Text inviteCodeText;
    private String inviteCode;
    @FXML
    public AnchorPane ap;
    @FXML
    private Button overviewButton;

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
        ap.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                backToOverview();
            }
            if(event.isControlDown() && event.getCode()==KeyCode.ENTER){
                sendInvites(); //enter by itself goes to new line in email box
            }
        });

        setInviteCode();
    }

    /**
     * Sets the invite code in the UI.
     */
    public void setInviteCode() {
        //TODO change to getInviteCode after proper backend is available
        inviteCode = String.valueOf(event.getInviteCode());
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
        emailTextArea.clear();
        mainCtrl.showEventOverview(event);
    }

    /**
     * Directs a used back to the event overview scene
     */
    public void backToOverview() {
        clearFields();
        mainCtrl.showEventOverview(event);
    }

    /**
     * Clears the fields on the expense page
     */
    private void clearFields() {
        if (inviteEmails != null) {
            inviteEmails.setText(""); // couldnt use .clear()
        }
    }

    /**
     * Refreshed the invitationCtrl scene by setting the invite code the the one corresponding to the current event.
     *
     * @param event The event we want to send invites for.
     */
    public void refresh(Event event) {
        this.event=event;
        setInviteCode();
    }

    /**
     * Sets the text based on the language chosen by the user.
     */
    @Override
    public void updateUI() {
        overviewButton.setText(Main.getLocalizedString("back"));
        title.setText(Main.getLocalizedString("OverviewTitle"));
        sendInv.setText(Main.getLocalizedString("sendInv"));
        invEmail.setText(Main.getLocalizedString("invEmail"));
        invCode.setText(Main.getLocalizedString("invCode"));
    }
}
