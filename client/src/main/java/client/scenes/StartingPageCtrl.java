package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;


public class StartingPageCtrl implements Main.UpdatableUI {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Text startingPage;
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
        serverUrl.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                setServer();
            }
        });
        adminPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                adminLogin();
            }
        });
    }

    private void setServer() {
        if (serverUrl.getText().isBlank()) {
            emptyFieldsError("server needed to enter the application");
        }
        try {
            return;
        } catch(Exception e) {
            e.printStackTrace();
            emptyFieldsError(e.getMessage());
        }
    }

    private void adminLogin() {
        if (adminPassword.getText().isBlank()) {
            emptyFieldsError("Password needed to enter the admin overview");
        }
//        else if (serverUrl.getText().isBlank()) {
//            emptyFieldsError("server needed to enter the application");
//        }

         try {
             String password = adminPassword.getText().trim();
             server.adminLogin(password);
             mainCtrl.showAdminEventOverview();
         } catch(Exception e) {
             e.printStackTrace();
             emptyFieldsError(e.getMessage());
         }
    }

    private void emptyFieldsError(String message) {
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
     *
     */
    @Override
    public void updateUI() {
        startingPage.setText(Main.getLocalizedString("startingPage"));
        serverUrlText.setText(Main.getLocalizedString("serverUrl"));
        adminPasswordText.setText(Main.getLocalizedString("adminPassword"));
        setServer.setText(Main.getLocalizedString("setServer"));
        login.setText(Main.getLocalizedString("login"));
    }
}
