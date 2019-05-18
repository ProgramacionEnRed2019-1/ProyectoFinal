package controller;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.User;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Properties;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private TextField name,surname,email,id,value;
    @FXML
    private DatePicker init,end;
    @FXML
    private ImageView qr;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void generateQR(ActionEvent event) throws WriterException, IOException {
        InputStream inputStream = getQRStream();
        qr.setImage(new Image(inputStream));
    }

    private InputStream getQRStream() throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(id.getText(), BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return new ByteArrayInputStream(pngOutputStream.toByteArray());
    }

    private void modifyUser(String add) throws IOException {
        User user = new User(name.getText(), surname.getText(), email.getText(), id.getText(), getDate(init)
                , getDate(end), Double.parseDouble(value.getText()));
        String response = request(new Gson().toJson(user), add);
        sendQR(email.getText());
    }

    public void addUser(ActionEvent event) throws IOException {
        modifyUser("add");
    }

    public void update(ActionEvent event) throws IOException {
        modifyUser("update");
    }

    private void sendQR(String userEmail) {
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
                adjunto.setDataHandler(new DataHandler(new ByteArrayDataSource(getQRStream(),"image/jpeg")));
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
            } catch (MessagingException | IOException | WriterException e) {
                emailError();
            }
        }
    }

    private void emailError() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Email not valid");
        errorAlert.setContentText("The email is invalid, message not sent");
        errorAlert.showAndWait();
    }

    private java.util.Date getDate(DatePicker picker) {
        return Date.from(Instant.from(picker.getValue().atStartOfDay(ZoneId.systemDefault())));
    }

    private String request(String params, String resource) throws IOException {
        URL url = new URL("http://localhost:8080/gym/user/"+resource);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(params);
        writer.flush();

        int httpCode = connection.getResponseCode();
        System.out.println("httpCode = " + httpCode);

        InputStream is = connection.getInputStream();

        byte[] buf = new byte[1024];
        int leidos = 0;
        ByteArrayOutputStream baos= new ByteArrayOutputStream();

        while ((leidos = is.read(buf)) != -1){
            baos.write(buf,0,leidos);
        }
        is.close();
        baos.close();
        return new String(baos.toByteArray());
    }
}
