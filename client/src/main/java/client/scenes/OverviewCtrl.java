package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import commons.Expense;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller class for the overview scene.
 */
public class OverviewCtrl implements Main.UpdatableUI {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    @FXML
    public Button addExpense;
    @FXML
    public Button home;
    @FXML
    private Tab all;
    private Event event;
    @FXML
    public Button sendInvites;
    @FXML
    public Text participants;
    @FXML
    public Button settleDebts;
    @FXML
    public Text expense;
    @FXML
    public MenuButton langButton;
    @FXML
    public Tab fromSelected;
    @FXML
    public Tab inclSelected;
    @FXML
    private Text title;
    @FXML
    private TextField titleField;
    @FXML
    private ChoiceBox<Participant> participantBox;
    @FXML
    private ListView<Expense> expenseList;
    private ObservableList<Expense> original;
    @FXML
    private FlowPane participantsField;

    /**
     * Constructs an OverviewCtrl object.
     *
     * @param serverUtils The utility class for server interaction.
     * @param mainCtrl    The main controller of the application.
     */
    @Inject
    public OverviewCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the controller.
     */
    public void initialize() {
        expenseList = new ListView<>();
        participantBox = new ChoiceBox<>();
    }

    /**
     * Updates the UI
     */
    @Override
    public void updateUI() {
        home.setText(Main.getLocalizedString("home"));
        addExpense.setText(Main.getLocalizedString("addExpense"));
        sendInvites.setText(Main.getLocalizedString("ovSendInvites"));
        settleDebts.setText(Main.getLocalizedString("ovSettleDebt"));
        expense.setText(Main.getLocalizedString("ovExpense"));
        langButton.setText(Main.getLocalizedString("langButton"));
        fromSelected.setText(Main.getLocalizedString("ovFromSelected"));
        inclSelected.setText(Main.getLocalizedString("ovInclSelected"));
        title.setText(Main.getLocalizedString("OverviewTitle"));
        participants.setText(Main.getLocalizedString("ovParticipants"));
    }

    /**
     * Prepares the display of the title.
     * This method prepares the title display, allowing users to edit the title on
     * double-click.
     */
    public void titlePrepare() {
        if (event.getTitle() != null)
            title.setText(event.getTitle());
        else
            title.setText("Title");
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
     * This method switches the title to edit mode, enabling users to modify the
     * event title.
     */
    private void startEditingTitle() {
        titleField.setText(title.getText());
        title.setVisible(false);
        titleField.setVisible(true);
        titleField.requestFocus();
    }

    /**
     * Updates the title.
     * This method updates the event title with the modified value and updates it on
     * the server.
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
     * This method generates a display for event participants, allowing users to
     * view and interact with them.
     */
    private void participantsDisplay() {
        participantsField.getChildren().clear();
        for (Participant contact : event.getParticipants()) {
            Hyperlink label = new Hyperlink(contact.getName());
            label.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2) {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        serverUtils.deleteParticipant(contact);
                        event.removeParticipant(contact);
                        this.event = serverUtils.updateEvent(this.event);
                        participantsDisplay();
                    } else
                        addParticipant1(contact);
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
                participantExists = true;
                break;
            }
        }
        if (!participantExists) {
            Participant newPart = serverUtils.addParticipant(participant);
            this.event.getParticipants().add(newPart);
        }
        else{
            serverUtils.updateParticipant(participant);
        }
        participant.setEventFollowed(event);
        this.event = serverUtils.updateEvent(event);
        participantsDisplay();
        mainCtrl.showEventOverview(event);
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

    /**
     *
     * @param actionEvent
     */
    public void switchToEnglish(ActionEvent actionEvent) {
        Main.switchLocale("en");
    }

    /**
     *
     * @param actionEvent
     */
    public void switchToDutch(ActionEvent actionEvent) {
        Main.switchLocale("nl");
    }

    /**
     * Directs user back to the startScreen. Here they can join other events if they
     * want to
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
     * Resets the expenses list and then filters it for all expenses paid by the
     * selected
     * participant in the box
     */
    public ListView<Expense> showFromSelected(Event event) {
        original = FXCollections.observableArrayList(); //do I need this?
        List<Expense> temp = new ArrayList<>(event.getExpenses());
        temp = temp.stream().filter(expense -> expense.getPaidBy().equals(participantBox.getValue()))
                .toList();
        original.setAll(temp);
        return new ListView<>(original);
    }

    /**
     * Adds a new participant.
     * This method opens the contact details scene to add a new participant.
     */
    @FXML
    public void addParticipant() {
        mainCtrl.showContactDetails(new Participant(), event);
    }

    /**
     * Resets the expenses list and then filters it for all expenses that involve
     * then selected
     * participant in the box
     */
    public ListView<Expense> showIncludingSelected(Event event){
        original = FXCollections.observableArrayList(); //do I need this?
        List<Expense> temp = new ArrayList<>(event.getExpenses());
        temp = temp.stream().filter(expense -> (expense.getInvolvedParticipants().contains(participantBox.getValue())
                || expense.getPaidBy().equals(participantBox.getValue())))
                        .toList();
        original.setAll(temp);
        return new ListView<>(original);
    }

    public void refresh(Event event) {
        this.event = serverUtils.updateEvent(event);
        titlePrepare();
        participantsDisplay();
        expenseList = new ListView<>();
        original = FXCollections.observableArrayList();
        original.setAll(event.getExpenses());
        expenseList.setItems(original);
        all.setContent(expenseList);
        fromSelected.setContent(showFromSelected(event));
        inclSelected.setContent(showIncludingSelected(event));
    }

    /**
     * switches to the Open Debt scene
     * @param actionEvent
     */
    public void settleDebts(ActionEvent actionEvent) {
        mainCtrl.showOpenDebts(event);
    }
}
