package client.services;

import client.utils.EmailUtils;
import commons.EmailRequestBody;

import java.util.ArrayList;

public class InvitationService {
    public InvitationService() {}
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
    public void sendInvites(String[] lines, String inviteCode, String email, String userPass, String urlConfig){
        ArrayList<String> emails = getEmails(lines);
        EmailRequestBody requestBody = new EmailRequestBody(emails, inviteCode);
        EmailUtils emailUtils = new EmailUtils();
        emailUtils.sendInvites(requestBody.getEmailAddresses(), requestBody.getCode(), email,userPass, urlConfig);
    }
}
