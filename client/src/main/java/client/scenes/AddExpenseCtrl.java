package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

import java.awt.*;

public class AddExpenseCtrl implements Main.UpdatableUI {
    @FXML
    public Text addEditText;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Text whoPaid;
    @FXML
    public Text whatFor;
    @FXML
    public Text howMuch;
    @FXML
    public Text when;
    @FXML
    public Text howToSplit;
    @FXML
    public CheckBox equally;
    @FXML
    public CheckBox onlySome;
    @FXML
    public Text expenseType;
    @FXML
    public Button abort;

    @FXML
    public Button add;

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
        whoPaid.setText(Main.getLocalizedString("whoPaid"));
        whatFor.setText(Main.getLocalizedString("whatFor"));
        howMuch.setText(Main.getLocalizedString("howMuch"));
        when.setText(Main.getLocalizedString("when"));
        howToSplit.setText(Main.getLocalizedString("howToSplit"));
        equally.setText(Main.getLocalizedString("equally"));
        onlySome.setText(Main.getLocalizedString("onlySome"));
        expenseType.setText(Main.getLocalizedString("expType"));
        abort.setText(Main.getLocalizedString("abort"));
        add.setText(Main.getLocalizedString("add"));

    }
}
