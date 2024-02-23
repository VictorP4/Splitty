package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;


public class OverviewCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

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
}
