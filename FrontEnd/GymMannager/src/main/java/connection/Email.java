package connection;

import com.google.zxing.WriterException;
import javafx.scene.control.Alert;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Email {

    public static void sendQR(String userEmail, InputStream imageStream) {
        if(!userEmail.contains("@") || userEmail.contains(" ") || !userEmail.contains(".")) emailError();
        else {
            Properties props = new Properties();
            // Nombre del host de correo, es smtp.gmail.com
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            // TLS si está disponible
            props.setProperty("mail.smtp.starttls.enable", "true");
            // Puerto de gmail para envio de correos
            props.setProperty("mail.smtp.port","587");
            // Nombre del usuario
            props.setProperty("mail.smtp.user", "gymprograred2019@gmail.com");
            // Si requiere o no usuario y password para conectarse.
            props.setProperty("mail.smtp.auth", "true");
            Session session = Session.getDefaultInstance(props);
            session.setDebug(true);
            BodyPart texto = new MimeBodyPart();
            try {
                texto.setText("Con este QR podra entrar al gym");
                BodyPart adjunto = new MimeBodyPart();
                adjunto.setDataHandler(new DataHandler(new ByteArrayDataSource(imageStream,"image/jpeg")));
                MimeMultipart multiParte = new MimeMultipart();
                multiParte.addBodyPart(texto);
                multiParte.addBodyPart(adjunto);
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress("gymprograred2019@gmail.com"));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
                message.setSubject("Gym QR");
                message.setContent(multiParte);
                Transport t = session.getTransport("smtp");
                t.connect("gymprograred2019@gmail.com","prograred-2019");
                t.sendMessage(message,message.getAllRecipients());
                t.close();
            } catch (MessagingException | IOException e) {
                emailError();
            }
        }
    }

    private static void emailError() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Email not valid");
        errorAlert.setContentText("The email is invalid, message not sent");
        errorAlert.showAndWait();
    }

}
