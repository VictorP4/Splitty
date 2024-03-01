package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the overview scene.
 */
public class OverviewCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Event event;
    @FXML
    private Text title;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea participantsField;

    /**
     * Constructs an OverviewCtrl object.
     *
     * @param serverUtils The utility class for server interaction.
     * @param mainCtrl    The main controller of the application.
     * @param e           The event being overviewed.
     */
    @Inject
    public OverviewCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, Event e) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.event = e;
    }

    /**
     * Initializes the controller.
     */
    public void initialize() {
        titlePrepare();
        participantsPrepare();
    }

    /**
     * Prepares the display of participants.
     */
    public void participantsPrepare(){
        participantsField.setText(participantsDisplay(event.getParticipants()));

        // Set up event handling for switching between view and edit modes
        participantsField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                updateParticipants();
            }
        });

        // Key pressed event handler to switch to view mode when Enter is pressed
        participantsField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                updateParticipants();
            }
        });
    }

    /**
     * Prepares the display of the title.
     */
    public void titlePrepare(){
        if(event.getTitle()!=null) title.setText(event.getTitle());
        else title.setText("Title");
        titleField.setText(title.getText());

        // Initially hide the TextField
        titleField.setVisible(false);

        // Set up event handling for switching between view and edit modes
        titleField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // Focus lost
                updateTitle();
            }
        });

        // Double-click event handler to switch to edit mode
        title.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                startEditingTitle();
            }
        });

        // Key pressed event handler to switch to view mode when Enter is pressed
        titleField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE) {
                updateTitle();
            }
        });
    }

    /**
     * Switches to edit mode for the title.
     */
    private void startEditingTitle() {
        titleField.setText(title.getText());
        title.setVisible(false);
        titleField.setVisible(true);
        titleField.requestFocus();
    }

    /**
     * Updates the title.
     */
    private void updateTitle() {
        title.setText(titleField.getText());
        title.setVisible(true);
        titleField.setVisible(false);
        event.setTitle(title.getText());
        serverUtils.updateEvent(event);
    }

    /**
     * Updates the participants.
     */
    private void updateParticipants() {
        event.setParticipants(getParticipants(participantsField.getText()));
        participantsField.setText(participantsDisplay(event.getParticipants()));
        serverUtils.updateEvent(event);
    }

    /**
     * Generates a display string for participants.
     */
    private String participantsDisplay(List<Participant>a){
        StringBuilder b= new StringBuilder();
        for(Participant p:a)
            b.append(p.getEmail()).append(", ");
        List<String> c=parseEmails(b.toString());
        b= new StringBuilder();
        int l=c.size();
        if (l > 0) {
            b.append(c.getFirst());
            for (int i = 1; i < l; i++) {
                b.append(", ").append(c.get(i));
            }
        }
        return b.toString();
    }

    /**
     * Parses email addresses from a string.
     */
    private List<Participant> getParticipants(String a){
        List<String> l = parseEmails(a);
        List<String> contained = new ArrayList<>();
        List<String> newStr=new ArrayList<>();
        List<String> evPar=new ArrayList<>();
        List<Participant> participants1=event.getParticipants();
        for(Participant p: participants1){
            evPar.add(p.getEmail());
        }
        for(String str: l){
            if(evPar.contains(str))contained.add(str);
            else newStr.add(str);
        }
        for (Participant participant : participants1) {
            // Check if participant email is not contained in the list
            if (!contained.contains(participant.getEmail())) {
                participants1.remove(participant);
            }
        }
        for(String papi: newStr){
            participants1.add(new Participant("",papi));
        }
        return participants1;
    }

    /**
     * Parses email addresses from a string.
     */
    public static List<String> parseEmails(String emailsString) {
        List<String> validEmails = new ArrayList<>();

        // Split the base string into individual email addresses
        String[] emailsArray = emailsString.split("[,\\s]+");

        for (String email : emailsArray) {
            if (isValidEmail(email)) {
                validEmails.add(email);
            } else {
                System.out.println("Invalid email format: " + email);
            }
        }

        return validEmails;
    }

    /**
     * Checks if an email address is valid.
     */
    public static boolean isValidEmail(String email) {
        // Basic email validation: ensure it contains one '@' character
        if (!email.contains("@")) {
            return false;
        }

        // Split the email by '@' to separate the local part and domain part
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }

        String localPart = parts[0];
        String domainPart = parts[1];

        // Check if localPart contains any invalid characters
        for (char c : localPart.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isDigit(c) && c != '.' && c != '_') {
                return false;
            }
        }

        // Check if domainPart contains at least one dot
        if (!domainPart.contains(".")) {
            return false;
        }

        // Split the domain part by '.' to get the segments
        String[] segments = domainPart.split("\\.");
        if (segments.length < 2) {
            return false;
        }

        // Check if each segment contains only letters or digits
        for (String segment : segments) {
            for (char c : segment.toCharArray()) {
                if (!Character.isLetter(c) && !Character.isDigit(c)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Shows invitations.
     */
    @FXML
    public void showInvites() {
        mainCtrl.showInvitation();
    }
}
