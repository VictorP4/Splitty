package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class InvitationCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @Inject
    public InvitationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
}
