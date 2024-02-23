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

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private StartScreenCtrl startScreenCtrl;
    private Scene startScreen;

    private OverviewCtrl eventOverviewCtrl;
    private Scene eventOverview;


    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
            Pair<AddQuoteCtrl, Parent> add, Pair<StartScreenCtrl, Parent> startScreen,
            Pair<OverviewCtrl, Parent> eventOverview) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.startScreenCtrl = startScreen.getKey();
        this.startScreen = new Scene(startScreen.getValue());

        this.eventOverviewCtrl = eventOverview.getKey();
        this.eventOverview = new Scene(eventOverview.getValue());

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

    public void showStartScreen() {
        primaryStage.setTitle("Start Screen");
        primaryStage.setScene(startScreen);
    }

    public void showEventOverview() {
        primaryStage.setTitle("Overview");
        primaryStage.setScene(eventOverview);
    }
}