package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
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
public class OverviewCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    @FXML
    private Text participants;
    @FXML
    private ChoiceBox<Participant> participantBox;
    @FXML
    private ListView<Expense> expenseList;
    @FXML
    private ObservableList<Expense> original;

    /**
     * Constructs a new instance of OverviewCtrl.
     *
     * @param serverUtils The utility class for server-related operations.
     * @param mainCtrl    The main controller of the application.
     */
    @Inject
    public OverviewCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Shows the invitation scene.
     */
    public void showInvites() {
        mainCtrl.showInvitation();
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
    public void addExpense() {
        mainCtrl.showAddExpense();
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
}
