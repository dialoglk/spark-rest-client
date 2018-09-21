package lk.dialog.analytics.spark.ops;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {

    private String ipAddress;
    private int port;
    private String username;
    private String password;
    private String listenPort;


    public AppProperties() {
        try (InputStream inputStream = new FileInputStream("api.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);

            ipAddress = properties.getProperty("THRIFT_IP", "10.44.209.44");
            port = Integer.parseInt(properties.getProperty("THRIFT_PORT", "8089"));
            username = properties.getProperty("THRIFT_USERNAME", "");
            password = properties.getProperty("THRIFT_PASSWORD", "");
            listenPort = properties.getProperty("API_LISTEN_PORT", "");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getListenPort() {
        return listenPort;
    }
}