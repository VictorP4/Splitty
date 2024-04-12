package client.scenes;

import client.Main;
import client.UserConfig;
import client.utils.EmailUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.EmailRequestBody;
import commons.Event;
import java.util.ArrayList;

import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
     * @param server     The utility class for server-related operations.
     * @param mainCtrl   The main controller of the application.
     * @param e          The event for which invitations are being sent.
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
        setInstructions();
        buttonSetup();

    }

    /**
     * Sets the invite code in the UI.
     */
    public void setInviteCode() {
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
        UserConfig userConfig = mainCtrl.getUserConfig();

        ArrayList<String> emails = getEmails();
        EmailRequestBody requestBody = new EmailRequestBody(emails, inviteCode);
        EmailUtils emailUtils = new EmailUtils();
        emailUtils.sendInvites(requestBody.getEmailAddresses(), requestBody.getCode(), mainCtrl.getUserConfig().getUserEmail(), mainCtrl.getUserConfig().getUserPass(), mainCtrl.getUserConfig().getServerURLConfig());
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
        title.setText(this.event.getTitle());
        setInviteCode();
        UserConfig userConfig = mainCtrl.getUserConfig();
        if(userConfig.getUserEmail().isBlank()||userConfig.getUserPass().isBlank()){
            sendInv.setDisable(true);
        }
    }

    /**
     * Sets the text based on the language chosen by the user.
     */
    @Override
    public void updateUI() {
        overviewButton.setText(Main.getLocalizedString("back"));
        sendInv.setText(Main.getLocalizedString("sendInv"));
        invEmail.setText(Main.getLocalizedString("invEmail"));
        invCode.setText(Main.getLocalizedString("invCode"));
    }

    /**
     * Sets the instruction popups for shortcuts
     */
    public void setInstructions(){
        mainCtrl.instructionsPopup(new Label(" press ESC to go \n back to event overview "), this.overviewButton);
        mainCtrl.instructionsPopup(new Label(" press CTRL + ENTER \n to send invite emails "), this.sendInv);
    }

    /**
     * Sets the focus look for the buttons
     */
    public void buttonSetup(){
        mainCtrl.buttonFocus(this.overviewButton);
        mainCtrl.buttonFocus(this.sendInv);
    }
}
