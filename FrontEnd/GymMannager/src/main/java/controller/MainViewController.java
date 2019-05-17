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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
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
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(id.getText(), BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(pngOutputStream.toByteArray());

        qr.setImage(new Image(inputStream));
    }

    public void addUser(ActionEvent event) throws IOException {
        User user = new User(name.getText(),surname.getText(),email.getText(),id.getText(),getDate(init)
                ,getDate(end),Double.parseDouble(value.getText()));
        String response = request(new Gson().toJson(user),"add");
    }

    public void update(ActionEvent event) throws IOException {
        User user = new User(name.getText(),surname.getText(),email.getText(),id.getText(),getDate(init)
                ,getDate(end),Double.parseDouble(value.getText()));
        String response = request(new Gson().toJson(user),"update");
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
