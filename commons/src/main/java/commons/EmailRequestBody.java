package commons;

import java.util.List;

public class EmailRequestBody {
    private List<String> emailAddresses;
    private String code;

    public EmailRequestBody(List<String> emailAddresses, String code) {
        this.emailAddresses = emailAddresses;
        this.code = code;
    }

    public List<String> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
