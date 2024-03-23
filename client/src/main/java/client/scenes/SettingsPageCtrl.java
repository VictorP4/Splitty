package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
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


    /**
     * Constructs a new instance of StartingPageCtrl.
     *
     * @param server   The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public SettingsPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
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
     * Sets the server for the session according to the users choice. If no server is chosen, a default server will
     * be selected (http://localhost:8080)
     */
    public void setServer() {
        String serverUrlString = serverUrl.getText();
        try {
            if (serverUrlString.isBlank()) {
                server.setSERVER("http://localhost:8080");
            }
            else {
                server.checkServer(serverUrlString);
                server.setSERVER("http://" + serverUrlString);
            }
        } catch(Exception e) {
            e.printStackTrace();
            errorPopup(e.getMessage());
        }
    }

    /**
     * Lets the admin give a password and login. If the admin did not fill in a server, the server is set to the default
     * server (http://localhost:8080).
     */
    public void login() {
        String password = adminPassword.getText().trim();
        if (password.isBlank()) {
            errorPopup("Password needed to enter the admin overview");
        }
        try {
            setServer();
            List<Event> events = server.adminLogin(password);   // return null
            mainCtrl.showAdminEventOverview();
        } catch(Exception e) {
             e.printStackTrace();
             errorPopup(e.getMessage());
        }
    }

    private void errorPopup(String message) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Refreshes the starting page.
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
    }

    public void toStartScreen() {
        mainCtrl.showStartScreen();
    }
}
