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
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

public class MainController {

    private MainWindow view;
    private Webcam webcam;

    //ARDUINO CONNECTION
    private OutputStream output = null;
    SerialPort serialPort;
    private final String PORT_NAME = "COM3";
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 9600;
    private static final int ERROR =1;

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
            enviarDatos("1");
        }
    }


    public void arduinoConnection() {

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();

            if (PORT_NAME.equals(currPortId.getName())) {
                portId = currPortId;
                break;
            }
        }

        if (portId == null) {

            System.exit(ERROR);
            return;
        }

        try {

            serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            output = serialPort.getOutputStream();

        } catch (Exception e) {

            System.exit(ERROR);
        }

    }

    private void enviarDatos(String data) {

        try {
            output.write(data.getBytes());

        } catch (IOException e) {

            System.exit(ERROR);
        }
    }
}
