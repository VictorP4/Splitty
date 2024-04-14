package client.scenes;

import client.Main;
import client.UserConfig;
import client.models.Debt;
import client.utils.EmailUtils;
import client.services.OpenDebtService;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import com.google.inject.Inject;
import commons.EmailRequestBody;
import commons.Event;
import commons.Expense;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class OpenDebtsCtrl implements Main.UpdatableUI {
    private final MainCtrl mainCtrl;
    @FXML
    public AnchorPane anchor;
    private ObservableList<Debt> debts;
    @FXML
    private Accordion debtsOverview;
    private Event event;
    @FXML
    private Text openDebt;
    private final WebSocketUtils webSocket;
    @FXML
    private Button back;
    private final ServerUtils serverUtils;
    private final OpenDebtService openDebtService;

    /**
     * Constructs a new instance of an OpenDebtCtrl
     *
     * @param mainCtrl        The main controller of the application.
     * @param openDebtService
     */
    @Inject
    public OpenDebtsCtrl(MainCtrl mainCtrl, WebSocketUtils webSocket, ServerUtils serverUtils, OpenDebtService openDebtService) {
        this.mainCtrl = mainCtrl;
        this.webSocket= webSocket;
        this.serverUtils = serverUtils;
        this.openDebtService = openDebtService;
    }

    /**
     * Initializes the open debt scene
     */
    public void initialize(){

        anchor.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ESCAPE){
                back();
            }
        });

        webSocket.addEventListener((event)->{
            if(this.event != null && this.event.getId().equals(event.getId())) {
                Platform.runLater(()-> refresh(event));
            }
        });

        setInstructions();
        mainCtrl.buttonFocus(this.back);
    }
    /**
     * updates the UI
     */
    @Override
    public void updateUI() {
        openDebt.setText(Main.getLocalizedString("openDebt"));
        back.setText(Main.getLocalizedString("back"));
    }

    /**
     * goes back to the event overview
     */
    public void back(){
        mainCtrl.showEventOverview(event);
    }

    /**
     * refreshes the debts
     */
    public void refresh(Event event){
        this.event=event;
        var tempDebts = openDebtService.getDebts(event);
        debts = FXCollections.observableList(tempDebts);
        while(!debtsOverview.getPanes().isEmpty()){
            debtsOverview.getPanes().removeFirst();
        }
        for(Debt debt : debts){
            //setting the graphic of pane
            HBox tempBox = new HBox();
            settingGraphicOfPane(debt, tempBox);
            TitledPane tempPane = new TitledPane();
            tempPane.setGraphic(tempBox);
            //setting the content of pane
            AnchorPane tempAP = new AnchorPane();
            setContentOfPane(debt, tempAP,tempPane);
            tempPane.setContent(tempAP);
            debtsOverview.getPanes().add(tempPane);
        }


    }

    /**
     * Gives the content of the respective pane for the accordion
     *
     * @param debt     the debt which will be in the content
     * @param tempAP   the anchor pane with the content
     * @param tempPane the reference for the title pane
     */
    private void setContentOfPane(Debt debt, AnchorPane tempAP, TitledPane tempPane) {
        tempAP.setMinHeight(0.0);
        tempAP.setMinWidth(0.0);
        tempAP.setPrefSize(367,205);

        tempAP.setStyle("-fx-background-color: efd6da; -fx-border-style: solid; -fx-border-radius: 4;-fx-border-color: black");

        Text text1;
        if(debt.getPersonOwed().getIban()!=null&&debt.getPersonOwed().getBic()!=null&&!debt.getPersonOwed().getIban().isEmpty()) {
            text1 = setTextBox(Main.getLocalizedString("transferTo"),14,27);

            Text text2 = setTextBox(Main.getLocalizedString("accHolder")+" "+ debt.getPersonOwed().getName(),14,44);

            Text text3 = setTextBox("IBAN: " + debt.getPersonOwed().getIban(),14,60);

            Text text4 = setTextBox("BIC: " + debt.getPersonOwed().getBic(),14,77);

            Text text5 = setTextBox(Main.getLocalizedString("emailHolder"),14,108);

            Button emailB = new Button(Main.getLocalizedString("reminder"));
            emailB.setStyle("-fx-background-color: #485a5c; -fx-border-color: 000000; -fx-border-radius: 4; " +
                    "-fx-border-style: solid; -fx-text-fill: white; -fx-font-style: Bold");
            mainCtrl.buttonShadow(emailB);
            mainCtrl.buttonFocus(emailB);
            emailB.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    HBox parent = (HBox) tempPane.getGraphic();
                    ObservableList<Node> list = parent.getChildren();
                    list.remove(1);
                    emailB.setDisable(true);
                    List<String> message = new ArrayList<>();
                    message.add(debt.getPersonInDebt().getName());
                    message.add(debt.getPersonInDebt().getEmail());
                    message.add(debt.getPersonOwed().getName());
                    message.add(debt.getPersonOwed().getIban());
                    message.add(debt.getPersonOwed().getBic());
                    EmailUtils emailUtils = new EmailUtils();
                    emailUtils.sendReminder(new EmailRequestBody(message, String.valueOf(debt.getAmount())),mainCtrl.getUserConfig().getUserEmail(),mainCtrl.getUserConfig().getUserPass());
                    InputStream is = getClass().getResourceAsStream("/client/misc/MailActive.png");
                    ImageView img = new ImageView(new Image(is));
                    try {
                        is.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    img.setFitWidth(16);
                    img.setFitHeight(16);
                    list.add(1, img);
                }
            });
            if(debt.getPersonOwed().getEmail()==null || debt.getPersonOwed().getEmail().isEmpty()) emailB.setDisable(true);
            UserConfig userConfig = mainCtrl.getUserConfig();
            if(userConfig.getUserEmail().isBlank()||userConfig.getUserPass().isBlank()){
                emailB.setDisable(true);
            }
            emailB.setLayoutX(124);
            emailB.setLayoutY(91);
            emailB.setMnemonicParsing(false);
            tempAP.getChildren().addAll(text1, text2, text3, text4, text5, emailB);
        }
        else{
            text1 = setTextBox(Main.getLocalizedString("infoNotAvailable"),14,27);
            tempAP.getChildren().addAll(text1);
        }
    }

    /**
     * Sets the graphic of the individual title pane
     * @param debt the corresponding debt to the pane
     * @param tempBox the HBox which will consist the graphic of the pane
     */
    private void settingGraphicOfPane(Debt debt, HBox tempBox) {
        tempBox.setAlignment(Pos.CENTER_RIGHT);
        tempBox.setPrefHeight(26.0);
        tempBox.setPrefWidth(422.0);
        tempBox.setSpacing(5);
        Text text = new Text();
        int amn =  (int)(serverUtils.convertCurrency(debt.getAmount(),"EUR",
                mainCtrl.getCurrency(), LocalDate.now().minusDays(1))*100);
        double amount = mainCtrl.getCurrency().equals("EUR") ? debt.getAmount() : (double)amn/100;
        text.setText(debt.getPersonInDebt().getName()+" "+Main.getLocalizedString("gives")+" "+ amount +" "+ Currency.getInstance(mainCtrl.getCurrency()).getSymbol()+" "+
                Main.getLocalizedString("to")+" "+ debt.getPersonOwed().getName());
        text.setWrappingWidth(275);
        text.setStrokeType(StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);
        InputStream is = getClass().getResourceAsStream("/client/misc/MailInactive.png");
        ImageView imgMail = new ImageView( new Image(is));
        imgMail.setId("mail");
        imgMail.setFitHeight(16);
        imgMail.setFitWidth(16);
        try {
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InputStream is1 = getClass().getResourceAsStream("/client/misc/HomeInactive.png");
        ImageView imgBank = new ImageView(new Image(is1));
        try {
            is1.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imgBank.setId("bank");
        imgBank.setFitHeight(16);
        imgBank.setFitWidth(16);
        Button markReceived = new Button(Main.getLocalizedString("markReceived"));
        markReceived.setStyle("-fx-background-color: #485a5c; -fx-border-color: 000000; -fx-border-radius: 4; " +
                "-fx-border-style: solid; -fx-text-fill: white; -fx-font-style: Bold");
        mainCtrl.buttonShadow(markReceived);
        mainCtrl.buttonFocus(markReceived);
        Event event1 = this.event;
        markReceived.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Node source = (Node) event.getSource();
                HBox parent = (HBox) source.getParent();
                markReceived.setDisable(true);
                ObservableList<Node> list = parent.getChildren();
                Expense expense = new Expense();
                expense.setPaidBy(debt.getPersonInDebt());
                expense.setDate(new Date());
                expense.setCurrency("EUR");
                expense.setInvolvedParticipants(new ArrayList<>(List.of(debt.getPersonOwed())));
                expense.setAmount(debt.getAmount());
                expense.setTitle("Debt repayment");
                serverUtils.addExpense(expense, event1.getId());
                list.remove(2);
                InputStream is = getClass().getResourceAsStream("/client/misc/HomeActive.png");
                ImageView img = new ImageView(new Image(is));
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                img.setFitHeight(16);
                img.setFitWidth(16);
                list.add(2,img);
            }
        });
        markReceived.setAlignment(Pos.CENTER_RIGHT);
        //TODO see if insets are necessary for markReceived
        tempBox.getChildren().addAll(text,imgMail,imgBank,markReceived);
    }

    /**
     * Sets the instruction popups for shortcuts.
     */
    public void setInstructions(){
        mainCtrl.instructionsPopup(new Label(" press ESC to go back "), this.back);
    }

    /**
     * Setting the text boxes for each pane in the accordion
     * @param string the content
     * @param x the x layout
     * @param y the y layout
     * @return the created Text with the given properties
     */
    public Text setTextBox(String string, int x, int y ){
        Text text1;
        text1 = new Text(string);
        text1.setLayoutX(x);
        text1.setLayoutY(y);
        text1.setStrokeType(StrokeType.OUTSIDE);
        text1.setStrokeWidth(0.0);
        return  text1;
    }
}
