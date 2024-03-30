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
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
import static client.Main.switchLocale;

/**
 * Controller class for the overview scene.
 */
public class OverviewCtrl implements Main.UpdatableUI {

    private static final String SELECTED_IMAGE_KEY =  "selectedImage";
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    @FXML
    public Button addExpense;
    @FXML
    public Button home;
    @FXML
    public ImageView menuButtonView;
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
    private ListView<Expense> expenseList;
    private ObservableList<Expense> original;
    @FXML
    private FlowPane participantsField;
    @FXML
    private Button statistics;
    private WebSocketUtils webSocket;
    @FXML
    private Pane options;
    @FXML
    public AnchorPane ap;
    private boolean admin;
    private final UserConfig userConfig = new UserConfig();
    private Map<Long,List<Expense>> previousExpenses;

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
        admin=false;
        String lp = userConfig.getLanguageConfig();
        if (lp.equals("en") || lp.equals("nl") || lp.equals("es")) {
            Image image = new Image("/client/misc/" + lp +  "_flag.png");
            menuButtonView.setImage(image);
        }

        ap.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                backToStartScreen();
            }
            if(event.isControlDown() && event.getCode() == KeyCode.S){
                showStatistics();
            }
            if(event.isControlDown() && event.getCode() == KeyCode.L){
                langButton.fire();
            }
            if(event.isControlDown() && event.getCode() == KeyCode.A){
                toAddExpense();
            }
            if(event.isShiftDown() && event.isControlDown()){
                mainCtrl.showOpenDebts(this.event);
            }
        });
        expenseList = new ListView<>();
        webSocket.connect("ws://localhost:8080/websocket");
        webSocket.addEventListener((event)->{
            if(this.event==null||!this.event.getId().equals(event.getId())) return;
            else{
                Platform.runLater(()->{
                    refresh(event);
                });
            }
        });
        setParticipantsPopup();
    }

    private void setParticipantsPopup() {
        Label pop = new Label("Double right click for delete,\n and double left click for edit");
        pop.setStyle(" -fx-background-color: white; -fx-border-color: black;");
        pop.setMinSize(100,50);
        Popup popup = new Popup();
        popup.getContent().add(pop);
        participants.setOnMouseEntered(event ->{
            popup.show(mainCtrl.getPrimaryStage(),event.getScreenX(), event.getScreenY()+5);
        });
        participants.setOnMouseExited(event ->{
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
                        alert.showAndWait().ifPresent((response)->{
                            if(response == ButtonType.OK){
                                event.removeParticipant(contact);
                                serverUtils.updateEvent(event);
                                List<Expense> toDelete = new ArrayList<>();
                                for(Expense expense : event.getExpenses()){
                                    if(expense.getPaidBy().equals(contact)) toDelete.add(expense);
                                    else if(expense.getInvolvedParticipants().contains(contact)){
                                        if(expense.getInvolvedParticipants().size()==1) toDelete.add(expense);
                                        else{
                                            expense.getInvolvedParticipants().remove(contact);
                                            serverUtils.updateExpense(event.getId(), expense);
                                        }
                                    }
                                }
                                for(Expense expense1: toDelete){
                                    serverUtils.deleteExpense(event.getId(), expense1);
                                }
                                serverUtils.deleteParticipant(contact);
                                this.event = serverUtils.updateEvent(this.event);
                                participantsDisplay();
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
        }
        else{
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
     *
     * @param actionEvent
     */
    public void switchToEnglish(ActionEvent actionEvent) throws BackingStoreException {
        userConfig.setLanguageConfig("en");
        switchLocale("messages", "en");
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/client/misc/en_flag.png")).toExternalForm());
        menuButtonView.setImage(image);
    }

    /**
     *
     * @param actionEvent
     */
    public void switchToDutch(ActionEvent actionEvent) throws BackingStoreException {
        userConfig.setLanguageConfig("nl");
        switchLocale("messages", "nl");
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/client/misc/nl_flag.png")).toExternalForm());
        menuButtonView.setImage(image);
    }

    public void switchToSpanish(ActionEvent actionEvent) throws BackingStoreException {
        userConfig.setLanguageConfig("es");
        switchLocale("messages","es");
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/client/misc/es_flag.png")).toExternalForm());
        menuButtonView.setImage(image);
    }

    public void addLang(ActionEvent actionEvent) throws BackingStoreException {
        Properties newLang = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/client/misc/langTemplate.txt"))) {
            newLang.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String newLangPath;
        try (OutputStream output = new FileOutputStream("src/main/resources/client/misc/messages.properties")) {
            newLang.store(output, "Add the name of your new language to the first line of this file as a comment\n"+
                    "Send the final translation version to ooppteam58@gmail.com");

            newLangPath = "src/main/resources/client/misc/messages.properties";
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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
     * want to
     */
    public void backToStartScreen() {
        if(admin){
            mainCtrl.showAdminEventOverview();
        }
        else mainCtrl.showStartScreen();
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
     * Fills the expense list with the expenses of the event
     * @return the list of expenses
     */
    public ListView<Expense> expenseFiller() {
        expenseList.setCellFactory(listView -> new ListCell<Expense>() {
            @Override
            protected void updateItem(Expense expense, boolean empty) {
                super.updateItem(expense, empty);
                if (empty || expense == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(expense.toString());

                    Tag tag = expense.getTag();
                    if (tag != null) {
                        String colorStyle = String.format("-fx-background-color: rgba(%d, %d, %d, 1);", tag.getRed(), tag.getGreen(), tag.getBlue());
                        setStyle(colorStyle);

                        double brightness = (tag.getRed() * 0.299 + tag.getGreen() * 0.587 + tag.getBlue() * 0.114) / 255;

                        String textColor = brightness < 0.5 ? "white" : "black";
                        setTextFill(Color.web(textColor));
                    }
                }
            }
        });
        return expenseList;
    }

    /**
     * Shows all expenses of the event
     */
    public void showAllExpenses() {

        expenseList = new ListView<>();
        original = FXCollections.observableArrayList();

        expenseList = expenseFiller();

        for (Expense e : event.getExpenses()){
            if (e.getTitle().equalsIgnoreCase("debt repayment")){
                continue;
            }
            original.add(e);
        }
        expenseList.setItems(original);
        all.setContent(expenseList);
        selectExpense();
    }

    /**
     * Resets the expenses list and then filters it for all expenses paid by the
     * selected participant in the box
     */
    public void showFromSelected() {
        expenseList =  new ListView<>();
        original = FXCollections.observableArrayList();

        expenseList = expenseFiller();

        for (Expense e : event.getExpenses()) {
            if (e.getTitle().equalsIgnoreCase("debt repayment")) {
                continue;
            }
            if (e.getPaidBy().equals(participantBox.getSelectionModel().getSelectedItem())) {
                original.add(e);
            }
        }
        expenseList.setItems(original);
        fromSelected.setContent(expenseList);
        selectExpense();
    }

    /**
     * Resets the expenses list and then filters it for all expenses that involve
     * then selected participant in the box
     */
    public void showIncludingSelected() {
        expenseList = new ListView<>();
        original = FXCollections.observableArrayList();

        expenseList = expenseFiller();

        for (Expense e : event.getExpenses()) {
            if (e.getTitle().equalsIgnoreCase("debt repayment")) {
                continue;
            }
            if (e.getPaidBy().equals(participantBox.getSelectionModel().getSelectedItem()) ||
                    e.getInvolvedParticipants().contains(participantBox.getSelectionModel().getSelectedItem())) {
                original.add(e);
            }
        }

        expenseList.setItems(original);

        inclSelected.setContent(expenseList);
        selectExpense();

    }

    public void refresh(Event event) {
        if(this.event==null||!this.event.getId().equals(event.getId())) previousExpenses = new HashMap<>();
        String lp = userConfig.getLanguageConfig();
        if (lp.equals("en") || lp.equals("nl") || lp.equals("es")) {
            Image image = new Image("/client/misc/" + lp +  "_flag.png");
            menuButtonView.setImage(image);
        }

        this.event = serverUtils.getEvent(event.getId());
        options.setVisible(false);
        block.setVisible(false);
        titlePrepare();
        participantsDisplay();
        setUpParticipantBox();
        showAllExpenses();
    }

    /**
     * switches to the Open Debt scene
     * @param actionEvent event that calls the method, click on the button
     */
    public void settleDebts(ActionEvent actionEvent) {
        mainCtrl.showOpenDebts(event);
    }

    /**
     * sets up the choice box "participant box", clears all options,
     * then adds all current participant of the event
     */
    public void setUpParticipantBox(){
        participantBox.getItems().removeAll(participantBox.getItems());
        for(Participant p : this.event.getParticipants()){
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
    public void selected(){
        showFromSelected();
        showIncludingSelected();
    }

    /**
     * When an expense is clicked on / selected an options pop-up pops-up
     */
    public void selectExpense(){

        this.expenseList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.expenseList.setOnMouseClicked((MouseEvent event) -> {

            if(event.getClickCount() == 2){
                options.setVisible(true);
                block.setVisible(true);
            }

        });
    }

    /**
     * Closes the options popup without any changes to the expense
     */
    public void cancel(){
        options.setVisible(false);
        block.setVisible(false);
    }

    /**
     * Deletes the selected expense
     */
    public void delete(){
        try{
            Response response = serverUtils.deleteExpense(this.event.getId(), expenseList.getSelectionModel().getSelectedItem());
            if(response.getStatus() == Response.Status.OK.getStatusCode()){
                System.out.println("OK! good job " + response.getStatus() );
                deletePrevExp(expenseList.getSelectionModel().getSelectedItem());
            }
            else{
                System.out.println("Status code: " + response.getStatus());
            }

//            event.removeExpense(expenseList.getSelectionModel().getSelectedItem());
            this.event = serverUtils.getEvent(this.event.getId());
        }
        finally {
            options.setVisible(false);
            block.setVisible(false);
            mainCtrl.showEventOverview(event);
        }
    }

    /**
     * Shows add/edit expense overview with the selected expense so the user can edit it
     */
    public void edit(){
        Expense toEdit = expenseList.getSelectionModel().getSelectedItem();

        if(toEdit == null) {
            System.out.println("nothing selected");
        }
        else {
            mainCtrl.showExpense(this.event, toEdit);
        }
    }

    /**
     * marks that an admin is accessing the event overview
     * @param b
     */
    public void setAdmin(boolean b) {
        this.admin=b;
    }
    /**
     * deletes the expense from the cached ones
     * @param expense the expense to be deleted from the cache
     */
    public void deletePrevExp(Expense expense){
        if(previousExpenses.get(expense.getId())!=null){
            previousExpenses.get(expense.getId()).removeLast();
            if(previousExpenses.get(expense.getId()).size()==0) previousExpenses.remove(expense.getId());
        }
    }
    /**
     * add an expense to the cache
     * @param expense the expense to be added
     */
    public void addPrevExp(Expense expense){
        if(previousExpenses.get(expense.getId())==null){
            previousExpenses.put(expense.getId(),new ArrayList<>());
            previousExpenses.get(expense.getId()).add(expense);
        }
        else previousExpenses.get(expense.getId()).add(expense);
    }
    /**
     * Returning the previous version of the expense stored in the cache
     * @param id the id of the expense
     * @return the previous version of the expense
     */
    public Expense getPrevExp(Long id){
        if(previousExpenses.get(id)==null) return null;
        return previousExpenses.get(id).get(previousExpenses.get(id).size()-1);
    }
}
