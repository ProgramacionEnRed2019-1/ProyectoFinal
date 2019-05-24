package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SelectionScreenController implements Initializable {

    @FXML
    private TextField userID;
    private static String id;
    private static int mode;

    public static final int REGISTER_MODE = 1;
    public static final int UPDATE_MODE = 2;

    public static String getId() {
        return id;
    }

    public static int getMode() {
        return mode;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void registerScreen(ActionEvent event) throws IOException {
        launch(event, REGISTER_MODE);
    }

    public void updateScreen(ActionEvent event) throws IOException {
        launch(event, UPDATE_MODE);
    }

    private void launch(ActionEvent event, int registerMode) throws IOException {
        if (userID.getText() != null && !userID.getText().equals("")) {
            id = userID.getText();
            mode = registerMode;
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
            Scene scene = new Scene(root, 1280, 720);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ingrese la cedula del usuario");
            alert.show();
        }
    }
}
