package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

/**
 * Controller class for the overview scene.
 */
public class OverviewCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    /**
     * Constructs a new instance of OverviewCtrl.
     *
     * @param serverUtils The utility class for server-related operations.
     * @param mainCtrl    The main controller of the application.
     */
    @Inject
    public OverviewCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Shows the invitation scene.
     */
    public void showInvites() {
        mainCtrl.showInvitation();
    }
}
