package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class OpenDebtsCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @Inject
    public OpenDebtsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
}
