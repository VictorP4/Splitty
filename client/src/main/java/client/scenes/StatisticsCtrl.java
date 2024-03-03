package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class StatisticsCtrl implements Main.UpdatableUI {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Text stats;
    @FXML
    public Text eventCost;

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

    @Override
    public void updateUI() {
        stats.setText(Main.getLocalizedString("statistics"));
        eventCost.setText(Main.getLocalizedString("statEventCost"));
    }
}
