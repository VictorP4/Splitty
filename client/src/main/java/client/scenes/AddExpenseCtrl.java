package client.scenes;

import client.Main;
import client.UserConfig;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import com.google.inject.Inject;
import commons.Tag;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.time.LocalDate;
import java.time.ZoneId;

import java.util.*;
import java.util.stream.Collectors;

public class AddExpenseCtrl implements Main.UpdatableUI {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public AnchorPane anchor;
    private Event event;
    private Tag selectedTag;
    @FXML
    public Text addEditText;
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
    public Text expenseType;
    @FXML
    public Button abort;
    @FXML
    public Button add;
    @FXML
    private CheckBox everybodyIn;
    @FXML
    private CheckBox someIn;
    @FXML
    public VBox box;
    @FXML
    private TextField amount;
    @FXML
    private ComboBox<Participant> paidBy;
    @FXML
    private DatePicker date;
    @FXML
    private ComboBox<String> currency;
    @FXML
    private TextField title;
    @FXML
    private MenuButton tagMenu;
    @FXML
    public AnchorPane ap;
    @FXML
    private Button undo;
    private Expense expense;
    private WebSocketUtils webSocket;
    private final UserConfig userConfig = new UserConfig();

    /**
     * Constructs a new instance of a AddExpenseCtrl.
     *
     * @param server   The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl, WebSocketUtils webSocket) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.webSocket = webSocket;
    }

    /**
     * initializes the Add Expense Controller
     */
    public void initialize() {
        anchor.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                mainCtrl.showEventOverview(event);
            }
            if (keyEvent.getCode() == KeyCode.ENTER) {
                ok();
            }
            if(keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.U){
                undo();
            }
        });
        webSocket.addExpenseListener((expense -> {
            if (this.expense == null || !Objects.equals(expense.getId(), this.expense.getId()))
                return;
            else {
                Platform.runLater(() -> {
                    cancel();
                    errorPopup("The expense was deleted by another user.");
                });
            }
        }));
        webSocket.addEventListener((event) -> {
            if (this.event == null || !Objects.equals(this.event.getId(), event.getId()))
                return;
            else {
                Platform.runLater(() -> {
                    refresh(event);
                });
            }
        });

        setInstructions();
    }

    /**
     * Updates the UI based on the language chosen by the user.
     */
    @Override
    public void updateUI() {
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
        undo.setText(Main.getLocalizedString("Undo"));
    }

    /**
     * cancels the process of adding a new expense by clearing inout fields and
     * returning to the overview screen
     */
    public void cancel() {
        mainCtrl.showEventOverview(event);
        this.expense=null;
        this.event = null;
    }

    /**
     * clears all input fields related to adding a new expense
     */
    public void clearFields() {
        amount.clear();
        title.clear();
        date.setValue(null); // TODO: make it give a default
        paidBy.getSelectionModel().clearSelection();
        tagMenu.setText("Select Tag");

        everybodyIn.setSelected(false);
        someIn.setSelected(false);
        paidBy.getItems().removeAll(paidBy.getItems());
        currency.getItems().removeAll(currency.getItems());
        box.getChildren().removeAll(box.getChildren());
        selectedTag = null;
    }

    /**
     * creates an expense based on the input
     * 
     * @return new expense
     */
    public Expense getExpense() {
        String title = this.title.getText();
        double amount = Double.parseDouble(this.amount.getText());
        LocalDate localdate = this.date.getValue();
        Date date = Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Participant paidBy = this.paidBy.getSelectionModel().getSelectedItem();
        List<Participant> partIn = add();
        Tag tag = selectedTag;
        String selectedCurrency = currency.getValue();

        return new Expense(title, amount, paidBy, partIn, date, selectedTag, selectedCurrency);
    }

    /**
     * accepts inputted expense, adds it to the server, returns to the event
     * overview scene
     */
    public void ok() {
        Expense addExp = null;

        // Checks if all mandatory boxes have been filled in
        try {
            addExp = getExpense();

        } catch (NumberFormatException e) {
            errorPopup("Invalid amount");
            return;
        } catch (Exception e) {
            errorPopup("Missing title or date");
            return;
        }
        if (addExp.getPaidBy() == null) {
            errorPopup("No paid by found.");
            return;
        }
        if (addExp.getAmount() < 0) {
            errorPopup("Invalid amount.");
            return;
        }
        if (addExp.getInvolvedParticipants().equals(new ArrayList<>())) {
            errorPopup("No involved participants selected.");
            return;
        }

        // Checks for any other related errors
        try {
            userConfig.setCurrencyConfig(currency.getValue());
            if (this.expense != null) {
                addExp.setId(this.expense.getId());
            }
            List<Long> ids = new ArrayList<>();
            for (Expense e : event.getExpenses()) {
                ids.add(e.getId());
            }
            if (ids.contains(addExp.getId())) {
                updateExp(addExp);
                Expense newExp = server.updateExpense(event.getId(), addExp);
                mainCtrl.addPrevExp(newExp);
            } else {
                Expense newExp = server.addExpense(addExp, event.getId());
                mainCtrl.addPrevExp(newExp);
            }
            event = server.getEvent(event.getId());
        } catch (WebApplicationException e) {
            errorPopup(e.getMessage());
            return;
        }
        clearFields();
        this.expense = null;
        mainCtrl.showEventOverview(event);

    }

    /**
     * A general method to create a popup error on the application, with custom
     * message.
     *
     * @param message The message passed in.
     */
    private void errorPopup(String message) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Refreshes the AddExpense page corresponding with the new event.
     */
    public void refresh(Event event) {
        this.event = event;
        addEditText.setText(Main.getLocalizedString("AddExpense"));
        clearFields();
        addToCurrency();
        for (Participant p : this.event.getParticipants()) {
            if (check(p)) {
                CheckBox cb = new CheckBox(p.getName());
                cb.setDisable(true);
                box.getChildren().add(cb);
                paidBy.getItems().add(p);
            }
        }
        setAddOrEditButton();
        currency.setValue(userConfig.getCurrencyConfig());

        populateTagMenu();
    }

    /**
     * setting the edit or add button
     */
    public void setAddOrEditButton() {
        if(this.expense==null) this.add.setText(Main.getLocalizedString("add"));
        else this.add.setText(Main.getLocalizedString("edit"));
    }

    /**
     * adds values to the currency picker combobox, separate method for "future use"
     * in case of multiple currencies
     * being implemented, keeps refresh cleaner
     */
    private void addToCurrency() {
        if(this.expense==null){
            currency.getItems().add("EUR");
            currency.getItems().add("CHF");
            currency.getItems().add("USD");
        } else{
            clearFields();
            currency.getItems().add(expense.getCurrency());
        }
    }

    /**
     * checks to see if a participant checkbox already exists,
     * if it does it won't be added again when refresh is called
     *
     * @param p participant we want to have as a checkbox option
     * @return true/false is the participant already there
     */
    public boolean check(Participant p) {
        List<CheckBox> cb = new ArrayList<>();
        listOf(cb);
        for (CheckBox c : cb) {
            if (c.getText().equals(p.getName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * adds the participants involved in an expense to a list
     *
     * @return list of participants in debt in an expense
     */
    public List<Participant> add() {
        List<Participant> in = new ArrayList<>();
        if (everybodyIn.isSelected()) {
            in.addAll(event.getParticipants());
        } else if (someIn.isSelected()) {
            ticked(in);
        }

        return in;
    }

    /**
     * adds all the participants of an expense to the list in
     *
     * @param in list with all the participants of the expense
     */
    private void ticked(List<Participant> in) {
        List<CheckBox> checkBoxes = new ArrayList<>();

        listOf(checkBoxes);
        for (CheckBox c : checkBoxes) {
            if (c.isSelected()) {
                String name = c.getText();
                for (Participant p : event.getParticipants()) {
                    if (Objects.equals(p.getName(), name)) {
                        in.add(p);
                    }
                }
            }
        }
    }

    /**
     * adds all checkboxes, all possible participants to be picked for an expense to
     * a list
     *
     * @param checkBoxes list of all checkboxes
     */
    private void listOf(List<CheckBox> checkBoxes) {
        for (Node node : box.getChildren()) {
            if (node instanceof CheckBox) {
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

    /**
     * deselects only some in when everybody in is selected
     */
    public void deSelSome() {
        someIn.setSelected(false);
        List<CheckBox> checks = new ArrayList<>();
        listOf(checks);
        for (CheckBox c : checks) {
            c.setSelected(false);
            c.setDisable(true);
        }
    }

    /**
     * deselects "equally between everybody" checkbox if "only some people" is
     * picked
     */
    public void deSelAll() {
        everybodyIn.setSelected(false);
        List<CheckBox> checks = new ArrayList<>();
        listOf(checks);
        for (CheckBox c : checks) {
            if (c.isDisable()) {
                c.setDisable(false);
            } else {
                c.setDisable(true);
            }
        }
    }

    /**
     * Populates the tag menu with the tags from the server.
     */
    public void populateTagMenu() {
        List<Tag> tags = event.getTags();
        tagMenu.getItems().clear();
        for (Tag tag : tags) {
            MenuItem menuItem = new MenuItem(tag.getName());
            menuItem.setOnAction(e -> handleTagSelection(tag));

            String colorStyle = String.format("-fx-background-color: rgba(%d, %d, %d, 1);", tag.getRed(),
                    tag.getGreen(), tag.getBlue());

            double brightness = (tag.getRed() * 0.299 + tag.getGreen() * 0.587 + tag.getBlue() * 0.114) / 255;
            String textColor = brightness < 0.5 ? "white" : "black";
            colorStyle += String.format("-fx-text-fill: %s;", textColor);

            menuItem.setStyle(colorStyle);
            tagMenu.getItems().add(menuItem);
        }
    }

    /**
     * Handles the selection of a tag from the tag menu.
     *
     * @param selected The selected tag.
     */
    private void handleTagSelection(Tag selected) {
        tagMenu.setText(selected.getName());
        this.selectedTag = selected;
    }

    /**
     * Initializes the scene for adding or editing tags.
     */
    public void goToAddTags() {
        mainCtrl.showAddTag(event);
    }

    /**
     * Populates the add/edit expense scene with information about the (already
     * existing) selected expense.
     *
     * @param event   of the expense
     * @param expense selected expense
     */
    public void refreshExp(Event event, Expense expense) {
        this.event = event;
        refresh(event);
        addEditText.setText(Main.getLocalizedString("EditExpense"));
        this.expense = expense;
        setAddOrEditButton();
        addToCurrency();
        this.title.setText(expense.getTitle());
        this.amount.setText(Double.toString(expense.getAmount()));
        LocalDate localDate = expense.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.date.setValue(localDate);
        paidBy.getSelectionModel().select(event.getParticipants().stream().filter(x->x.getId().equals(expense.getPaidBy().getId())).findFirst().get());
        currency.setValue(this.expense.getCurrency());
        Set<Long> expenseParList = new HashSet<>(expense.getInvolvedParticipants().stream().map(x->x.getId()).collect(Collectors.toList()));
        Set<Long> eventParList = new HashSet<>(this.event.getParticipants().stream().map(x->x.getId()).collect(Collectors.toList()));
        if (expenseParList.equals(eventParList)) {
            everybodyIn.setSelected(true);
        } else {
            someIn.setSelected(true);
            deSelAll();
            for (Node n : box.getChildren()) {
                if (n instanceof CheckBox) {
                    CheckBox c = (CheckBox) n;
                    List<String> names = new ArrayList<>();
                    for (Participant p : expense.getInvolvedParticipants()) {
                        names.add(p.getName());
                    }
                    if (names.contains(c.getText())) {
                        c.setSelected(true);
                    }
                }
            }
        }
        if (expense.getTag() != null) {
            this.tagMenu.setText(expense.getTag().getName());
            this.selectedTag=expense.getTag();
        }
    }

    /**
     * updates the expanse in the event
     *
     * @param expense the updated expense
     */
    public void updateExp(Expense expense) {
        int index = 0;
        for (Expense e : this.event.getExpenses()) {
            if (e.getId().equals(expense.getId())) {
                this.event.getExpenses().set(index, expense);
                System.out.println(event.getExpenses()); // TODO
                return;
            } else {
                index++;
            }
        }
    }

    /**
     * Sets the scene according to the previous version of the same expense
     */
    @FXML
    public void undo(){
        if(expense==null||mainCtrl.getPrevExp(expense.getId())==null) errorPopup("Undo unavailable");
        else{
            mainCtrl.deletePrevExp(this.expense);
            if(mainCtrl.getPrevExp(this.expense.getId())==null){
                errorPopup("Undo unavailable");
                return;
            }
            Expense  expense1 = mainCtrl.getPrevExp(expense.getId());
            final Boolean[] b = {true};
            expense1.getInvolvedParticipants().forEach((p)-> b[0] = b[0] &&this.event.getParticipants().contains(p));
            b[0] = b[0] && this.event.getParticipants().contains(expense1.getPaidBy()) && this.event.getTags().contains(expense1.getTag());
            if(!b[0]){
                undo();
                return;
            }
            clearFields();
            refreshExp(this.event,mainCtrl.getPrevExp(expense.getId()));
        }

    }

    /**
     * Sets the instruction popups for all shortcuts
     */
    public void setInstructions(){
        mainCtrl.instructionsPopup(new Label(" press ENTER \n to add an expense "), this.add);
        mainCtrl.instructionsPopup(new Label(" press ESC to go \n back to event overview "), this.abort);
        mainCtrl.instructionsPopup(new Label(" press CTRL + U \n to undo changes "), this.undo);
    }
}
