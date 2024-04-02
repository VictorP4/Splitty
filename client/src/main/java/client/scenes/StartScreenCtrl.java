package client.scenes;

import client.Main;
import client.UserConfig;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static client.Main.switchLocale;

/**
 * Controller class for the start screen scene.
 */
public class StartScreenCtrl implements Main.UpdatableUI {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    public Text createNewEvent;
    @FXML
    public Button settingsPage;
    @FXML
    public Text joinEvent;
    @FXML
    public Button createButton;
    @FXML
    public Button joinButton;
    @FXML
    public Text recentViewedEvents;
    @FXML
    public MenuButton langButton;
    @FXML
    public ImageView menuButtonView;
    @FXML
    public Menu customLangs;
    @FXML
    public AnchorPane anchor;
    @FXML
    private TextField eventTitle;
    @FXML
    private TextField eventCode;
    @FXML
    private CheckBox alreadyJoined;
    @FXML
    private ListView<Event> recentlyAccessed;
    private ObservableList<Event> listViewItems;
    private static final String SELECTED_IMAGE_KEY = "selectedImage";
    private WebSocketUtils webSocket;

    private Preferences prefs = Preferences.userNodeForPackage(StartScreenCtrl.class);;
    private final UserConfig userConfig = new UserConfig();

    /**
     * Constructs a new instance of StartScreenCtrl.
     *
     * @param server   The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public StartScreenCtrl(ServerUtils server, MainCtrl mainCtrl, WebSocketUtils webSocket) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.webSocket = webSocket;
    }

    /**
     * Initialized the start screen. It sets the cell factory for the listview,
     * allowing it to be populated with events.
     * Furthermore, the method contain keyPress events for ease of use for the user.
     */
    public void initialize() {
        listViewItems = FXCollections.observableArrayList();
        userConfig.reloadLanguageFile();
        String lp = userConfig.getLanguageConfig();
        if (lp.equals("en") || lp.equals("nl") || lp.equals("es")) {
            Image image = new Image(prefs.get(SELECTED_IMAGE_KEY, null));
            prefs.put(SELECTED_IMAGE_KEY, "/client/misc/"+lp+"_flag.png");
            menuButtonView.setImage(image);
        }

        server.setSERVER(userConfig.getServerURLConfig());
        alreadyJoined.setDisable(true);

        eventCode.textProperty().addListener((observable, oldValue, newValue) -> {
            alreadyJoined.setDisable(newValue == null || newValue.isBlank());
        });
        settingsPage.setOnAction(event -> {
            mainCtrl.showSettingsPage();
        });
        eventCode.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                joinEvent();
            }
        });
        eventTitle.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createEvent();
            }
        });
        anchor.setOnKeyPressed(event ->{
            if(event.isControlDown() && event.getCode() == KeyCode.L){
                langButton.fire();
            }
            if(event.isControlDown() && event.getCode() == KeyCode.S){
                toSettings();
            }
        });

        recentlyAccessed.setCellFactory(lv -> {
            ListCell<Event> lc = new ListCell<>();
            lc.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null) {
                    lc.setText(newItem.getTitle());
                } else {
                    lc.setText(null);
                }
            });
            lc.setOnMouseClicked(me -> {
                if (lc.getItem() != null)
                    mainCtrl.showEventOverview(lc.getItem());
            });
            return lc;
        });
        webSocket.addServerListener(()->{
            Platform.runLater(()->{
                mainCtrl.showStartScreen();
                errorPopup("Server not available");
            });
        });

        setInstructions();

        buttonSetup();
    }

    /**
     * Creates a new event. Has checks for if the event does not have a title due to
     * stop back-end throwing errors.
     */
    public void createEvent() { // :)
        Event createdEvent = new Event();

        if (eventTitle.getText().isBlank()) {
            errorPopup("Why no title? (>_<) <-- this is supposed to be mad");
            return;
        }

        createdEvent.setTitle(eventTitle.getText());
        try {
            createdEvent = server.addEvent(createdEvent);
            Participant creator = new Participant();
            mainCtrl.refreshEventOverview(createdEvent);
            mainCtrl.showContactDetails(creator, createdEvent);
            updateMostRecent(createdEvent);
        } catch (WebApplicationException e) {
            errorPopup(e.getMessage());
        }
    }

    /**
     * Has a participant join an existing event either through an invite code or
     * through pressing
     * a title in the list. Will throw error in case of the event-code not being a
     * long or the event not existing.
     */
    public void joinEvent() {
        boolean newMember = !alreadyJoined.isSelected();
        try {
            String inviteCode = eventCode.getText().trim();
            if (inviteCode.isBlank()) {
                errorPopup("This is not a valid event code :(");
                return;
            }
            Event fetchedEvent = server.getEventByInviteCode(inviteCode);
            if(fetchedEvent==null){
                errorPopup("Invalid invite code");
                return;
            }
            mainCtrl.refreshEventOverview(fetchedEvent);
            if (newMember) {
                Participant joined = new Participant();
                mainCtrl.showContactDetails(joined, fetchedEvent);
            } else {
                mainCtrl.showEventOverview(fetchedEvent);
            }
            updateMostRecent(fetchedEvent);
        } catch (WebApplicationException e) {
            errorPopup(e.getMessage());
        }
    }



    /**
     * updates the list with most recent events.
     * 
     * @param event The current event that has either been created or joined by the
     *              user
     */
    private void updateMostRecent(Event event) {
        listViewItems.removeIf(recent -> recent.getId().equals(event.getId()));
        listViewItems.addFirst(event);
        recentlyAccessed.setItems(listViewItems);
    }

    /**
     * Updates the UI based on the language chosen by the user.
     */
    @Override
    public void updateUI() {
        createNewEvent.setText(Main.getLocalizedString("NewEvent"));
        joinEvent.setText(Main.getLocalizedString("joinEvent"));
        createButton.setText(Main.getLocalizedString("createEventButton"));
        joinButton.setText(Main.getLocalizedString("joinEventButton"));
        recentViewedEvents.setText(Main.getLocalizedString("recentEvents"));
        langButton.setText(Main.getLocalizedString("langButton"));
        settingsPage.setText(Main.getLocalizedString("settings"));
    }

    /**
     * Allows the used to switch to Dutch
     *
     * @param actionEvent The event that caused this method to be called
     */
    public void switchToDutch(ActionEvent actionEvent) throws BackingStoreException {
        userConfig.setLanguageConfig("nl");
        switchLocale("messages", "nl");
        Image image = new Image(
                Objects.requireNonNull(getClass().getResource( "/client/misc/nl_flag.png")).toExternalForm());
        prefs.put(SELECTED_IMAGE_KEY, "/client/misc/nl_flag.png");
        menuButtonView.setImage(image);
    }

    /**
     * Allows the used to switch to English
     *
     * @param actionEvent The event that caused this method to be called
     */
    public void switchToEnglish(ActionEvent actionEvent) throws BackingStoreException {
        userConfig.setLanguageConfig("en");
        switchLocale("messages", "en");
        Image image = new Image(
                Objects.requireNonNull(getClass().getResource("/client/misc/en_flag.png")).toExternalForm());
        prefs.put(SELECTED_IMAGE_KEY, "/client/misc/en_flag.png");
        menuButtonView.setImage(image);
    }

    /**
     * Allows the used to switch to Spanish
     *
     * @param actionEvent The event that caused this method to be called
     */
    public void switchToSpanish(ActionEvent actionEvent) throws BackingStoreException {
        userConfig.setLanguageConfig("es");
        switchLocale("messages", "es");
        Image image = new Image(
                Objects.requireNonNull(getClass().getResource("/client/misc/es_flag.png")).toExternalForm());
        prefs.put(SELECTED_IMAGE_KEY, "/client/misc/es_flag.png");
        menuButtonView.setImage(image);
    }

    public EventHandler<ActionEvent> customSwitch(String bundlePath) throws BackingStoreException {
        switchLocale(bundlePath, null);
        return null;
    }

    public void addLang(ActionEvent actionEvent) throws BackingStoreException {
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
     * Updates all events in listviewItems to keep up with recent updates.
     */
    private void updateAllEvents() {
        try{
            listViewItems.replaceAll(event -> server.getEvent(event.getId()));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Refreshes the startScreen
     */
    public void refresh() {
        userConfig.reloadLanguageFile();
        String lp = userConfig.getLanguageConfig();
        if (lp.equals("en") || lp.equals("nl") || lp.equals("es")) {
            prefs.put(SELECTED_IMAGE_KEY, "/client/misc/"+lp+"_flag.png");
            Image image = new Image(prefs.get(SELECTED_IMAGE_KEY, null));
            menuButtonView.setImage(image);
        }
        eventTitle.clear();
        eventCode.clear();
        alreadyJoined.setDisable(true);
        alreadyJoined.setSelected(false);
        updateAllEvents();
        recentlyAccessed.setItems(listViewItems);

    }

    /**
     * Directs users to the settings page. Here they can fill in a server of their
     * choice & login as an admin
     */
    public void toSettings() {
        mainCtrl.showSettingsPage();
    }

    /**
     * Creates error for invalid action
     * @param message for the popup
     */
    private void errorPopup(String message) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets the instruction popups for shortcuts.
     */
    public void setInstructions(){
        mainCtrl.instructionsPopup(new Label(" press ENTER to create event "), this.createButton);
        mainCtrl.instructionsPopup(new Label(" press ENTER to join event "), this.joinButton);
        mainCtrl.instructionsPopup(new Label(" press CTRL + S \n to go to settings "), this.settingsPage);
        mainCtrl.instructionsPopup(new Label(" press CTRL + L to \n open language menu "), this.langButton);
    }


    public void buttonEffects(Button button){
        button.setOnMouseEntered(event -> button.setEffect(new InnerShadow()));
        button.setOnMouseExited(event -> button.setEffect(null));
        button.focusedProperty().addListener( (obs, old, newV) -> {
            if(newV){
                Color b = Color.rgb(80, 133, 230);
                button.setEffect(new DropShadow(10, b));
            }
            //173, g: 216, b: 230
            else {
                button.setEffect(null);
            }

        });
    }

    public void langMenuSet(){
        langButton.setOnMouseEntered(event -> langButton.setEffect(new InnerShadow()));
        langButton.setOnMouseExited(event -> langButton.setEffect(null));
        langButton.focusedProperty().addListener( (obs, old, newV) -> {
            if(newV){
                Color b = Color.rgb(80, 133, 230);
                langButton.setEffect(new DropShadow(10, b));
            }
            else {
                langButton.setEffect(null);
            }

        });

    }

    public void buttonSetup(){
        buttonEffects(settingsPage);
        buttonEffects(createButton);
        buttonEffects(joinButton);
        langMenuSet();
    }
}
