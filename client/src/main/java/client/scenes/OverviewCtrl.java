package client.scenes;

import client.Main;
import client.UserConfig;
import client.services.OverviewService;
import client.services.TagService;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;

import javafx.application.Platform;

import commons.Tag;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Controller class for the overview scene.
 */
public class OverviewCtrl implements Main.UpdatableUI {

    private static final String SELECTED_IMAGE_KEY = "selectedImage";
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final OverviewService overviewService;
    private final TagService tagService;
    private final UserConfig userConfig;
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
    private final WebSocketUtils webSocket;
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
    public Button moneyTransfer;
    @FXML
    public AnchorPane ap;
    private boolean admin;
    private final Preferences prefs = Preferences.userNodeForPackage(OverviewCtrl.class);
    private Map<Long, List<Expense>> previousExpenses;

    /**
     * Constructs an OverviewCtrl object.
     *
     * @param serverUtils The utility class for server interaction.
     * @param mainCtrl    The main controller of the application.
     * @param webSocket The websocket.
     * @param overviewService the OverviewService to be injected.
     * @param tagService the TagService to be injected.
     * @param userConfig the user configuration for persisted data.
     */
    @Inject
    public OverviewCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, WebSocketUtils webSocket,
                        OverviewService overviewService, TagService tagService, UserConfig userConfig) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.webSocket = webSocket;
        this.overviewService = overviewService;
        this.tagService = tagService;
        this.userConfig = userConfig;
        MenuItem item = new MenuItem("Text");
    }

    /**
     * Initializes the controller.
     */
    public void initialize() {
        admin = false;
        expenseTable = new TableView<>();
        refreshExpenseTable();

        webSocket.connect("ws://localhost:8080/websocket");
        webSocket.addEventListener((event) -> {
            if (this.event != null && Objects.equals(this.event.getId(), event.getId())) {
                Platform.runLater(() -> {
                    refresh(event);
                });
            }
        });

        loadLanguageConfig();
        currencyButton.setText(getCurrency());

        setInviteCodeFunctionality();
        initializeShortcuts();
        setParticipantsPopup();
        setParticipantBoxPopup();
        setInstructions();
        buttonSetup();
    }

    /**
     * Initializes the keyboard shortcuts for the overview page.
     */
    private void initializeShortcuts() {
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
            if (event.isControlDown() && event.getCode() == KeyCode.T) {
                toMoneyTransfer();
            }
            if (event.isControlDown() && event.getCode() == KeyCode.D) {
                mainCtrl.showOpenDebts(this.event);
            }
        });
    }

    /**
     * Initializes the functionality of the invite code. This includes a copy on click and visible color changes.
     */
    private void setInviteCodeFunctionality() {
        inviteCode.setOnMouseEntered(colorSwitch -> inviteCode.setFill(Color.rgb(32,178,170)));
        inviteCode.setOnMouseExited(colorSwitch -> {
            inviteCode.setFill(Color.BLACK);
            inviteCode.setEffect(null);
        });
        inviteCode.setOnMouseClicked(codeCopyEvent -> {
            inviteCode.setEffect(new DropShadow(5.0, Color.rgb(32,178,170)));
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(event.getInviteCode());
            clipboard.setContent(content);
        });
    }

    /**
     * Refreshes the expenseTable to show the expenses.
     */
    private void refreshExpenseTable() {
        expenseTable.getItems().clear();
        expenseTable.getColumns().clear();

        TableColumn<Expense, String> dateColumn = new TableColumn<>(Main.getLocalizedString("date"));
        dateColumn.setCellValueFactory(e -> new ReadOnlyObjectWrapper<>(overviewService.formattedDate(e.getValue().getDate())));

        TableColumn<Expense, String> whoPaidColumn = new TableColumn<>(Main.getLocalizedString("whoPaid"));
        whoPaidColumn.setCellValueFactory(e -> new ReadOnlyObjectWrapper<>(e.getValue().getPaidBy().getName()));

        TableColumn<Expense, String> howMuchColumn = new TableColumn<>(Main.getLocalizedString("howMuch"));
        howMuchColumn.setCellValueFactory(e -> new ReadOnlyObjectWrapper<>(
                        Currency.getInstance(getCurrency()).getSymbol()
                                + " " + e.getValue().getAmount()
        ));

        TableColumn<Expense, String> inclParticipantsColumn = new TableColumn<>(Main.getLocalizedString("involvedParticipants"));
        inclParticipantsColumn.setCellValueFactory(e ->  new ReadOnlyObjectWrapper<>(
                overviewService.setParticipantsString(e.getValue().getInvolvedParticipants())));

        TableColumn<Expense, Tag> tagsColumn = new TableColumn<>(Main.getLocalizedString("tag"));
        tagsColumn.setCellValueFactory(e -> new ReadOnlyObjectWrapper<>(e.getValue().getTag()));
        tagsColumn.setCellFactory(column -> new TableCell<>() {
            {
                itemProperty().addListener((obs, oldTag, newTag) -> {
                    if (newTag == null) {
                        setText(null);
                        setStyle(null);
                    } else {
                        setText(newTag.getName());
                        this.setStyle(tagService.getCellColor(newTag));
                        this.setTextFill(Color.web(tagService.getCellColor(newTag)));
                    }
                });
            }
        });

        expenseTable.getColumns().addAll(tagsColumn, dateColumn, whoPaidColumn, howMuchColumn, inclParticipantsColumn);

        // sets the sizes of the columns
        expenseTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        dateColumn.setPrefWidth(80);
        inclParticipantsColumn.prefWidthProperty().bind(expenseTable.widthProperty()
                .subtract(tagsColumn.widthProperty())
                .subtract(whoPaidColumn.widthProperty())
                .subtract(howMuchColumn.widthProperty())
                .subtract(dateColumn.widthProperty())
        );
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
        participants.setOnMouseEntered(event ->
                popup.show(mainCtrl.getPrimaryStage(), event.getScreenX(), event.getScreenY() + 5));
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
        overviewService.removeParticipantFromEvent(this.event, contact);
        event = serverUtils.getEvent(this.event.getId());
        participantsDisplay();
        mainCtrl.showEventOverview(event);
    }

    /**
     * Updates participant information.
     * This method updates participant information and refreshes the display.
     *
     * @param participant The participant to be updated.
     */
    public void updateParticipant(Participant participant) {
        overviewService.updateParticipant(this.event, participant);
        this.event = serverUtils.getEvent(this.event.getId());
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
        loadLanguageConfig();
        Main.switchLocale("messages","en");
        refresh(event);
    }

    /**
     * Switches the language of the app to Dutch.
     */
    public void switchToDutch() throws BackingStoreException {
        userConfig.setLanguageConfig("nl");
        loadLanguageConfig();
        Main.switchLocale("messages","nl");
        refresh(event);
    }

    /**
     * Switches the language of the app to Spanish
     */
    public void switchToSpanish() throws BackingStoreException {
        userConfig.setLanguageConfig("es");
        loadLanguageConfig();
        Main.switchLocale("messages","es");
        refresh(event);
    }

    /**
     * Creates a new messages.properties file and downloads it to a users Downloads directory.
     */
    public void addLang() {
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
     * Directs user towards the money transfer scene
     */
    public void toMoneyTransfer() {
        mainCtrl.showMoneyTransfer(event);
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
     * Changes the preferred currency of the event to Euro (EUR).
     * Updates the event's preferred currency on the server and refreshes the
     * displayed expenses accordingly.
     */
    @FXML
    public void changeCurrencyEUR() {
        userConfig.setCurrencyConfig("EUR");
        currencyButton.setText("EUR");
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
        currencyButton.setText("USD");
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
        currencyButton.setText("CHF");
        refresh(this.event);
        showAllExpenses();
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
     * Shows all expenses of the event
     */
    public void showAllExpenses() {
        expenseTable = new TableView<>();
        refreshExpenseTable();
        original = overviewService.getAllExpenses(this.event);
        original = (ObservableList<Expense>) overviewService.convertCurrency(original, getCurrency());
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
        Participant p = participantBox.getSelectionModel().getSelectedItem();
        original = overviewService.getFromSelected(this.event, p);
        original = (ObservableList<Expense>) overviewService.convertCurrency(original, getCurrency());
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
        Participant p = participantBox.getSelectionModel().getSelectedItem();
        original = overviewService.getIncludingSelected(this.event, p);
        original = (ObservableList<Expense>) overviewService.convertCurrency(original, getCurrency());
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

        loadLanguageConfig();
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
     * Loads the language configuration of the user and displays a flag when necessary.
     */
    private void loadLanguageConfig() {
        userConfig.reloadLanguageFile();
        String lp = userConfig.getLanguageConfig();
        if (lp.equals("en") || lp.equals("nl") || lp.equals("es")) {
            Image image = new Image(prefs.get(SELECTED_IMAGE_KEY, null));
            prefs.put(SELECTED_IMAGE_KEY, "/client/misc/" + lp + "_flag.png");
            menuButtonView.setImage(image);
        }
    }

    /**
     * switches to the Open Debt scene
     */
    public void settleDebts() {
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
     * Deletes the selected expense and refreshes the overview
     */
    public void delete() {
        try {
            overviewService.deleteExpense(this.event, expenseTable.getSelectionModel().getSelectedItem());
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
        admin = b;
    }

    /**
     * deletes the expense from the cached ones
     *
     * @param expense the expense to be deleted from the cache
     */
    public void deletePrevExp(Expense expense) {
        overviewService.deletePrevExp(previousExpenses, expense);
    }

    /**
     * add an expense to the cache
     *
     * @param expense the expense to be added
     */
    public void addPrevExp(Expense expense) {
        overviewService.addPrevExp(previousExpenses, expense);
    }

    /**
     * Returning the previous version of the expense stored in the cache
     *
     * @param id the id of the expense
     * @return the previous version of the expense
     */
    public Expense getPrevExp(Long id) {
        return overviewService.getPrevExp(previousExpenses, id);
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
        participantBox.setOnMouseExited(mouseEvent -> popup.hide());
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
        mainCtrl.instructionsPopup(new Label(" press CTRL + T to \n transfer money"), this.moneyTransfer);
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
    public UserConfig getUserConfig(){
        return this.userConfig;
    }

}
