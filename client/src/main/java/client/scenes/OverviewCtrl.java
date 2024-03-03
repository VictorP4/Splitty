package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

import javafx.collections.FXCollections;
import javafx.scene.control.TextArea;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
/**
 * Controller class for the overview scene.
 */
import java.util.Objects;

public class OverviewCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private Event event;
    @FXML
    private Text title;
    @FXML
    private TextField titleField;
    @FXML
    private ChoiceBox<Participant> participantBox;
    @FXML
    private ListView<Expense> expenseList;
    @FXML
    private ObservableList<Expense> original;
    @FXML
    private FlowPane participantsField;

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
        this.event = serverUtils.updateEvent(e);
    }


    /**
     * Prepares the display of the title.
     * This method prepares the title display, allowing users to edit the title on double-click.
     */
    public void titlePrepare() {
        if (event.getTitle() != null) title.setText(event.getTitle());
        else title.setText("Title");
        titleField.setText(title.getText());
        titleField.setVisible(false);
        titleField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                updateTitle();
            }
        });
        title.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                startEditingTitle();
            }
        });
        titleField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE) {
                updateTitle();
            }
        });
    }

    /**
     * Switches to edit mode for the title.
     * This method switches the title to edit mode, enabling users to modify the event title.
     */
    private void startEditingTitle() {
        titleField.setText(title.getText());
        title.setVisible(false);
        titleField.setVisible(true);
        titleField.requestFocus();
    }

    /**
     * Updates the title.
     * This method updates the event title with the modified value and updates it on the server.
     */
    private void updateTitle() {
        title.setText(titleField.getText());
        title.setVisible(true);
        titleField.setVisible(false);
        event.setTitle(title.getText());
        serverUtils.updateEvent(event);
    }

    /**
     * Generates a display for participants.
     * This method generates a display for event participants, allowing users to view and interact with them.
     */
    private void participantsDisplay() {
        participantsField.getChildren().clear();
        for (Participant contact : event.getParticipants()) {
            Hyperlink label = new Hyperlink(contact.getName());
            label.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2) {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        event.removeParticipant(contact);
                        participantsDisplay();
                    } else addParticipant1(contact);
                }
            });
            participantsField.getChildren().add(label);
        }
    }

    /**
     * Updates participant information.
     * This method updates participant information and refreshes the display.
     *
     * @param participant The participant to be updated.
     */
    public void updateParticipant(Participant participant) {
        boolean participantExists = false;
        for (Participant p : event.getParticipants()) {
            if (Objects.equals(p.getId(), participant.getId())) {
                p.setName(participant.getName());
                p.setEmail(participant.getEmail());
                p.setBIC(participant.getBic());
                p.setIBAN(participant.getIban());
                participantExists = true;
                break;
            }
        }
        if (!participantExists) {
            event.addParticipant(participant);
        }
        System.out.println(event.getParticipants());
        serverUtils.updateEvent(event);
        participantsDisplay();
    }

    /**
     * Opens the contact details for adding a participant.
     * This method opens the contact details scene to add a new participant.
     */
    public void addParticipant1(Participant participant) {
        mainCtrl.showContactDetails(participant, event);
    }

    /**
     * Shows invitations.
     * This method navigates to the invitation scene.
     */
    @FXML
    public void showInvites() {
        mainCtrl.showInvitation(this.event);
    }

    // when initializing new event -> participants is empty (participants.clear())

    /**
     * Directs user back to the startScreen. Here they can join other events if they want to
     */
    public void backToStartScreen() {
        mainCtrl.showStartScreen();
    }

    /**
     * Directs user towards the addExpense scene
     */
    public void toAddExpense() {
        mainCtrl.showAddExpense(event);
    }

    /**
     * Shows all expenses of the event
     */
    public void showAllExpenses() {
        expenseList.setItems(original);
    }

    /**
     * Resets the expenses list and then filters it for all expenses paid by the selected
     * participant in the box
     */
    public void showFromSelected() {
        showAllExpenses();
        expenseList.setItems((ObservableList<Expense>) expenseList.getItems().stream()
                .filter(expense -> expense.getPaidBy().equals(participantBox.getValue())).toList());
    }

    /**
     * Adds a new participant.
     * This method opens the contact details scene to add a new participant.
     */
    @FXML
    public void addParticipant() {
        mainCtrl.showContactDetails(new Participant(),event);
    }


    /**
     * Resets the expenses list and then filters it for all expenses that involve then selected
     * participant in the box
     */
    public void showIncludingSelected() {
        showAllExpenses();
        expenseList.setItems((ObservableList<Expense>) expenseList.getItems().stream()
                .filter(expense -> (expense.getInvolvedParticipants().contains(participantBox.getValue())
                        || expense.getPaidBy().equals(participantBox.getValue())))
                .toList());
    }

    public void refresh(Event event) {
        this.event = event;
        titlePrepare();
        participantsDisplay();
        original = FXCollections.observableArrayList(event.getExpenses());
        expenseList.setItems(original);
    }
}


