package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

/**
 * Controller class for the start screen scene.
 */
public class StartScreenCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * Constructs a new instance of StartScreenCtrl.
     *
     * @param server   The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public StartScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Shows the overview scene.
     */
    public void showOverview() {
        mainCtrl.showOverview();
    }
}
