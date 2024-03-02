package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddExpenseCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    public CheckBox partIn;
    @FXML
    private TextField amount;
    @FXML
    private Spinner<Participant> paidBy;
    @FXML
    private DatePicker date;
    @FXML
    private Spinner<String> currency;
    @FXML
    private TextField title;

    /**
     * Constructs a new instance of a AddExpenseCtrl.
     *
     * @param server   The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * cancels the process of adding a new expense by clearing inout fields and returning to the overview screen
     */
    public void cancel() {
        clearFields();
        mainCtrl.showAddExpense();
    }

    /**
     * clears all input fields related to adding a new expense
     */
    private void clearFields() {
        amount.clear();
        title.clear();
        date.cancelEdit();
        paidBy.getValueFactory().setValue(null);
        partIn.setSelected(false);
        currency.getValueFactory().setValue(" ");
    }

    private Expense getExpense() {
        String title = this.title.getText();
        double amount = Double.parseDouble(this.amount.getText());
        LocalDate localdate = this.date.getValue();
        Date date = Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Participant paidBy = this.paidBy.getValue();
        List<Participant> partIn = new ArrayList<>();
        //List<Participant> partIn  = this.partIn.getSkin();
        return new Expense(title, amount, paidBy, partIn, date);
    }

    /**
     * accepts inputted expense, adds it to the server, returns to the event overview scene
     */
    private void ok() {
        try {
            server.addExpense(getExpense());
        }
        catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFields();
        mainCtrl.showEventOverview();
    }

    /**
     * handles events for adding an expense
     *
     * @param e the key the user pressed
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }
}