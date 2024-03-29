package client.scenes;

import client.Main;
import client.UserConfig;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import com.google.inject.Inject;
import commons.Event;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.util.List;

public class SettingsPageCtrl implements Main.UpdatableUI {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Text settings;
    @FXML
    public Text serverUrlText;
    @FXML
    public Text adminPasswordText;
    @FXML
    public TextField serverUrl;
    @FXML
    public PasswordField adminPassword;
    @FXML
    public Button setServer;
    @FXML
    public Button login;
    @FXML
    public AnchorPane ap;
    @FXML
    public Button setToLocalServer;
    @FXML
    public Text localServer;
    private final WebSocketUtils webSocket;
    private final UserConfig userConfig = new UserConfig();

    /**
     * Constructs a new instance of StartingPageCtrl.
     *
     * @param server   The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public SettingsPageCtrl(ServerUtils server, MainCtrl mainCtrl, WebSocketUtils webSocket) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.webSocket = webSocket;
    }

    /**
     * Initialized the start screen and the listview
     */
    public void initialize() {
        serverUrl.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                setServer();
            }
        });
        adminPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                login();
            }
        });
        ap.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                mainCtrl.showStartScreen();
            }
        });
    }

    /**
     * Sets the server for the session according to the users choice. If no server
     * is chosen, the server in the user_config file will be selected.
     */
    public void setServer() {
        String userInput = serverUrl.getText();
        String url = "http://" + userInput;
        try {
            if (!userInput.isBlank() && server.checkServer(url).getStatus() == 200) {
                server.setSERVER(url);
                webSocket.disconnect();
                webSocket.connect("ws://" + userInput + "/websocket");
                errorPopup("Succesfully connected to the new server!");
            }
            else {
                errorPopup("Server not found. Changing to default: localhost:8080");
                server.setSERVER(userConfig.getServerURLConfig());
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorPopup("Server not found. Changing to default: localhost:8080");
            server.setSERVER(userConfig.getServerURLConfig());
        }
        refresh();
    }

    /**
     * Lets the admin give a password and login. If no server is chosen, the server
     * in the
     * user_config file will be selected
     */
    public void login() {
        String password = adminPassword.getText().trim();
        if (password.isBlank()) {
            errorPopup("Password needed to enter the admin overview");
        }
        try {
            setServer();
            List<Event> events = server.adminLogin(password); // return null
            mainCtrl.showAdminEventOverview();
        } catch (Exception e) {
            e.printStackTrace();
            errorPopup(e.getMessage());
        }
    }

    /**
     * Sets the server back to the server specified in the config file.
     * This is always "http://localhost:8080"
     */
    public void setToLocalServer() {
        try {
            Response response = server.checkServer(userConfig.getServerURLConfig());
            if (response.getStatus() == 200) {
                server.setSERVER(userConfig.getServerURLConfig());
                errorPopup("Server succesfully changed to default");
            }
        } catch (Exception e) {
            errorPopup(e.getMessage());
            e.printStackTrace();
        }
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
     * Refreshes the settings page.
     */
    public void refresh() {
        serverUrl.clear();
        adminPassword.clear();
    }

    /**
     * Updates the UI based on the language chosen by the user.
     */
    @Override
    public void updateUI() {
        settings.setText(Main.getLocalizedString("settings"));
        serverUrlText.setText(Main.getLocalizedString("serverUrl"));
        adminPasswordText.setText(Main.getLocalizedString("adminPassword"));
        setServer.setText(Main.getLocalizedString("setServer"));
        login.setText(Main.getLocalizedString("login"));
        localServer.setText(Main.getLocalizedString("localServer"));
        setToLocalServer.setText(Main.getLocalizedString("setToLocalServer"));
    }

    /**
     * Takes the user back to the startscreen.
     */
    @FXML
    public void toStartScreen() {
        mainCtrl.showStartScreen();
    }
}
