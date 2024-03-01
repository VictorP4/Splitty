package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.awt.*;

public class AddExpenseCtrl implements Main.UpdatableUI {
    @FXML
    public Text addEditText;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Text whoPaid;

    /**
     * Constructs a new instance of a AddExpenseCtrl.
     *
     * @param server The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void updateUI() {
        addEditText.setText(Main.getLocalizedString("AEExpense"));
        whoPaid.setText(Main.getLocalizedString(""));

    }
}
