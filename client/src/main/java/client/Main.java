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

import client.scenes.AddExpenseCtrl;
import client.scenes.ContactDetailsCtrl;
import client.scenes.InvitationCtrl;
import client.scenes.OpenDebtsCtrl;
import client.scenes.StatisticsCtrl;
import com.google.inject.Injector;

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

    @Override
    public void start(Stage primaryStage) throws IOException {
        var overview = FXML.load(QuoteOverviewCtrl.class, "client", "scenes", "QuoteOverview.fxml");

        var add = FXML.load(AddQuoteCtrl.class, "client", "scenes", "AddQuote.fxml");
        var addExpense = FXML.load(AddExpenseCtrl.class, "client", "scenes", "AddExpense.fxml");
        var contactDetails = FXML.load(ContactDetailsCtrl.class, "client", "scenes", "ContactDetails.fxml");
        var invitation = FXML.load(InvitationCtrl.class, "client", "scenes", "Invitation.fxml");
        var openDebts = FXML.load(OpenDebtsCtrl.class, "client", "scenes", "OpenDebts.fxml");
        var statistics = FXML.load(StatisticsCtrl.class, "client", "scenes", "Statistics.fxml");


        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, overview, add, addExpense, contactDetails, invitation, openDebts, statistics);
    }
}