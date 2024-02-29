/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;
    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;
    private AddExpenseCtrl addExpenseCtrl;
    private Scene addExpense;
    private ContactDetailsCtrl contactDetailsCtrl;
    private Scene contactDetails;
    private InvitationCtrl invitationCtrl;
    private Scene invitation;
    private OpenDebtsCtrl openDebtsCtrl;
    private Scene openDebts;
    private StatisticsCtrl statisticsCtrl;
    private Scene statistics;
    private AddQuoteCtrl addCtrl;
    private Scene add;
    private StartScreenCtrl startScreenCtrl;
    private Scene startScreen;
    private OverviewCtrl eventOverviewCtrl;
    private Scene eventOverview;

    /**
     * Initializes the main controller with the provided dependencies and sets up the primary stage.
     * This method sets the primary stage and initializes scenes for different scenes.
     * It also associates each scene with its corresponding controller.
     * Finally, it shows the overview scene and displays the primary stage.
     *
     * @param primaryStage   The primary stage of the JavaFX application.
     * @param overview       A Pair containing the OverviewCtrl instance and its corresponding parent Node.
     * @param add            A Pair containing the AddQuoteCtrl instance and its corresponding parent Node.
     * @param addExpense     A Pair containing the addExpense instance and its corresponding parent Node.
     * @param contactDetails A Pair containing the contactDetails instance and its corresponding parent Node.
     * @param invitation     A Pair containing the invitation instance and its corresponding parent Node.
     * @param openDebts      A Pair containing the openDebts instance and its corresponding parent Node.
     * @param statistics     A Pair containing the statistics instance and its corresponding parent Node.
     * @param startScreen    A Pair containing the StartScreenCtrl instance and its corresponding parent Node.
     * @param eventOverview  A Pair containing the OverviewCtrl instance and its corresponding parent Node.
     */
    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add, Pair<AddExpenseCtrl, Parent> addExpense,
                           Pair<ContactDetailsCtrl, Parent> contactDetails, Pair<InvitationCtrl, Parent> invitation,
                           Pair<OpenDebtsCtrl, Parent> openDebts, Pair<StatisticsCtrl, Parent> statistics,
                           Pair<StartScreenCtrl, Parent> startScreen, Pair<OverviewCtrl, Parent> eventOverview) {

        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.startScreenCtrl = startScreen.getKey();
        this.startScreen = new Scene(startScreen.getValue());

        this.eventOverviewCtrl = eventOverview.getKey();
        this.eventOverview = new Scene(eventOverview.getValue());

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpense = new Scene(addExpense.getValue());

        this.contactDetailsCtrl = contactDetails.getKey();
        this.contactDetails = new Scene(contactDetails.getValue());

        this.invitationCtrl = invitation.getKey();
        this.invitation = new Scene(invitation.getValue());

        this.openDebtsCtrl = openDebts.getKey();
        this.openDebts = new Scene(openDebts.getValue());

        this.statisticsCtrl = statistics.getKey();
        this.statistics = new Scene(statistics.getValue());

//        showOverview();
        showStartScreen();
        primaryStage.show();
    }

    /**
     * Displays the quote scene in the primary stage and refreshes its contents.
     */
    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    /**
     * Displays the add quote scene in the primary stage.
     * Associates the key pressed event with the AddQuoteCtrl.
     */
    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    /**
     * Displays the start screen scene in the primary stage.
     */
    public void showStartScreen() {
        primaryStage.setTitle("Start Screen");
        primaryStage.setScene(startScreen);
    }

    /**
     * Displays the event overview scene in the primary stage.
     */
    public void showEventOverview() {
        primaryStage.setTitle("Overview");
        primaryStage.setScene(eventOverview);
    }

    public void showAddExpense() {
        primaryStage.setTitle("AddExpense");
        primaryStage.setScene(addExpense);
    }

    /**
     * Displays the Contact Details scene in the primary stage.
     */
    public void showContactDetails() {
        primaryStage.setTitle("ContactDetails");
        primaryStage.setScene(contactDetails);
    }

    /**
     * Displays the Invitation scene in the primary stage.
     */
    public void showInvitation() {
        primaryStage.setTitle("Invitation");
        primaryStage.setScene(invitation);
    }

    /**
     * Displays the Open Debts scene in the primary stage.
     */
    public void showOpenDebts() {
        primaryStage.setTitle("OpenDebts");
        primaryStage.setScene(openDebts);
    }

    /**
     * Displays the Statistics scene in the primary stage.
     */
    public void showStatistics() {
        primaryStage.setTitle("Statistics");
        primaryStage.setScene(statistics);
    }
}