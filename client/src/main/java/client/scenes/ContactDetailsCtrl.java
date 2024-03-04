package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class ContactDetailsCtrl implements Main.UpdatableUI {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Text aeParticipant;
    @FXML
    public Text name;
    @FXML
    public Text email;
    @FXML
    public Button abort;
    @FXML
    public Button okButton;

    /**
     * Constructs a new instance of a ContactDetailsCtrl.
     *
     * @param server The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public ContactDetailsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void updateUI() {
        aeParticipant.setText(Main.getLocalizedString("AEParticipant"));
        name.setText(Main.getLocalizedString("cdName"));
        email.setText(Main.getLocalizedString("email"));
        abort.setText(Main.getLocalizedString("abort"));
        okButton.setText(Main.getLocalizedString("ok"));
    }
}
