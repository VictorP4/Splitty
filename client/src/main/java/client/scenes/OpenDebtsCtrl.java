package client.scenes;
import client.Main;
import client.models.Debt;
import client.models.OpenDebtString;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.EmailRequestBody;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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

public class OpenDebtsCtrl implements Main.UpdatableUI {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ObservableList<Debt> debts;
    @FXML
    private Accordion debtsOverview;
    private Event event;
    private OpenDebtString strings;
    private String lang;

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
     * updates the UI
     */
    @Override
    public void updateUI() {
        if(this.strings==null) this.strings = new OpenDebtString();

        if(Main.getLocalizedString("accHolder").equals("Account Holder:")) lang="en";
        else lang="nl";
        strings.setBankInfo(Main.getLocalizedString("transferTo"));
        strings.setBankAccount(Main.getLocalizedString("accHolder"));
        strings.setEmail(Main.getLocalizedString("emailHolder"));
        strings.setSendReminder(Main.getLocalizedString("reminder"));
        strings.setMarkReceived(Main.getLocalizedString("markReceived"));



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
        if(this.lang==null) lang="en";
        this.event=event;
        if(this.strings==null) this.strings = new OpenDebtString();
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
        Text text1 = null;
        if(debt.getPersonOwed().getIban()!=null&&debt.getPersonOwed().getBic()!=null) {
            text1 = new Text("Bank information available, transfer the money to:");
            text1.setLayoutX(14.0);
            text1.setLayoutY(27.0);
            text1.setStrokeType(StrokeType.OUTSIDE);
            text1.setStrokeWidth(0.0);
            text1.textProperty().bind(strings.bankInfo());
            if(strings.getBankInfo()==null) strings.setBankInfo("Bank information available, transfer the money to:");

            Text text2 = new Text("Account Holder: " + debt.getPersonOwed().getName());
            text2.textProperty().bind(strings.bankAccountProperty());
            if(strings.getBankAccount()==null) strings.setBankAccount("Account Holder: ");
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

            Text text5 = new Text("Email configured:");
            text5.textProperty().bind(strings.email());
            if(strings.getEmail()==null) strings.setEmail("Email configured:");
            text5.setLayoutX(14.0);
            text5.setLayoutY(108.0);
            text5.setStrokeType(StrokeType.OUTSIDE);
            text5.setStrokeWidth(0.0);

            Button emailB = new Button("Send reminder");
            emailB.textProperty().bind(strings.sendReminder());
            if(strings.getSendReminder()==null) strings.setSendReminder("Send reminder");
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
                    Response response = server.sendReminder(new EmailRequestBody(message, String.valueOf(debt.getAmount())));
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
            if(debt.getPersonOwed().getEmail().isEmpty()) emailB.setDisable(true);
            emailB.setLayoutX(124);
            emailB.setLayoutY(91);
            emailB.setMnemonicParsing(false);
            tempAP.getChildren().addAll(text1, text2, text3, text4, text5, emailB);
        }
        else{
            text1 = new Text("Bank information not available");
            text1.textProperty().bind(strings.bankInfoNA());
            if(strings.getBankInfoNA()==null) strings.setBankInfoNA("Bank information not available");
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

        if(this.lang.equals("en")) text.setText(debt.getPersonInDebt().getName()+" gives "+ debt.getAmount()+" euro to "+ debt.getPersonOwed().getName());
        else text.setText(debt.getPersonInDebt().getName()+" geeft "+ debt.getAmount()+" euro ann "+ debt.getPersonOwed().getName());
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
        Button markReceived = new Button("Mark Received");
        markReceived.textProperty().bind(strings.markReceived());
        if(strings.getMarkReceived()==null) strings.setMarkReceived("Mark Received");
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
                expense.setInvolvedParticipants(new ArrayList<>(List.of(debt.getPersonOwed())));
                expense.setAmount(debt.getAmount());
                expense.setTitle("Debt repayment");
                server.addExpense(expense, event1.getId());
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
}
