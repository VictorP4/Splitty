package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class StartScreenCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public StartScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
}
