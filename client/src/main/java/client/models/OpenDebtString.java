package client.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OpenDebtString {
    private final StringProperty bankAccount = new SimpleStringProperty();
    public StringProperty bankAccountProperty() {
        return this.bankAccount;
    }
    public String getBankAccount(){
        return this.bankAccount.get();
    }
    public void setBankAccount(String value){
        this.bankAccount.set(value);
    }
}
