package controller;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import view.MainWindow;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainController {

    private MainWindow view;
    private Webcam webcam;

    public MainController(MainWindow view){
        this.view = view;
        webcam = Webcam.getDefault();
        webcam.open();
        this.init();
    }

    private void init() {
        view.setPanel(new WebcamPanel(webcam));
        new Thread(this::run).start();
    }

    private void run() {
        while (true) {
            BufferedImage bufferedImage = webcam.getImage();
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                Result result = new MultiFormatReader().decode(bitmap);
                request(result.getText());
            } catch (NotFoundException | IOException ignored) {
            }
        }
    }

    private void request(String text) throws IOException {
        URL url = new URL("http://localhost:8080/gym/user/exist");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        String params = "id="+text;
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
        if (new String(baos.toByteArray()).equals("true")){
            //TODO
        }
    }
}
