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

import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;


public class MainCtrl {

    private Stage primaryStage;
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
    private StartScreenCtrl startScreenCtrl;
    private Scene startScreen;
    private OverviewCtrl eventOverviewCtrl;
    private Scene eventOverview;
    private AddTagCtrl addTagCtrl;
    private Scene addTag;

    private AdminEventOverviewCtrl adminEventOverviewCtrl;
    private Scene adminEventOverview;

    private SettingsPageCtrl settingsPageCtrl;
    private Scene settingsPage;

    /**
     * Initializes the main controller with the provided dependencies and sets up the primary stage.
     * This method sets the primary stage and initializes scenes for different scenes.
     * It also associates each scene with its corresponding controller.
     * Finally, it shows the overview scene and displays the primary stage.
     *
     * @param primaryStage   The primary stage of the JavaFX application.
     * @param addExpense     A Pair containing the addExpense instance and its corresponding parent Node.
     * @param contactDetails A Pair containing the contactDetails instance and its corresponding parent Node.
     * @param invitation     A Pair containing the invitation instance and its corresponding parent Node.
     * @param openDebts      A Pair containing the openDebts instance and its corresponding parent Node.
     * @param statistics     A Pair containing the statistics instance and its corresponding parent Node.
     * @param startScreen    A Pair containing the StartScreenCtrl instance and its corresponding parent Node.
     * @param eventOverview  A Pair containing the OverviewCtrl instance and its corresponding parent Node.
     */
    public void initialize(Stage primaryStage, Pair<AddExpenseCtrl, Parent> addExpense,
                           Pair<ContactDetailsCtrl, Parent> contactDetails, Pair<InvitationCtrl, Parent> invitation,
                           Pair<OpenDebtsCtrl, Parent> openDebts, Pair<StatisticsCtrl, Parent> statistics,
                           Pair<StartScreenCtrl, Parent> startScreen, Pair<OverviewCtrl, Parent> eventOverview,
                           Pair<AdminEventOverviewCtrl, Parent> adminEventOverviewCtrl, Pair<AddTagCtrl, Parent> addTag,
                           Pair<SettingsPageCtrl, Parent> settingsPage) {

        this.primaryStage = primaryStage;

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

        this.adminEventOverviewCtrl = adminEventOverviewCtrl.getKey();
        this.adminEventOverview = new Scene(adminEventOverviewCtrl.getValue());

        this.addTagCtrl = addTag.getKey();
        this.addTag = new Scene(addTag.getValue());

        this.settingsPageCtrl = settingsPage.getKey();
        this.settingsPage = new Scene(settingsPage.getValue());

        showStartScreen();
        primaryStage.show();
    }

    /**
     * Displays the starting page scene in the primary stage.
     */
    public void showSettingsPage() {
        primaryStage.setTitle("StartingPage");
        primaryStage.setScene(settingsPage);
        settingsPageCtrl.refresh();
    }

    /**
     * Displays the start screen scene in the primary stage.
     */
    public void showStartScreen() {
        primaryStage.setTitle("StartScreen");
        primaryStage.setScene(startScreen);
        startScreenCtrl.refresh();
    }

    /**
     * Displays the admin event overview scene in the primary stage.
     */
    public void showAdminEventOverview() {
        primaryStage.setTitle("AdminEventOverview");
        adminEventOverviewCtrl.refresh();
        primaryStage.setScene(adminEventOverview);
    }

    /**
     * Displays the event overview scene in the primary stage.
     */
    public void showEventOverview(Event event) {
        eventOverviewCtrl.refresh(event);
        primaryStage.setTitle("EventOverview");
        primaryStage.setScene(eventOverview);
    }

    public void refreshEventOverview(Event event) {
        eventOverviewCtrl.refresh(event);
    }

    /**
     * Displays the add expense scene in the primary stage
     * Associates the key pressed event with the AddExpenseCtrl
     */
    public void showAddExpense(Event event) {
        addExpenseCtrl.refresh(event);
        primaryStage.setTitle("AddExpense");
        primaryStage.setScene(addExpense);
    }

    /**
     * Displays the contact details scene for the given participant in the primary stage.
     *
     * @param participant The participant whose details are to be displayed.
     */
    public void showContactDetails(Participant participant, Event event) {
        contactDetailsCtrl.refresh(participant, event);
        primaryStage.setTitle("ContactDetails");
        primaryStage.setScene(contactDetails);
    }

    /**
     * Updates the participant details in the event overview scene.
     *
     * @param participant The updated participant details.
     */
    public void updateParticipant(Participant participant) {
        eventOverviewCtrl.updateParticipant(participant);
    }

    /**
     * Displays the invitation scene in the primary stage.
     */
    public void showInvitation(Event event) {
        invitationCtrl.refresh(event);
        primaryStage.setTitle("Invitation");
        primaryStage.setScene(invitation);
    }

    /**
     * Displays the open debts scene for the given event in the primary stage.
     *
     * @param event The event for which open debts are to be displayed.
     */
    public void showOpenDebts(Event event) {
        openDebtsCtrl.refresh(event);
        primaryStage.setTitle("OpenDebts");
        primaryStage.setScene(openDebts);
    }

    /**
     * Displays the statistics scene in the primary stage.
     */
    public void showStatistics(Event event) {
        statisticsCtrl.refresh(event);
        primaryStage.setTitle("Statistics");
        primaryStage.setScene(statistics);
    }

    /**
     * Displays the add tag scene in the primary stage.
     */
    public void showAddTag(Event event) {
        addTagCtrl.refresh(event);
        primaryStage.setTitle("AddTag");
        primaryStage.setScene(addTag);
    }

    /**
     * Displays an overview of an expense in the add/edit expense scene
     * @param event of the expense
     * @param expense displayed
     */
    public void showExpense(Event event, Expense expense){
        addExpenseCtrl.refreshExp(event, expense);
        primaryStage.setTitle("EditExpense");
        primaryStage.setScene(addExpense);
    }
    public String getSceneTitle(){
        return primaryStage.getTitle();
    }

}