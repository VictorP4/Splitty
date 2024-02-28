package client.scenes;

import client.utils.ServerUtils;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;

import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;


import java.util.ArrayList;
import java.util.List;

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
        debts = FXCollections.observableList(tempDebts);
        List<TilePane> titlePanes= new ArrayList<>();
        debtsOverview = new Accordion();
        for(Debt debt : debts){
            //setting the graphic of pane
            HBox tempBox = new HBox();
            settingGraphicOfPane(debt, tempBox);
            TitledPane tempPane = new TitledPane();
            tempPane.setGraphic(tempBox);
            //setting the content of pane
            AnchorPane tempAP = new AnchorPane();
            setContentOfPane(debt, tempAP);
            tempPane.setContent(tempAP);
            debtsOverview.getPanes().add(tempPane);
        }


    }

    private void setContentOfPane(Debt debt, AnchorPane tempAP) {

        tempAP.setMinHeight(0.0);
        tempAP.setMinWidth(0.0);
        tempAP.setPrefSize(367,205);
        Text text1 = new Text("Bank information available, transfer the money to:");
        text1.setLayoutX(14.0);
        text1.setLayoutY(27.0);
        text1.setStrokeType(StrokeType.OUTSIDE);
        text1.setStrokeWidth(0.0);
        Text text2 = new Text("Account Holder: "+ debt.getPersonOwed().getName());
        text2.setLayoutX(14.0);
        text2.setLayoutY(44.0);
        text2.setStrokeType(StrokeType.OUTSIDE);
        text2.setStrokeWidth(0.0);
        //TODO after IBAN is added to participant
        Text text3 = new Text("IBAN: "+ debt.getPersonOwed());
        text3.setLayoutX(14.0);
        text3.setLayoutY(60.0);
        text3.setStrokeType(StrokeType.OUTSIDE);
        text3.setStrokeWidth(0.0);
        //TODO after BIC is added to participant
        Text text4 = new Text("BIC: "+ debt.getPersonOwed());
        text4.setLayoutX(14.0);
        text4.setLayoutY(77.0);
        text4.setStrokeType(StrokeType.OUTSIDE);
        text4.setStrokeWidth(0.0);
        Text text5 = new Text("Email configured:");
        text5.setLayoutX(14.0);
        text5.setLayoutY(108.0);
        text5.setStrokeType(StrokeType.OUTSIDE);
        text5.setStrokeWidth(0.0);
        Button emailB = new Button("Send reminder");
        emailB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Node source = (Node) event.getSource();
                HBox parent = (HBox) source.getParent();
                ObservableList<Node> list = parent.getChildren();
                list.remove(1);
                list.add(1,new ImageView(new Image("../resources/client/misc/MailActive.png")));

            }
        });
        emailB.setLayoutX(124);
        emailB.setLayoutY(91);
        emailB.setMnemonicParsing(false);
        tempAP.getChildren().addAll(text1,text2,text3,text4,text5,emailB);

    }

    private void settingGraphicOfPane(Debt debt, HBox tempBox) {
        tempBox.setAlignment(Pos.CENTER_RIGHT);
        tempBox.setPrefHeight(26.0);
        tempBox.setPrefWidth(422.0);
        tempBox.setSpacing(5);
        Text text = new Text();
        text.setText(debt.getPersonInDebt().getName()+" gives "+ debt.getAmount()+" € to "+ debt.getPersonOwed().getName());
        text.setWrappingWidth(275);
        text.setStrokeType(StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);
        ImageView imgMail = new ImageView( new Image("../resources/client/misc/MailInactive.png"));
        imgMail.setId("mail");
        ImageView imgBank = new ImageView(new Image("../resources/client/misc/HomeInactive.png"));
        imgBank.setId("bank");
        Button markReceived = new Button("Mark Received");
        markReceived.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Node source = (Node) event.getSource();
                HBox parent = (HBox) source.getParent();
                ObservableList<Node> list = parent.getChildren();
                list.remove(2);
                list.add(2,new ImageView(new Image("../resources/client/misc/HomeActive.png")));
            }
        });
        markReceived.setAlignment(Pos.CENTER_RIGHT);
        //TODO see if insets are necessary for markReceived
        tempBox.getChildren().addAll(text,imgMail,imgBank,markReceived);
    }


}
