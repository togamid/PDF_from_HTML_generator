package email;

import objects.TestingResult;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.DataSource;
import java.util.List;
import java.util.Properties;

public class Email {

    private static Session session;
    private static final String subject = "Ihr Zertifikat ";

    private static final String text = "Sehr geehrter Herr/Frau {lastname}, \n\nIhr Selbsttest am {date} war {result}. " +
            "\nIm Anhang finden Sie Ihr Testat. \n\nMit freundlichen Grüßen\nTestzentrum Hochschule Ansbach"; //TODO: beides aus einer Config-datei laden

    public static void initSession(String username, String password, String host){

        //Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username,password);
                    }
                });
    }

    public static void sendEmail(TestingResult result) throws MessagingException{
        String to = result.email;

        MimeMessage message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(subject);
            message.setFrom("testzentrum@hs-ansbach.de");

            BodyPart mailContent = new MimeBodyPart();
            mailContent.setText(processEmailText(text, result));

            BodyPart pdf = new MimeBodyPart();
            DataSource source = new FileDataSource(result.getPDFLocation());
            pdf.setDataHandler(new DataHandler(source));
            String filename = "Zertifikat_"+ result.lastname + "_" + result.firstname +".pdf";
            pdf.setFileName(filename );
            pdf.setHeader("Content-Type", "application/pdf;charset=utf-8");


            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mailContent);
            multipart.addBodyPart(pdf);

            message.setContent(multipart);

            //send message
            Transport.send(message);
    }

    public static int sendEmails(List<TestingResult>results){
        int i = 0;
        for(TestingResult result : results) {
            try {
                sendEmail(result);
                i++;
            } catch (Exception e)
            {
                System.out.println("Could not send message at " + result.email);
            }
        }
        return i;
    }


    private static String processEmailText(String text, TestingResult result){
        String output = text.replace("{lastname}", result.lastname);
        output = output.replace("{firstname}", result.firstname);
        output = output.replace("{date}", result.date);
        output = output.replace("{result}", result.resultLong);
        return output;
    }
}
