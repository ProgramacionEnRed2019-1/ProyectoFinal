package connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPRequest {

    public static String postRequest(String params, String resource) throws IOException {
        URL url = new URL("http://localhost:8080/gym/gym/user/"+resource);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        System.out.println(params);
        writer.write(params);
        writer.flush();

        int httpCode = connection.getResponseCode();
        System.out.println("httpCode = " + httpCode);

        InputStream is = connection.getInputStream();

        byte[] buf = new byte[1024];
        int leidos;
        ByteArrayOutputStream baos= new ByteArrayOutputStream();

        while ((leidos = is.read(buf)) != -1){
            baos.write(buf,0,leidos);
        }
        is.close();
        baos.close();
        return new String(baos.toByteArray());
    }

}
