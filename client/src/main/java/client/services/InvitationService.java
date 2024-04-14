package client.services;

import client.utils.EmailUtils;
import commons.EmailRequestBody;

import java.util.ArrayList;

public class InvitationService {

    /**
     * Constructs a new invitationService.
     */
    public InvitationService() {}

    /**
     * Gets the email from the string array and returns them as an arraylist.
     *
     * @param lines the lines of different emails passed in
     * @return an arraylist containing all emails
     */
    public ArrayList<String> getEmails(String[] lines) {
        ArrayList<String> emails = new ArrayList<>();
        for (String line : lines) {
            String email = line.trim();
            if (!email.isEmpty()) {
                emails.add(email);
            }
        }
        return emails;
    }

    /**
     * Sends an invitation to the emails passed into the string array.
     *
     * @param lines The emails where we have to send an invitation
     * @param inviteCode the invite code of the event
     * @param email the email the invitation needs to be sent from
     * @param userPass the app password to allow for sending with a users email
     * @param urlConfig the string containing the server configuration of the user
     */
    public void sendInvites(String[] lines, String inviteCode, String email, String userPass, String urlConfig){
        ArrayList<String> emails = getEmails(lines);
        EmailRequestBody requestBody = new EmailRequestBody(emails, inviteCode);
        EmailUtils emailUtils = new EmailUtils();
        emailUtils.sendInvites(requestBody.getEmailAddresses(), requestBody.getCode(), email,userPass, urlConfig);
    }
}
