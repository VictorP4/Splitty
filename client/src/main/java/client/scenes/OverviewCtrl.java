package client.scenes;

import client.Main;
import client.UserConfig;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;

import javafx.application.Platform;

import jakarta.ws.rs.core.Response;

import commons.Tag;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import commons.Expense;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Popup;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static client.Main.switchLocale;

/**
 * Controller class for the overview scene.
 */
public class OverviewCtrl implements Main.UpdatableUI {

    private static final String SELECTED_IMAGE_KEY = "selectedImage";
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    @FXML
    public Button addExpense;
    @FXML
    public Button home;
    @FXML
    public ImageView menuButtonView;
    @FXML
    public ImageView participantImage;
    @FXML
    private Pane block;
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
    public MenuButton currencyButton;
    @FXML
    private Tab fromSelected;
    @FXML
    private Tab inclSelected;
    @FXML
    private Text title;
    @FXML
    private TextField titleField;
    @FXML
    private ChoiceBox<Participant> participantBox;
    @FXML
    private FlowPane participantsField;
    @FXML
    private Button statistics;
    private WebSocketUtils webSocket;
    @FXML
    private Pane options;
    @FXML
    private Button cancel;
    @FXML
    private Button delete;
    @FXML
    private Button edit;
    @FXML
    public Text inviteCode;
    private ObservableList<Expense> original;
    @FXML
    private TableView<Expense> expenseTable;
    @FXML
    private TableColumn<Expense, String> dateColumn;
    @FXML
    private TableColumn<Expense, String> whoPaidColumn;
    @FXML
    private TableColumn<Expense, String> howMuchColumn;
    @FXML
    private TableColumn<Expense, String> inclParticipantsColumn;
    @FXML
    private TableColumn<Expense, Tag> tagsColumn;
    @FXML
    public AnchorPane ap;
    private boolean admin;
    private Preferences prefs = Preferences.userNodeForPackage(OverviewCtrl.class);;
    private final UserConfig userConfig = new UserConfig();
    private Map<Long, List<Expense>> previousExpenses;

    /**
     * Constructs an OverviewCtrl object.
     *
     * @param serverUtils The utility class for server interaction.
     * @param mainCtrl    The main controller of the application.
     */
    @Inject
    public OverviewCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, WebSocketUtils webSocket) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.webSocket = webSocket;
        MenuItem item = new MenuItem("Text");
    }

    /**
     * Initializes the controller.
     */
    public void initialize() {
        expenseTable = new TableView<>();

        admin = false;
        userConfig.reloadLanguageFile();
        String lp = userConfig.getLanguageConfig();
        if (lp.equals("en") || lp.equals("nl") || lp.equals("es")) {
            Image image = new Image(prefs.get(SELECTED_IMAGE_KEY, null));
            prefs.put(SELECTED_IMAGE_KEY, "/client/misc/" + lp + "_flag.png");
            menuButtonView.setImage(image);
        }

        inviteCode.setOnMouseEntered(colorSwitch -> {
            inviteCode.setStyle("-fx-text-fill: #de6161;");
        });
        inviteCode.setOnMouseExited(colorSwitch -> {
            inviteCode.setStyle("-fx-text-fill: black;");
        });
        inviteCode.setOnMouseClicked(codeCopyEvent -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(event.getInviteCode());
            clipboard.setContent(content);
        });

        ap.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                userConfig.reloadLanguageFile();
                backToStartScreen();
            }
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                showStatistics();
            }
            if (event.isControlDown() && event.getCode() == KeyCode.L) {
                langButton.fire();
            }
            if (event.isControlDown() && event.getCode() == KeyCode.A) {
                toAddExpense();
            }
            if (event.isControlDown() && event.getCode() == KeyCode.D) {
                mainCtrl.showOpenDebts(this.event);
            }
        });
        webSocket.connect("ws://localhost:8080/websocket");
        webSocket.addEventListener((event) -> {
            if (this.event == null || !this.event.getId().equals(event.getId()))
                return;
            else {
                Platform.runLater(() -> {
                    refresh(event);
                });
            }
        });
        setParticipantsPopup();
        setParticipantBoxPopup();
        setInstructions();
        buttonSetup();
        refreshExpenseTable();
    }

    /**
     * Refreshes the expenseTable to show the expenses.
     */
    private void refreshExpenseTable() {
        expenseTable.getItems().clear();
        expenseTable.getColumns().clear();

        dateColumn = new TableColumn<>(Main.getLocalizedString("date"));
        dateColumn.setCellValueFactory(e -> new SimpleStringProperty(formattedDate(e.getValue().getDate())));

        whoPaidColumn = new TableColumn<>(Main.getLocalizedString("whoPaid"));
        whoPaidColumn.setCellValueFactory(e -> new SimpleStringProperty(
                 Currency.getInstance(userConfig.getCurrencyConfig()).getSymbol()
                         + " " + Double.toString(e.getValue().getAmount())));

        howMuchColumn = new TableColumn<>(Main.getLocalizedString("howMuch"));
        howMuchColumn.setCellValueFactory(e -> new SimpleStringProperty(Double.toString(e.getValue().getAmount())));

        inclParticipantsColumn = new TableColumn<>(Main.getLocalizedString("involvedParticipants"));
        inclParticipantsColumn.setCellValueFactory(e ->  new SimpleStringProperty(
                setParticipantsString(e.getValue().getInvolvedParticipants())));

        tagsColumn = new TableColumn<>(Main.getLocalizedString("tag"));
        tagsColumn.setCellValueFactory(e -> new SimpleObjectProperty<>(e.getValue().getTag()));
        tagsColumn.setCellFactory(column -> new TableCell<>() {
            {
                itemProperty().addListener((obs, oldTag, newTag) -> {
                    if (newTag == null) {
                        setText(null);
                        setStyle(null);
                    } else {
                        setText(newTag.getName());
                        setCellColor(this, newTag);
                    }
                });
            }
        });

        expenseTable.getColumns().addAll(tagsColumn, dateColumn, whoPaidColumn, howMuchColumn, inclParticipantsColumn);
    }

    /**
     * Formats the date and returns a string version of it.
     *
     * @param date The date to be formatted.
     * @return the string of the formatted date.
     */
    private String formattedDate(java.util.Date date) {
        Date sqldate = new Date(date.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
        return dateFormat.format(sqldate);
    }

    /**
     * Creates and returns a string of the involvedParticipants list.
     *
     * @return a string of the involvedParticipants list.
     */
    private String setParticipantsString(List<Participant> involvedParticipants) {
        StringBuilder participantString = new StringBuilder();
        for (int i = 0; i < involvedParticipants.size(); i++) {
            participantString.append(involvedParticipants.get(i).getName());
            if (i < involvedParticipants.size() - 1) {
                participantString.append(", ");
            }
        }
        return participantString.toString();
    }

    /**
     * Sets the color of a cell to the color of the tag added.
     *
     * @param cell The cell of which the color should be set.
     * @param tag The tag of which we will set the cell color to.
     */
    private void setCellColor(TableCell<Expense, Tag> cell, Tag tag) {
        String colorStyle = String.format("-fx-background-color: rgba(%d, %d, %d, 1);", tag.getRed(),
                tag.getGreen(), tag.getBlue());
        cell.setStyle(colorStyle);

        double brightness = (tag.getRed() * 0.299 + tag.getGreen() * 0.587
                + tag.getBlue() * 0.114) / 255;
        String textColor = brightness < 0.5 ? "white" : "black";
        cell.setTextFill(Color.web(textColor));
    }

    /**
     * Creates the participant pop-up and sets the styling for it.
     */
    private void setParticipantsPopup() {
        Label pop = new Label(" Double right click for delete, \n and double left click for edit ");
        pop.setStyle(" -fx-background-color: white; -fx-border-color: black;");
        pop.setMinSize(100, 50);
        Popup popup = new Popup();
        popup.getContent().add(pop);
        participants.setOnMouseEntered(event -> {
            popup.show(mainCtrl.getPrimaryStage(), event.getScreenX(), event.getScreenY() + 5);
        });
        participants.setOnMouseExited(event -> {
            popup.hide();
        });
    }

    /**
     * Updates the UI
     */
    @Override
    public void updateUI() {
        addExpense.setText(Main.getLocalizedString("addExpense"));
        sendInvites.setText(Main.getLocalizedString("ovSendInvites"));
        settleDebts.setText(Main.getLocalizedString("ovSettleDebt"));
        expense.setText(Main.getLocalizedString("ovExpense"));
        langButton.setText(Main.getLocalizedString("langButton"));
        fromSelected.setText(Main.getLocalizedString("ovFromSelected"));
        inclSelected.setText(Main.getLocalizedString("ovInclSelected"));
        participants.setText(Main.getLocalizedString("ovParticipants"));
        statistics.setText(Main.getLocalizedString("ovStatistics"));
        currencyButton.setText(Main.getLocalizedString("currency"));
        delete.setText(Main.getLocalizedString("delete"));
        edit.setText(Main.getLocalizedString("edit"));
        cancel.setText(Main.getLocalizedString("cancel"));
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
                        var alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.initModality(Modality.APPLICATION_MODAL);
                        alert.setContentText("Are you sure you want to delete this participant?");

                        alert.showAndWait().ifPresent((response) -> {
                            if (response == ButtonType.OK) {
                                removeParticipantFromEvent(contact);
                            }
                        });

                    } else
                        addParticipant1(contact);
                }
            });
            participantsField.getChildren().add(label);
        }
    }

    /**
     * Removes a participant from the event. It also removed the participant from
     * any related expenses or removed
     * the expense all together if they are the only involved participant.
     * Then it updates the participant display.
     *
     * @param contact the participant to be removed from the event and expenses
     */
    private void removeParticipantFromEvent(Participant contact) {
        event.removeParticipant(contact);
        serverUtils.updateEvent(event);
        List<Expense> toDelete = new ArrayList<>();
        for (Expense expense : event.getExpenses()) {
            if (expense.getPaidBy().equals(contact))
                toDelete.add(expense);
            else if (expense.getInvolvedParticipants().contains(contact)) {
                if (expense.getInvolvedParticipants().size() == 1)
                    toDelete.add(expense);
                else {
                    expense.getInvolvedParticipants().remove(contact);
                    serverUtils.updateExpense(event.getId(), expense);
                }
            }
        }
        for (Expense expense1 : toDelete) {
            serverUtils.deleteExpense(event.getId(), expense1);
        }
        serverUtils.deleteParticipant(contact);
        this.event.setParticipants(serverUtils.getEvent(event.getId()).getParticipants());
        this.event = serverUtils.updateEvent(this.event);
        participantsDisplay();
    }

    /**
     * Updates participant information.
     * This method updates participant information and refreshes the display.
     *
     * @param participant The participant to be updated.
     */
    public void updateParticipant(Participant participant) {
        boolean participantExists = false;
        int index = -1;
        for (Participant p : event.getParticipants()) {
            if (Objects.equals(p.getId(), participant.getId())) {
                index = event.getParticipants().indexOf(p);
                participantExists = true;
                break;
            }
        }
        if (!participantExists) {
            Participant newPart = serverUtils.addParticipant(participant);
            this.event.getParticipants().add(newPart);
        } else {
            serverUtils.updateParticipant(participant);
            this.event.getParticipants().set(index, participant);
        }
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
     * Switches the language of the app to English
     */
    public void switchToEnglish() throws BackingStoreException {
        userConfig.setLanguageConfig("en");
        userConfig.reloadLanguageFile();
        switchLocale("messages", "en");
        Image image = new Image(
                Objects.requireNonNull(getClass().getResource("/client/misc/en_flag.png")).toExternalForm());
        menuButtonView.setImage(image);
        refresh(event);
    }

    /**
     * Switches the language of the app to Dutch.
     */
    public void switchToDutch() throws BackingStoreException {
        userConfig.setLanguageConfig("nl");
        userConfig.reloadLanguageFile();
        switchLocale("messages", "nl");
        Image image = new Image(
                Objects.requireNonNull(getClass().getResource("/client/misc/nl_flag.png")).toExternalForm());
        menuButtonView.setImage(image);
        refresh(event);
    }

    /**
     * Switches the language of the app to Spanish
     */
    public void switchToSpanish() throws BackingStoreException {
        userConfig.setLanguageConfig("es");
        userConfig.reloadLanguageFile();
        switchLocale("messages", "es");
        Image image = new Image(
                Objects.requireNonNull(getClass().getResource("/client/misc/es_flag.png")).toExternalForm());
        menuButtonView.setImage(image);
        refresh(event);
    }

    /**
     * Creates a new messages.properties file and downloads it to a users Downloads directory.
     */
    public void addLang() throws BackingStoreException {
        Properties newLang = new Properties();
        try (BufferedReader reader = new BufferedReader(
                new FileReader("src/main/resources/client/misc/langTemplate.txt"))) {
            newLang.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String newLangPath;
        try (OutputStream output = new FileOutputStream("src/main/resources/client/misc/messages.properties")) {
            newLang.store(output, "Add the name of your new language to the first line of this file as a comment\n" +
                    "Send the final translation version to ooppteam58@gmail.com");

            newLangPath = "client/src/main/resources/client/misc/messages.properties";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File(newLangPath);
        String saveDir = System.getProperty("user.home") + "/Downloads" + "/" + file.getName();
        try {
            Files.move(file.toPath(), Paths.get(saveDir), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File downloaded to: " + saveDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Directs user back to the startScreen. Here they can join other events if they
     * want to.
     */
    public void backToStartScreen() {
        if (admin) {
            mainCtrl.showAdminEventOverview();
        }

        else {
            userConfig.reloadLanguageFile();
            mainCtrl.showStartScreen();
        }
    }

    /**
     * Directs user towards the addExpense scene
     */
    public void toAddExpense() {
        mainCtrl.showAddExpense(event);
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
     * Converts the currency of all the expenses.
     *
     * @param a The list with expenses to be converted.
     * @return a new list of expenses with the converted currencies.
     */
    public List<Expense> convertCurrency(List<Expense> a) {
        String c = userConfig.getCurrencyConfig();
        for (Expense b : a) {
            if (!b.getCurrency().equals(c)) {
                int amn = (int) (serverUtils.convertCurrency(b.getAmount(), b.getCurrency(),
                        c, new Date(b.getDate().getTime()).toLocalDate()) * 100);
                b.setAmount((double) amn / 100);
                b.setCurrency(c);
            }
        }
        return a;
    }

    /**
     * Changes the preferred currency of the event to Euro (EUR).
     * Updates the event's preferred currency on the server and refreshes the
     * displayed expenses accordingly.
     */
    @FXML
    public void changeCurrencyEUR() {
        userConfig.setCurrencyConfig("EUR");
        refresh(this.event);
        showAllExpenses();
    }

    /**
     * Changes the preferred currency of the event to US Dollar (USD).
     * Updates the event's preferred currency on the server and refreshes the
     * displayed expenses accordingly.
     */
    @FXML
    public void changeCurrencyUSD() {
        userConfig.setCurrencyConfig("USD");
        refresh(this.event);
        showAllExpenses();
    }

    /**
     * Changes the preferred currency of the event to Swiss Franc (CHF).
     * Updates the event's preferred currency on the server and refreshes the
     * displayed expenses accordingly.
     */
    @FXML
    public void changeCurrencyCHF() {
        userConfig.setCurrencyConfig("CHF");
        refresh(this.event);
        showAllExpenses();
    }

    /**
     * Shows all expenses of the event
     */
    public void showAllExpenses() {
        expenseTable = new TableView<>();
        refreshExpenseTable();
        original = FXCollections.observableArrayList();

        for (Expense e : event.getExpenses()) {
            if (e.getTitle().equalsIgnoreCase("debt repayment")) {
                continue;
            }
            original.add(e);
        }
        original = (ObservableList<Expense>) convertCurrency(original);
        expenseTable.setItems(original);
        all.setContent(expenseTable);
        selectExpense();
    }

    /**
     * Resets the expenses list and then filters it for all expenses paid by the
     * selected participant in the box
     */
    public void showFromSelected() {
        expenseTable = new TableView<>();
        refreshExpenseTable();
        original = FXCollections.observableArrayList();

        for (Expense e : event.getExpenses()) {
            if (e.getTitle().equalsIgnoreCase("debt repayment")) {
                continue;
            }
            if (e.getPaidBy().equals(participantBox.getSelectionModel().getSelectedItem())) {
                original.add(e);
            }
        }
        original = (ObservableList<Expense>) convertCurrency(original);
        expenseTable.setItems(original);
        fromSelected.setContent(expenseTable);
        selectExpense();
    }

    /**
     * Resets the expenses list and then filters it for all expenses that involve
     * then selected participant in the box
     */
    public void showIncludingSelected() {
        expenseTable = new TableView<>();
        refreshExpenseTable();
        original = FXCollections.observableArrayList();

        for (Expense e : event.getExpenses()) {
            if (e.getTitle().equalsIgnoreCase("debt repayment")) {
                continue;
            }
            if (e.getPaidBy().equals(participantBox.getSelectionModel().getSelectedItem()) ||
                    e.getInvolvedParticipants().contains(participantBox.getSelectionModel().getSelectedItem())) {
                original.add(e);
            }
        }
        original = (ObservableList<Expense>) convertCurrency(original);
        expenseTable.setItems(original);
        inclSelected.setContent(expenseTable);
        selectExpense();
    }

    /**
     * Refreshes the scene.
     *
     * @param event which overview to load
     */
    public void refresh(Event event) {
        if (this.event == null || !this.event.getId().equals(event.getId()))
            previousExpenses = new HashMap<>();

        userConfig.reloadLanguageFile();
        String lp = userConfig.getLanguageConfig();
        if (lp.equals("en") || lp.equals("nl") || lp.equals("es")) {
            Image image = new Image(prefs.get(SELECTED_IMAGE_KEY, null));
            prefs.put(SELECTED_IMAGE_KEY, "/client/misc/" + lp + "_flag.png");
            menuButtonView.setImage(image);
        }

        this.event = serverUtils.getEvent(event.getId());
        inviteCode.setText(event.getInviteCode());
        options.setVisible(false);
        block.setVisible(false);
        titlePrepare();
        participantsDisplay();
        setUpParticipantBox();
        showAllExpenses();
    }

    /**
     * switches to the Open Debt scene
     * 
     * @param actionEvent event that calls the method, click on the button
     */
    public void settleDebts(ActionEvent actionEvent) {
        mainCtrl.showOpenDebts(event);
    }

    /**
     * sets up the choice box "participant box", clears all options,
     * then adds all current participant of the event
     */
    public void setUpParticipantBox() {
        participantBox.getItems().removeAll(participantBox.getItems());
        for (Participant p : this.event.getParticipants()) {
            participantBox.getItems().add(p);
        }
    }

    /**
     * Shows the statistics of the event
     */
    public void showStatistics() {
        mainCtrl.showStatistics(event);
    }

    /**
     * Calls methods showFromSelected & showIncludingSelected
     * when a participant is picked in the choice box
     */
    public void selected() {
        showFromSelected();
        showIncludingSelected();
    }

    /**
     * When an expense is clicked on / selected an options pop-up pops-up
     */
    public void selectExpense() {
        this.expenseTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.expenseTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                options.setVisible(true);
                block.setVisible(true);
            }
        });
    }

    /**
     * Closes the options popup without any changes to the expense
     */
    public void cancel() {
        options.setVisible(false);
        block.setVisible(false);
    }

    /**
     * Deletes the selected expense
     */
    public void delete() {
        try {
            Response response = serverUtils.deleteExpense(this.event.getId(),
                    expenseTable.getSelectionModel().getSelectedItem());
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                System.out.println("OK! good job " + response.getStatus());
                deletePrevExp(expenseTable.getSelectionModel().getSelectedItem());
            } else {
                System.out.println("Status code: " + response.getStatus());
            }
            this.event = serverUtils.getEvent(this.event.getId());
        } finally {
            options.setVisible(false);
            block.setVisible(false);
            mainCtrl.showEventOverview(event);
        }
    }

    /**
     * Shows add/edit expense overview with the selected expense so the user can
     * edit it
     */
    public void edit() {
        Expense toEdit = expenseTable.getSelectionModel().getSelectedItem();
        if (toEdit == null) {
            System.out.println("nothing selected");
        } else {
            mainCtrl.showExpense(this.event, toEdit);
        }
    }

    /**
     * marks if an admin is accessing the event overview (true), or not (false)
     * 
     * @param b the boolean that describes whether the admin is accessing an event overview.
     */
    public void setAdmin(boolean b) {
        this.admin = b;
    }

    /**
     * deletes the expense from the cached ones
     * 
     * @param expense the expense to be deleted from the cache
     */
    public void deletePrevExp(Expense expense) {
        if (previousExpenses.get(expense.getId()) != null) {
            previousExpenses.get(expense.getId()).removeLast();
            if (previousExpenses.get(expense.getId()).isEmpty())
                previousExpenses.remove(expense.getId());
        }
    }

    /**
     * add an expense to the cache
     * 
     * @param expense the expense to be added
     */
    public void addPrevExp(Expense expense) {
        if (previousExpenses.get(expense.getId()) == null) {
            previousExpenses.put(expense.getId(), new ArrayList<>());
            previousExpenses.get(expense.getId()).add(expense);
        } else
            previousExpenses.get(expense.getId()).add(expense);
    }

    /**
     * Returning the previous version of the expense stored in the cache
     * 
     * @param id the id of the expense
     * @return the previous version of the expense
     */
    public Expense getPrevExp(Long id) {
        if (previousExpenses.get(id) == null)
            return null;
        return previousExpenses.get(id).getLast();
    }

    /**
     * Get the currency
     * 
     * @return the currency
     */
    public String getCurrency() {
        return userConfig.getCurrencyConfig();
    }

    /**
     * Sets the popup for participant box explanation.
     */
    public void setParticipantBoxPopup() {
        Label pop = new Label(" Pick the participant ");
        pop.setStyle("-fx-background-color: white; -fx-border-color: black"); // lightPink
        pop.setMinSize(50, 25);
        Popup popup = new Popup();
        popup.getContent().add(pop);
        participantBox.setOnMouseEntered(mouseEvent -> {
            popup.show(mainCtrl.getPrimaryStage(), mouseEvent.getScreenX(), mouseEvent.getScreenY() + 5);
        });
        participantBox.setOnMouseExited(mouseEvent -> {
            popup.hide();
        });
    }

    /**
     * Sets the instruction popups for shortcuts
     */
    public void setInstructions() {
        mainCtrl.instructionsPopup(new Label(" press ESC to go to start screen "), this.home);
        mainCtrl.instructionsPopup(new Label(" press CTRL + S to go \n to the statistics page "), this.statistics);
        mainCtrl.instructionsPopup(new Label(" press CTRL + A to \n go to add expense "), this.addExpense);
        mainCtrl.instructionsPopup(new Label(" press CTRL + D to \n show open debts "), this.settleDebts);
        mainCtrl.instructionsPopup(new Label(" press CTRL + L to \n open language menu "), this.langButton);
    }

    /**
     * Sets the focus and hover over look for the elements on the scene.
     */
    public void buttonSetup() {
        mainCtrl.buttonFocus(this.home);
        mainCtrl.buttonFocus(this.settleDebts);
        mainCtrl.buttonFocus(this.statistics);
        mainCtrl.buttonFocus(this.sendInvites);
        mainCtrl.buttonFocus(this.addExpense);
        mainCtrl.buttonShadow(this.sendInvites);
        mainCtrl.menuButtonFocus(this.langButton);
        mainCtrl.menuButtonFocus(this.currencyButton);

        this.participantImage.focusedProperty().addListener((obs, old, newV) -> {
            if (newV) {
                Color b = Color.rgb(0, 150, 230);
                this.participantImage.setEffect(new DropShadow(10, b));
            } else {
                this.participantImage.setEffect(null);
            }
        });

        this.participantBox.focusedProperty().addListener((obs, old, newV) -> {
            if (newV) {
                Color b = Color.rgb(0, 150, 230);
                this.participantBox.setEffect(new DropShadow(10, b));
            } else {
                this.participantBox.setEffect(null);
            }
        });

        this.currencyButton.setOnMouseEntered(mouseEvent -> {
            this.currencyButton.setEffect(new InnerShadow());

        });
        this.currencyButton.setOnMouseExited(mouseEvent -> {
            this.currencyButton.setEffect(null);
        });

    }

}
