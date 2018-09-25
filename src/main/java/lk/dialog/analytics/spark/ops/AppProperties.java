package lk.dialog.analytics.spark.ops;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {
    private static AppProperties ourInstance = new AppProperties();

    public static AppProperties getInstance() {
        return ourInstance;
    }

    private String ipAddress;
    private int port;
    private String username;
    private String password;
    private int queueSize;
    private int execTimeMin;


    private AppProperties() {
        try (InputStream inputStream = new FileInputStream("api.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);

            ipAddress = properties.getProperty("THRIFT_IP", "thrift.server");
            port = Integer.parseInt(properties.getProperty("THRIFT_PORT", "8089"));
            username = properties.getProperty("THRIFT_USERNAME", "");
            password = properties.getProperty("THRIFT_PASSWORD", "");
            queueSize = Integer.parseInt(properties.getProperty("JOB_QUEUE_SIZE", "20"));
            execTimeMin = Integer.parseInt(properties.getProperty("TIME_PER_QUERY_MIN", "10"));



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

    public int getQueueSize() {
        return queueSize;
    }

    public int getExecTimeMin() {
        return execTimeMin;
    }
}
