package controller;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import connection.Email;
import connection.HTTPRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
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
        try {
            id.setText(SelectionScreenController.getId());
            id.setEditable(false);
            InputStream inputStream = getQRStream();
            qr.setImage(new Image(inputStream));
            if (SelectionScreenController.getMode() == SelectionScreenController.UPDATE_MODE){
                try{
                    User user = new User("a","b","c",id.getText(),new Date(),new Date(),0);
                    String toJson = new Gson().toJson(user);
                    String json = HTTPRequest.postRequest(toJson, "user");
                    user = new Gson().fromJson(json,User.class);
                    name.setText(user.getName());
                    surname.setText(user.getSurname());
                    email.setText(user.getEmail());
                }catch (Exception e){
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("id isn't valid");
                    errorAlert.setContentText("The id is invalid, please create a new user");
                    errorAlert.showAndWait();
                    goBack();
                }
            }
        } catch (WriterException | IOException ignored) {}

    }

    public void back(ActionEvent event) throws IOException {
        goBack();
    }

    public void modify(ActionEvent event) throws IOException, WriterException {
        modifyUser(SelectionScreenController.getMode() == SelectionScreenController.REGISTER_MODE?"add":"update");
    }

    private InputStream getQRStream() throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(id.getText(), BarcodeFormat.QR_CODE, 300, 300);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return new ByteArrayInputStream(pngOutputStream.toByteArray());
    }

    private void modifyUser(String add) throws IOException, WriterException {
        User user = new User(name.getText(), surname.getText(), email.getText(), id.getText(), getDate(init)
                , getDate(end), Double.parseDouble(value.getText()));
        String response = HTTPRequest.postRequest(new Gson().toJson(user), add);
        Email.sendQR(email.getText(),getQRStream());
        goBack();
    }

    private void goBack() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/SelectionScreen.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        ((Stage) id.getScene().getWindow()).setScene(scene);
    }

    private Date getDate(DatePicker picker) {
        return Date.from(Instant.from(picker.getValue().atStartOfDay(ZoneId.systemDefault())));
    }

}
