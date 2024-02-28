package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;

public class OpenDebtsCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ObservableList<Debt> debts;
    @FXML
    private Accordion debtsOverview;

    /**
     * Constructs a new instance of an OpenDebtCtrl
     *
     * @param server The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public OpenDebtsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * goes back go event overview
     */
    public void back(){
        mainCtrl.showEventOverview();
    }

    /**
     * refreshes the debts
     */
    public void refresh(Event event){
        var tempDebts = server.getDebts(event);
        debtsOverview = new Accordion();
    }
}
