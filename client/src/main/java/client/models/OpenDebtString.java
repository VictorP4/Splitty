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
     * Returns the string property for bankAccount
     * @return StringProperty of bankAccount
     */
    public StringProperty bankAccountProperty() {
        return this.bankAccount;
    }

    /**
     * Returns the strings content of bankAccount
     * @return the string of bankAccount
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

    /**
     * Returns the string property for markReceived
     * @return StringProperty of markReceived
     */
    public StringProperty markReceived(){
        return this.markReceived;
    }
    /**
     * Returns the strings content of markReceived
     * @return the string of markReceived
     */
    public String getMarkReceived(){
        return this.markReceived.get();
    }
    /**
     * Sets the string of markReceived
     * @param value new string for markReceived
     */
    public void setMarkReceived(String value){
        this.markReceived.set(value);
    }
    /**
     * Returns the string property for sendReminder
     * @return StringProperty of sendReminder
     */
    public StringProperty sendReminder(){
        return this.sendReminder;
    }

    /**
     * Returns the strings content of sendReminder
     * @return the string of sendReminder
     */
    public String getSendReminder(){
        return this.sendReminder.get();
    }
    /**
     * Sets the string of sendReminder
     * @param value new string for sendReminder
     */
    public void setSendReminder(String value){
        this.sendReminder.set(value);
    }
    /**
     * Returns the string property for bankInfo
     * @return StringProperty of bankInfo
     */
    public StringProperty bankInfo(){
        return this.bankInfo;
    }
    /**
     * Returns the strings content of bankInfo
     * @return the string of bankInfo
     */
    public String getBankInfo(){
        return this.bankInfo.get();
    }
    /**
     * Sets the string of bankInfo
     * @param value new string for bankInfo
     */
    public void setBankInfo(String value){
        this.bankInfo.set(value);
    }
    /**
     * Returns the string property for email
     * @return StringProperty of email
     */
    public StringProperty email(){
        return this.email;
    }
    /**
     * Returns the strings content of email
     * @return the string of email
     */
    public String getEmail(){
        return this.email.get();
    }
    /**
     * Sets the string of email
     * @param value new string for email
     */
    public void setEmail(String value){
        this.email.set(value);
    }
    /**
     * Returns the string property for bankInfoNA
     * @return StringProperty of bankInfoNA
     */
    public StringProperty bankInfoNA(){
        return this.bankInfoNA;
    }
    /**
     * Returns the strings content of bankInfoNA
     * @return the string of bankInfoNA
     */
    public String getBankInfoNA(){
        return this.bankInfoNA.get();
    }
    /**
     * Sets the string of bankInfoNA
     * @param value new string for bankInfoNA
     */
    public void setBankInfoNA(String value){
        this.bankInfoNA.set(value);
    }


}
