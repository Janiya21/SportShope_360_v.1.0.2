package controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class EmailSenderViewController {

    @FXML
    public JFXTextField txtMailTo;

    @FXML
    private JFXTextArea txtContext;

    @FXML
    private JFXTextField txtSubject;

    @FXML
    void sendEmailOnAction(ActionEvent event) {
        if(!txtMailTo.getText().isEmpty() && !txtSubject.getText().isEmpty() && !txtContext.getText().isEmpty()){
            sendMail(txtMailTo.getText(),txtSubject.getText(),txtContext.getText());
        }
    }

    public static void sendMail(String recipient,String subject, String context){
        System.out.println("Loading....");

        Properties properties = new Properties();

        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        String myAccountMail = "dissanayakasarath198@gmail.com";
        String password = "Sarath_2001";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountMail,password);
            }
        });

        Message message = prepareMessage(session,myAccountMail,recipient,subject,context);

        try {
            assert message != null;
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("Email sent Successfully...");
    }

    private static Message prepareMessage(Session session, String myAccountMail, String recipient,String subject, String context) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(myAccountMail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(context);
            return message;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
