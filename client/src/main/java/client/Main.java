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
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import com.google.inject.Injector;

import client.scenes.AddExpenseCtrl;
import client.scenes.ContactDetailsCtrl;
import client.scenes.InvitationCtrl;
import client.scenes.OpenDebtsCtrl;
import client.scenes.StatisticsCtrl;
import client.scenes.OverviewCtrl;
import client.scenes.StartScreenCtrl;
import client.scenes.AddQuoteCtrl;
import client.scenes.MainCtrl;
import client.scenes.QuoteOverviewCtrl;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    /**
     * Entry point for the JavaFX application. Initializes the application and sets up the primary stage.
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException If an error occurs while loading the FXML files.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        // loading all the FXML documents
        var quoteOverview = FXML.load(QuoteOverviewCtrl.class, "client", "scenes", "QuoteOverview.fxml");
        var add = FXML.load(AddQuoteCtrl.class, "client", "scenes", "AddQuote.fxml");
        var addExpense = FXML.load(AddExpenseCtrl.class, "client", "scenes", "AddExpense.fxml");
        var contactDetails = FXML.load(ContactDetailsCtrl.class, "client", "scenes", "ContactDetails.fxml");
        var invitation = FXML.load(InvitationCtrl.class, "client", "scenes", "Invitation.fxml");
        var openDebts = FXML.load(OpenDebtsCtrl.class, "client", "scenes", "OpenDebts.fxml");
        var statistics = FXML.load(StatisticsCtrl.class, "client", "scenes", "Statistics.fxml");
        var startScreen = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");;
        var eventOverview = FXML.load(OverviewCtrl.class, "client", "scenes", "Overview.fxml");;

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, quoteOverview, add, addExpense, contactDetails,
                invitation, openDebts, statistics, startScreen, eventOverview);
    }
}