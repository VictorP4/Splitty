package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class StatisticsCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

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
}
