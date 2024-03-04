package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.awt.*;

public class AddExpenseCtrl implements Main.UpdatableUI {
    @FXML
    public Text addEditText;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Text whoPaid;
    @FXML
    public Text whatFor;
    @FXML
    public Text howMuch;
    @FXML
    public Text when;
    @FXML
    public Text howToSplit;
    @FXML
    public CheckBox equally;
    @FXML
    public Text expenseType;
    @FXML
    public Button abort;

    @FXML
    public Button add;
    @FXML
    public Button overviewButton;
    private Event event;

    @FXML
    private CheckBox everybodyIn;
    @FXML
    private CheckBox someIn;
    @FXML
    public VBox box;
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

    @Override
    public void updateUI() {
        overviewButton.setText(Main.getLocalizedString("overviewButton"));
        addEditText.setText(Main.getLocalizedString("AEExpense"));
        whoPaid.setText(Main.getLocalizedString("whoPaid"));
        whatFor.setText(Main.getLocalizedString("whatFor"));
        howMuch.setText(Main.getLocalizedString("howMuch"));
        when.setText(Main.getLocalizedString("when"));
        howToSplit.setText(Main.getLocalizedString("howToSplit"));
        everybodyIn.setText(Main.getLocalizedString("equally"));
        someIn.setText(Main.getLocalizedString("onlySome"));
        expenseType.setText(Main.getLocalizedString("expType"));
        abort.setText(Main.getLocalizedString("abort"));
        add.setText(Main.getLocalizedString("add"));

    }

    /**
     * cancels the process of adding a new expense by clearing inout fields and returning to the overview screen
     */
    public void cancel() {
        clearFields();
        mainCtrl.showEventOverview(event);
    }

    /**
     * clears all input fields related to adding a new expense
     */
    public void clearFields() {
        amount.clear();
        title.clear();
        date.cancelEdit();
        if(paidBy.getValueFactory()!=null) paidBy.getValueFactory().setValue(null);
        everybodyIn.setSelected(false);
        someIn.setSelected(false);
        if(currency.getValueFactory()!=null) currency.getValueFactory().setValue(" ");
    }

    public Expense getExpense() {
        String title = this.title.getText();
        double amount = Double.parseDouble(this.amount.getText());
        LocalDate localdate = this.date.getValue();
        Date date = Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Participant paidBy = this.paidBy.getValue();
        List<Participant> partIn = add();
        return new Expense(title, amount, paidBy, partIn, date);
    }

    /**
     * accepts inputted expense, adds it to the server, returns to the event overview scene
     */
    public void ok() {
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
        mainCtrl.showEventOverview(event);
    }

    /**
     *
     */
    public void refresh(Event event){
        this.event = event;
        for(Participant p : this.event.getParticipants()){
            box.getChildren().add(new CheckBox(p.getName()));
        }
    }

    /**
     * adds the participants involved in an expense to a list
     * @return list of participants in debt in an expense
     */
    public List<Participant> add(){
        List<Participant> in = new ArrayList<>();
        if(everybodyIn.isSelected()){
            in.addAll(event.getParticipants());
        }
        else if (someIn.isSelected()){
            ticked(in);
        }

        return in;
    }

    /**
     * adds all the participants of an expense to teh list in
     * @param in list with all the participants of the expense
     */
    private void ticked(List<Participant> in) {
        List<CheckBox> checkBoxes = new ArrayList<>();

        listOf(checkBoxes);
        for (CheckBox c : checkBoxes){
            if(c.isSelected()){
                String name = c.getText();
                for(Participant p : event.getParticipants()){
                    if (Objects.equals(p.getName(), name)){
                        in.add(p);
                    }
                }
            }
        }
    }

    /**
     * creates a list of all checkboxes, all possible participants to be picked for an expense   
     * @param checkBoxes list of all checkboxes
     */
    private void listOf(List<CheckBox> checkBoxes) {
        for(Node node : box.getChildren()){
            if (node instanceof CheckBox){
                CheckBox checkBox = (CheckBox) node;
                checkBoxes.add(checkBox);
            }
        }
    }

    /**
     * Directs a used back to the event overview scene
     */
    public void backToOverview() {
        clearFields();
        mainCtrl.showEventOverview(event);
    }
}
