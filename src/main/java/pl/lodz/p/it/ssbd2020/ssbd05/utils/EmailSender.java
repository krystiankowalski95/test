package pl.lodz.p.it.ssbd2020.ssbd05.utils;

import com.sun.mail.smtp.SMTPTransport;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2020.ssbd05.exceptions.AppBaseException;

import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;

/**
 * Klasa odpowiedzialna za wysyłanie maili z naszego systemu do użytkowników
 */
@Log
public class EmailSender {

    private final Properties emailProperties;

    /**
     * Konstruktor bezparametrowy klasy EmailSender
     *
     * @throws AppBaseException Wyjątek aplikacyjny
     */
    public EmailSender() throws AppBaseException {
        emailProperties = ResourceBundles.loadProperties("config.email.properties");
    }

    /**
     * Wyślij mail do potwierdzenia konta. Wysyłany po zarejestrowaniu.
     *
     * @param mail  mail
     * @param token token
     */
    public void sendRegistrationEmail(String mail, String token)  {
        String subject = ResourceBundles.getTranslatedText("mail.account.confirm.subject");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String link = request.getRequestURL()
                .substring(0, (request.getRequestURL().length() - request.getServletPath().length())).concat("/confirmAccount.xhtml?token=");
        String body = "<a href=\"" + link + token + "\">"+ ResourceBundles.getTranslatedText("mail.account.confirm.body") +"</a>";
        new Thread(() -> {
            sendEmail(mail, subject, body);
            return;
        }).start();
    }

    /**
     * Wyślij email informujący o zablokowaniu konta użytkownika
     *
     * @param mail mail
     */
    public void sendBlockedAccountEmail(String mail) {
        String subject = ResourceBundles.getTranslatedText("mail.account.blocked.subject");
        String body = ResourceBundles.getTranslatedText("mail.account.blocked");

        new Thread(() -> {
            sendEmail(mail, subject, body);
            return;
        }).start();
    }

    /**
     * Wyślij email informujący o odblokowaniu konta użytkownika
     *
     * @param mail mail
     */
    public void sendUnlockedAccountEmail(String mail) {
        String subject = ResourceBundles.getTranslatedText("mail.account.unlocked");
        String body = ResourceBundles.getTranslatedText("mail.account.unlocked");

        new Thread(() -> {
            sendEmail(mail, subject, body);
            return;
        }).start();
    }

    /**
     * Wyślij email informujący o potwierdzeniu konta użytkownika
     *
     * @param mail mail
     */
    public void sendConfirmedAccountEmail(String mail) {
        String subject = ResourceBundles.getTranslatedText("messages.account.confirmed");
        String body = ResourceBundles.getTranslatedText("messages.account.confirmed");

        new Thread(() -> {
            sendEmail(mail, subject, body);
            return;
        }).start();
    }

    /**
     * Metoda odpowiedzialna za wysyłanie maila po zalogowaniu na konto posiadające poziom dostępu Administrator
     * @param mail Adres email użytkownika
     * @param date Data uwierzytelnienia
     * @param ip   Adres logiczny, z którego nastąpiło uwierzytelnienie
     */
    public void sendMailToAdmin(String mail, LocalDateTime date, String ip) {
        String subject = ResourceBundles.getTranslatedText("mail.admin.login.subject");
        StringBuilder body = new StringBuilder();
        body.append(ResourceBundles.getTranslatedText("mail.admin.login.subject"))
                .append(", ")
                .append(ResourceBundles.getTranslatedText("mail.admin.login.date"))
                .append(" ").append(DateFormatter.formatDate(date))
                .append(", ")
                .append(ResourceBundles.getTranslatedText("mail.admin.login.ip"))
                .append(" ").append(ip);

        new Thread(() -> {
            sendEmail(mail, subject, body.toString());
            return;
        }).start();
    }

    /**
     * Wyślij email do zresetowania hasła konta użytkownika
     *
     * @param mail  mail
     * @param token token
     */
    public void sendPasswordResetEmail(String mail, String token) {
        String subject = ResourceBundles.getTranslatedText("page.resetpassword.title");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String link = request.getRequestURL()
                .substring(0, (request.getRequestURL().length() - request.getServletPath().length())).concat("/changeResettedPassword.xhtml?token=");
        String body = "<a href=\"" + link + token + "\">"+ ResourceBundles.getTranslatedText("mail.resetpassword.body") +"</a>";

        new Thread(() -> {
            sendEmail(mail, subject, body);
            return;
        }).start();
    }
    /**
     * Metoda wysyłająca mail z wiadomością o zmianie statusu rezerwacji.
     *
     * @param mail  mail
     * @param reservationNumber numer rezerwacji
     * @param newStatus status rezerwacji
     */
    public void sendChangingReservationStatusEmail(String mail, String reservationNumber, String newStatus)  {
        String subject = ResourceBundles.getTranslatedText("mail.reservation.status.change.subject");
        StringBuilder body = new StringBuilder();
        body.append(ResourceBundles.getTranslatedText("mail.reservation.status.change.number"))
                .append(" ")
                .append(reservationNumber)
                .append(" ")
                .append(ResourceBundles.getTranslatedText("mail.reservation.status.change.status"))
                .append(" ")
                .append(newStatus);

        new Thread(() -> {
            sendEmail(mail, subject, body.toString());
            return;
        }).start();
    }

    /**
     * Metoda odpowiadająca bezpośrednio za wysłanie maila
     *
     * @param mail    mail
     * @param subject subject
     * @param body    body
     */
    private void sendEmail(String mail, String subject, String body) {
        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", emailProperties.getProperty("host"));
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", emailProperties.getProperty("port"));
        prop.put("mail.smtp.ssl.trust", emailProperties.getProperty("host"));
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        try {
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(body, "UTF-8", "html");
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(mimeBodyPart);

            msg.setFrom(new InternetAddress(emailProperties.getProperty("username")));
            msg.setContent(mimeMultipart);
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail, false));
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
            t.connect(emailProperties.getProperty("host"), emailProperties.getProperty("username"), emailProperties.getProperty("password"));
            t.sendMessage(msg, msg.getAllRecipients());

            if(t.getLastReturnCode() != Integer.parseInt(emailProperties.getProperty("code")))
                log.warning("An error occurred while sending email");

            t.close();

        } catch (MessagingException e) {
            log.warning("An error occurred while sending email " + e.getMessage() + " " + LocalDateTime.now());
        }
    }
}

