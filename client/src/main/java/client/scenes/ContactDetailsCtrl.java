package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class ContactDetailsCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @Inject
    public ContactDetailsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
}
