package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class StartingPageCtrl implements Main.UpdatableUI {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public TextField serverUrl;
    @FXML
    public PasswordField adminPassword;
    @FXML
    public Button setUrl;
    @FXML
    public Button login;
    @FXML
    private AnchorPane ap;


    /**
     * Constructs a new instance of StartingPageCtrl.
     *
     * @param server   The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public StartingPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initialized the start screen and the listview
     */
    public void initialize() {
        ap.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                mainCtrl.showStartScreen();
            }
        });
    }

    /**
     * Refreshes the starting page.
     */
    public void refresh() {
        serverUrl.clear();
        adminPassword.clear();
    }

    @Override
    public void updateUI() {

    }
}
