package lk.dialog.analytics.spark.ops;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;

import java.sql.*;

import static lk.dialog.analytics.spark.Application.ourInstance;

public class SparkConnection {

    private Connection connection;
    private Logger logger;

    public SparkConnection(String database) throws SQLException, ClassNotFoundException {

        Class.forName("org.apache.hive.jdbc.HiveDriver");
        connection = DriverManager.getConnection(
                String.format("jdbc:hive2://%s:%d/%s", ourInstance.getIpAddress(), ourInstance.getPort(), database),
                ourInstance.getUsername(),
                ourInstance.getPassword());

        connection.isClosed();

        if (connection.isClosed()) {
            System.out.println("Not connected to Spark shell");
        } else {
            System.out.println("Connected to Spark shell");
        }


        logger = Logger.getLogger(getClass());
    }


    public JsonElement execute(String query) {
        JsonArray result = new JsonArray();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            String[] columns = new String[metaData.getColumnCount()];
            for (int i = 1; i <= columns.length; i++) {
                columns[i - 1] = metaData.getColumnName(i);
            }

            while (rs.next()) {
                JsonObject item = new JsonObject();
                for (String col : columns) {
                    item.addProperty(col, rs.getString(col));
                }
                result.add(item);

            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }
}
