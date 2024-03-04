package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import commons.Event;
import commons.Participant;
import javafx.scene.control.TextField;

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

    /**
     * Constructs a new instance of ContactDetailsCtrl.
     *
     * @param server   The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public ContactDetailsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

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
        participant.setName(nameField.getText());
        participant.setEmail(emailField.getText());
        participant.setBIC(bicField.getText());
        participant.setIBAN(ibanField.getText());
        mainCtrl.updateParticipant(participant);
        back();
    }

    /**
     * Returns to the event overview scene.
     */
    @FXML
    public void back() {
        mainCtrl.showEventOverview(event);
    }
}

/**
 * imma save this leftover from the previous implementation for later on,
 * when we'll add more UI features for notifying and stuff
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

