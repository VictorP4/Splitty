package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class StatisticsCtrl implements Main.UpdatableUI {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    @FXML
    public Text stats;
    @FXML
    public Text eventCost;
    @FXML
    public Button backButton;
    @FXML
    public Text total;

    /**
     * Constructs a new instance of a StatisticsCtrl
     *
     * @param server The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public StatisticsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Initializes the controller.
     */
    @Override
    public void updateUI() {
        stats.setText(Main.getLocalizedString("statistics"));
        eventCost.setText(Main.getLocalizedString("statEventCost"));
        backButton.setText(Main.getLocalizedString("back"));
        getTotal();
    }

    /**
     * Sets the event for which the statistics are to be displayed.
     */
    public void getTotal() {
        double totalCost = event.getExpenses().stream()
                .mapToDouble(Expense::getAmount).sum();
        total.setText(String.format("%.2f \u20AC", totalCost));
    }

    /**
     * Sets the event for which the statistics are to be displayed.
     *
     * @param event The event for which the statistics are to be displayed.
     */
    public void refresh(Event event) {
        this.event = event;
        getTotal();
    }

    /**
     * Returns to the Overview scene.
     */
    public void back() {
        mainCtrl.showEventOverview(event);
    }
}
