package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;


public class OverviewCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    private Event event;
    @FXML
    public Button addExpenseButton;
    @FXML
    public Text eventTitle;

    /**
     * Constructs a new instance of a OverviewCtrl
     *
     * @param serverUtils The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public OverviewCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }


    public void toAddExpense() {
        mainCtrl.showAddExpense(event);
    }
}
