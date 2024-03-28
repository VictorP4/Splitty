package commons;

import java.util.List;

public class EmailRequestBody {
    private List<String> emailAddresses;
    private String code;

    /**
     * Creates a new empty EmailRequestBody.
     */
    public EmailRequestBody() {
        // Default constructor required by Jackson for deserialization
    }

    /**
     * Creates a new EmailRequestBody with a list of email addresses and a code.
     *
     * @param emailAddresses The email addresses in the email
     * @param code the code
     */
    public EmailRequestBody(List<String> emailAddresses, String code) {
        this.emailAddresses = emailAddresses;
        this.code = code;
    }

    /**
     * Gets the email addresses.
     *
     * @return the email addresses
     */
    public List<String> getEmailAddresses() {
        return emailAddresses;
    }

    /**
     * Sets the email addresses.
     *
     * @param emailAddresses the email addresses to be set.
     */
    public void setEmailAddresses(List<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    /**
     * Gets the code of the email request body.
     *
     * @return the code of the email request body.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code of the email request body.
     *
     * @param code the code the email request body has to be set to.
     */
    public void setCode(String code) {
        this.code = code;
    }
}
