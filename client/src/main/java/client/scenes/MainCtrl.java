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




    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
            Pair<AddQuoteCtrl, Parent> add, Pair<AddExpenseCtrl, Parent> addExpense,
                           Pair<ContactDetailsCtrl, Parent> contactDetails, Pair<InvitationCtrl, Parent> invitation,
                           Pair<OpenDebtsCtrl, Parent> openDebts, Pair<StatisticsCtrl, Parent> statistics) {

        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

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

        showOverview();



        primaryStage.show();
    }

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();

    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }


    public void showAddExpense() {
        primaryStage.setTitle("AddExpense");
        primaryStage.setScene(addExpense);
    }
    public void showContactDetails() {
        primaryStage.setTitle("ContactDetails");
        primaryStage.setScene(contactDetails);
    }
    public void showInvitation() {
        primaryStage.setTitle("Invitation");
        primaryStage.setScene(invitation);
    }
    public void showOpenDebts() {
        primaryStage.setTitle("OpenDebts");
        primaryStage.setScene(openDebts);
    }
    public void showStatistics() {
        primaryStage.setTitle("Statistics");
        primaryStage.setScene(statistics);
    }

}