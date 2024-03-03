package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;

public class AddExpenseCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;

    /**
     * Constructs a new instance of a AddExpenseCtrl.
     *
     * @param server The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Directs a used back to the event overview scene
     */
    public void backToOverview() {
        clearFields();
        mainCtrl.showEventOverview(event);
    }

    /**
     * Clears the fields on the expense page
     */
    private void clearFields() {
        // clear all fields that the user filled in (text fields, checked boxes ect.)
    }
}
