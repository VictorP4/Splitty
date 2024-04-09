package client.scenes;

import client.Main;
import client.models.Debt;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import com.google.inject.Inject;
import commons.EmailRequestBody;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.core.Response;
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
import javafx.scene.layout.TilePane;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;


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
    private WebSocketUtils webSocket;
    @FXML
    private Button back;
    private ServerUtils serverUtils;

    /**
     * Constructs a new instance of an OpenDebtCtrl
     *
     *
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public OpenDebtsCtrl(MainCtrl mainCtrl, WebSocketUtils webSocket, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.webSocket= webSocket;
        this.serverUtils = serverUtils;
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
            if(this.event==null||!this.event.getId().equals(event.getId())) return;
            else{
                Platform.runLater(()->{
                    refresh(event);
                });
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
        var tempDebts = getDebts(event);
        debts = FXCollections.observableList(tempDebts);
        List<TilePane> titlePanes= new ArrayList<>();
        while(debtsOverview.getPanes().size()>0){
            debtsOverview.getPanes().remove(0);
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
     * Creates a list of transactions to settle the open debt within the group
     *
     * @param event the corresponding event
     * @return list of maximum n-1 transactions to settle debt
     */
    public List<Debt> getDebts(Event event){
        List<Participant> list = new ArrayList<>(event.getParticipants());
        double[] debts = new double[list.size()];

        List<Debt> result = new ArrayList<>();
        list.sort((x,y)->{
            if(x.getDebt()<y.getDebt()) return -1;
            else if(x.getDebt()>y.getDebt()) return 1;
            else return 0;
        });
        for(int k=0;k<debts.length;k++){
            debts[k]=list.get(k).getDebt();
        }
        int i=0;
        int j=list.size()-1;
        while(i<j){
            if(Math.abs(list.get(i).getDebt())<0.01){
                i++;

            }
            else if(Math.abs(list.get(j).getDebt())<0.01){
                j--;

            }
            else if(Math.abs(debts[i]+debts[j])<0.01){
                result.add(new Debt(debts[j],list.get(j),list.get(i)));
                debts[i]=0;
                debts[j]=0;
                i++;
                j--;
            }
            else if(Math.abs(debts[i])<debts[j]){
                result.add(new Debt(Math.abs(debts[i]),list.get(j),list.get(i)));
                debts[j]+=debts[i];
                debts[i]=0;
                i++;

            }
            else{
                result.add(new Debt(debts[j],list.get(j),list.get(i)));
                debts[i]+=debts[j];
                debts[j]=0;
                j--;
            }
        }
        return result;
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

        Text text1 = null;
        if(debt.getPersonOwed().getIban()!=null&&debt.getPersonOwed().getBic()!=null&&!debt.getPersonOwed().getIban().isEmpty()) {
            text1 = new Text(Main.getLocalizedString("transferTo"));
            text1.setLayoutX(14.0);
            text1.setLayoutY(27.0);
            text1.setStrokeType(StrokeType.OUTSIDE);
            text1.setStrokeWidth(0.0);

            Text text2 = new Text(Main.getLocalizedString("accHolder")+" "+ debt.getPersonOwed().getName());
            text2.setLayoutX(14.0);
            text2.setLayoutY(44.0);
            text2.setStrokeType(StrokeType.OUTSIDE);
            text2.setStrokeWidth(0.0);

            Text text3 = new Text("IBAN: " + debt.getPersonOwed().getIban());
            text3.setLayoutX(14.0);
            text3.setLayoutY(60.0);
            text3.setStrokeType(StrokeType.OUTSIDE);
            text3.setStrokeWidth(0.0);

            Text text4 = new Text("BIC: " + debt.getPersonOwed().getBic());
            text4.setLayoutX(14.0);
            text4.setLayoutY(77.0);
            text4.setStrokeType(StrokeType.OUTSIDE);
            text4.setStrokeWidth(0.0);

            Text text5 = new Text(Main.getLocalizedString("emailHolder"));
            text5.setLayoutX(14.0);
            text5.setLayoutY(108.0);
            text5.setStrokeType(StrokeType.OUTSIDE);
            text5.setStrokeWidth(0.0);

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
                    Response response = serverUtils.sendReminder(new EmailRequestBody(message, String.valueOf(debt.getAmount())));
                    if (response.getStatus() == 200) {
                        System.out.println("Reminder sent successfully.");
                    } else {
                        System.out.println("Failed to send reminder. Status code: " + response.getStatus());
                    }
                    ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/client/misc/MailActive.png")));
                    img.setFitWidth(16);
                    img.setFitHeight(16);
                    list.add(1, img);
                }
            });
            if(debt.getPersonOwed().getEmail()==null || debt.getPersonOwed().getEmail().isEmpty()) emailB.setDisable(true);
            emailB.setLayoutX(124);
            emailB.setLayoutY(91);
            emailB.setMnemonicParsing(false);
            tempAP.getChildren().addAll(text1, text2, text3, text4, text5, emailB);
        }
        else{
            text1 = new Text(Main.getLocalizedString("infoNotAvailable"));
            text1.setLayoutX(14.0);
            text1.setLayoutY(27.0);
            text1.setStrokeType(StrokeType.OUTSIDE);
            text1.setStrokeWidth(0.0);
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
        text.setText(debt.getPersonInDebt().getName()+" "+Main.getLocalizedString("gives")+" "+ amount +" "+ Currency.getInstance(mainCtrl.getCurrency()).getSymbol()+" "+Main.getLocalizedString("to")+" "+ debt.getPersonOwed().getName());
        text.setWrappingWidth(275);
        text.setStrokeType(StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);
        ImageView imgMail = new ImageView( new Image(getClass().getResourceAsStream("/client/misc/MailInactive.png")));
        imgMail.setId("mail");
        imgMail.setFitHeight(16);
        imgMail.setFitWidth(16);
        ImageView imgBank = new ImageView(new Image(getClass().getResourceAsStream("/client/misc/HomeInactive.png")));
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
                ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/client/misc/HomeActive.png")));
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
}
