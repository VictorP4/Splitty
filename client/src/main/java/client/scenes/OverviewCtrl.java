package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;


public class OverviewCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    @Inject
    public OverviewCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }
}
