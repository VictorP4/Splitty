package client.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * classes used for easy translation for the open debt scene
 */
public class OpenDebtString {
    private final StringProperty bankAccount = new SimpleStringProperty();

    private final StringProperty markReceived = new SimpleStringProperty();
    private final StringProperty sendReminder = new SimpleStringProperty();
    private final StringProperty bankInfo = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty bankInfoNA = new SimpleStringProperty();

    /**
     *
     * @return string porpery of bankAccount
     */
    public StringProperty bankAccountProperty() {
        return this.bankAccount;
    }

    /**
     *
     * @return the string of bank account
     */
    public String getBankAccount(){
        return this.bankAccount.get();
    }

    /**
     * Sets the string of bankAccount
     * @param value new string for bankAccount
     */
    public void setBankAccount(String value){
        this.bankAccount.set(value);
    }
    public StringProperty markReceived(){
        return this.markReceived;
    }
    public String getMarkReceived(){
        return this.markReceived.get();
    }
    public void setMarkReceived(String value){
        this.markReceived.set(value);
    }
    public StringProperty sendReminder(){
        return this.sendReminder;
    }
    public String getSendReminder(){
        return this.sendReminder.get();
    }
    public void setSendReminder(String value){
        this.sendReminder.set(value);
    }
    public StringProperty bankInfo(){
        return this.bankInfo;
    }
    public String getBankInfo(){
        return this.bankInfo.get();
    }
    public void setBankInfo(String value){
        this.bankInfo.set(value);
    }
    public StringProperty email(){
        return this.email;
    }
    public String getEmail(){
        return this.email.get();
    }
    public void setEmail(String value){
        this.email.set(value);
    }
    public StringProperty bankInfoNA(){
        return this.bankInfoNA;
    }
    public String getBankInfoNA(){
        return this.bankInfoNA.get();
    }
    public void setBankInfoNA(String value){
        this.bankInfoNA.set(value);
    }


}
