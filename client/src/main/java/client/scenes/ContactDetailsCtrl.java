package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import commons.Event;
import commons.Participant;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import java.util.Objects;

public class ContactDetailsCtrl implements Main.UpdatableUI {
/**
 * Controller class for the contact details scene.
 * This class controls the behavior of the contact details scene, allowing users to view and edit
 * the details of a participant.
 */

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Text aeParticipant;
    @FXML
    public Text name;
    @FXML
    public Text email;
    @FXML
    public Button abort;
    @FXML
    public Button okButton;
    private Participant participant;
    private Event event;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField ibanField;

    @FXML
    private TextField bicField;

    private WebSocketUtils webSocket;

    @FXML
    public AnchorPane ap;


    /**
     * Constructs a new instance of ContactDetailsCtrl.
     *
     * @param server   The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public ContactDetailsCtrl(ServerUtils server, MainCtrl mainCtrl, WebSocketUtils webSocket) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.webSocket=webSocket;
    }

    /**
     * initializes the Contact Details Controller
     *
     */
    public void initialize(){
        ap.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                back();
            }
            else if (event.getCode() == KeyCode.ENTER) {
                save();
            }
        });
        webSocket.addParticipantListener((participant)->{
            if(this.participant==null||!Objects.equals(participant.getId(),this.participant.getId())) return;
            else{
                Platform.runLater(()->{
                    back();
                    var alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setContentText("The participant was deleted by another user.");
                    alert.showAndWait();
                });
            }
        });

        setInstructions();
    }


    /**
     * Changes the language of the scene
      */
    @Override
    public void updateUI() {
        aeParticipant.setText(Main.getLocalizedString("AEParticipant"));
        name.setText(Main.getLocalizedString("cdName"));
        email.setText(Main.getLocalizedString("email"));
        abort.setText(Main.getLocalizedString("abort"));
        okButton.setText(Main.getLocalizedString("ok"));
    }

    /**
     * Initializes the contact details scene with participant information.
     *
     * @param participant The participant whose details are to be displayed.
     */
    public void refresh(Participant participant, Event event) {
        this.participant = participant;
        this.event=event;
        if (participant.getName() != null) nameField.setText(participant.getName());
        else nameField.clear();
        if (participant.getEmail() != null) emailField.setText(participant.getEmail());
        else emailField.clear();
        if (participant.getBic() != null) bicField.setText(participant.getBic());
        else bicField.clear();
        if (participant.getIban() != null) ibanField.setText(participant.getIban());
        else ibanField.clear();
    }

    /**
     * Saves the modified participant details and updates the main controller.
     */
    @FXML
    public void save() {
        if(nameField.getText().isEmpty()){
            errorPopup("Invalid name");
            return;
        }
        participant.setName(nameField.getText());
        participant.setEmail(emailField.getText());
        participant.setBIC(bicField.getText());
        participant.setIBAN(ibanField.getText());
        mainCtrl.updateParticipant(participant);
        this.participant=null;
    }

    /**
     * Returns to the event overview scene.
     */
    @FXML
    public void back() {
        this.participant=null;
        mainCtrl.showEventOverview(event);
    }

    /**
     * creates error for invalid action
     * @param message
     */
    private void errorPopup(String message) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets the instructions popups for shortcuts.
     */
    public void setInstructions(){
        mainCtrl.instructionsPopup(new Label(" press ENTER to add participant"), this.okButton);
        mainCtrl.instructionsPopup(new Label(" press ESC to discard changes "), this.abort);
    }

}

/**
 * imma save this leftover from the previous addParticipant implementation for later on,
 * when we'll add more UI features for bad input notifying and stuff
 * /**
 *      * Validates email format.
 *      * This method checks whether the provided email address is valid.
 *      *
 *      * @param email The email address to validate.
 *      * @return True if the email is valid, false otherwise.
 *
 * public static boolean isValidEmail(String email) {
 *         // Basic email validation: ensure it contains one '@' character
 *         if (!email.contains("@")) {
 *             return false;
 *         }
 *
 *         // Split the email by '@' to separate the local part and domain part
 *         String[] parts = email.split("@");
 *         if (parts.length != 2) {
 *             return false;
 *         }
 *
 *         String localPart = parts[0];
 *         String domainPart = parts[1];
 *
 *         // Check if localPart contains any invalid characters
 *         for (char c : localPart.toCharArray()) {
 *             if (!Character.isLetter(c) && !Character.isDigit(c) && c != '.' && c != '_') {
 *                 return false;
 *             }
 *         }
 *
 *         // Check if domainPart contains at least one dot
 *         if (!domainPart.contains(".")) {
 *             return false;
 *         }
 *
 *         // Split the domain part by '.' to get the segments
 *         String[] segments = domainPart.split("\\.");
 *         if (segments.length < 2) {
 *             return false;
 *         }
 *
 *         // Check if each segment contains only letters or digits
 *         for (String segment : segments) {
 *             for (char c : segment.toCharArray()) {
 *                 if (!Character.isLetter(c) && !Character.isDigit(c)) {
 *                     return false;
 *                 }
 *             }
 *         }
 *
 *         return true;
 *     }
 */

