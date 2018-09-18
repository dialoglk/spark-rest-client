package lk.dialog.analytics.spark.ops;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.*;

public class SparkConnection {

    private Connection connection;

    public SparkConnection(String database) {
        AppProperties properties = AppProperties.getInstance();

        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            connection = DriverManager.getConnection(
                    String.format("jdbc:hive2://%s:%d/%s", properties.getIpAddress(), properties.getPort(), database),
                    "",
                    "");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public JsonElement execute(String query) {
        System.out.println("trying to execute " + query);
        JsonArray result = new JsonArray();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            System.out.println("query executed");
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
