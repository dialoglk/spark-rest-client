package lk.dialog.analytics.spark.ops;

import com.google.gson.JsonElement;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Executes and fetches query results for thrift connections made to the spark database
 */
public class QueryExecutor {

    private Map<String, SparkConnection> connectionMap;
    private Map<String, Boolean> statusMap;
    private Map<Integer, JsonElement> resultsMap;

    private Logger logger;
    public QueryExecutor() {
        connectionMap = new HashMap<>();
        statusMap = new HashMap<>();
        resultsMap = new HashMap<>();

        logger = Logger.getLogger(getClass());
    }


    /**
     * Submits a query to be executed in a selected spark connection. A connection will be
     *   created for each database name. Only one query is allowed per connection.
     * @param dbName
     * @param query
     * @param id Query id to fetch results later
     * @return
     */
    public boolean submit(String dbName, String query, Integer id) {
        System.out.println(String.format("submitting a query(id=%d) for DB '%s'", id, dbName));
        if (statusMap.get(dbName) != null && statusMap.get(dbName)) {
            logger.info(String.format("Already running a query(id=%d) for DB '%s'", id, dbName));
            return false;
        }


        //check if a connection is available in the map
        if (connectionMap.get(dbName) == null) {
            try {
                connectionMap.put(dbName, new SparkConnection(dbName));
            } catch (SQLException | ClassNotFoundException e) {
                logger.warn(String.format("Could not create a connection to DB '%s'", dbName));
                return false;
            }
        }

        //fetch the connection from the connections map and set the query running state to true
        SparkConnection connection = connectionMap.get(dbName);
        statusMap.put(dbName, true);

        new Thread(new QueryThread(id, query, connection, dbName)).start();
        logger.debug(String.format("Query submit for db: %s ID=%d", dbName, id));
        return true;
    }


    /**
     * Fetch the results of a submitted query as a JsonElement. If the result does not
     *   exist, null will be returned.
     * @param id
     * @return
     */
    public JsonElement getResult(Integer id) {
        JsonElement data = resultsMap.get(id);
        if (data != null) {
            resultsMap.remove(id);
        }

        return data;
    }


    private class QueryThread implements Runnable {

        private Integer id;
        private String query;
        private SparkConnection connection;
        private String dbName;

        public QueryThread(Integer id, String query, SparkConnection connection, String dbName) {
            this.id = id;
            this.query = query;
            this.connection = connection;
            this.dbName = dbName;
        }

        @Override
        public void run() {
            JsonElement output = connection.execute(query);
            resultsMap.put(id, output);

            //free the resource after the query is done
            statusMap.remove(dbName);
        }
    }



}
