package lk.dialog.analytics.spark.ops;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lk.dialog.analytics.spark.models.JobResponse;
import org.apache.log4j.Logger;
import org.eclipse.jetty.deploy.App;
import sun.nio.ch.ThreadPool;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Executes and fetches query results for thrift connections made to the spark database
 */
public class QueryExecutor {

    private Map<String, SparkConnection> connectionMap;
    private Map<String, Boolean> statusMap;
    private Map<String, ThreadPoolExecutor> executorMap;
    private Map<Integer, JobResponse> resultsMap;


    private Logger logger;
    public QueryExecutor() {
        connectionMap = new HashMap<>();
        statusMap = new HashMap<>();
        resultsMap = new HashMap<>();
        executorMap = new HashMap<>();

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

        //if the executor for this db is missing, create it.
        if (executorMap.get(dbName) == null) {
            executorMap.put(dbName, new ThreadPoolExecutor(1, 1,
                    AppProperties.getInstance().getExecTimeMin(), TimeUnit.MINUTES,
                    new ArrayBlockingQueue<>(AppProperties.getInstance().getQueueSize())));
        }

        //check whether the executor for this db is at full capacity
        if(executorMap.get(dbName).getQueue().remainingCapacity() == 0) {
            logger.info(String.format("Queue full for DB '%s'", id, dbName));
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


        QueryThread thread = new QueryThread(id, query, connection, dbName);
        executorMap.get(dbName).submit(thread);
        logger.debug(String.format("Query submit for db: %s ID=%d", dbName, id));
        return true;
    }


    /**
     * Fetch the results of a submitted query as a JsonElement. If the result does not
     *   exist, null will be returned.
     * @param id
     * @return
     */
    public JobResponse getResult(Integer id) {
        JobResponse data = resultsMap.get(id);
        if (data != null) {
            resultsMap.remove(id);
        }

        return data;
    }


    /**
     * Executes the actual query using the given connection.
     */
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
            try {
                JsonArray output = (JsonArray) connection.execute(query);
                JobResponse response = new JobResponse();
                if(output != null) {
                    response.setSuccess(true);
                    response.setTimedout(false);
                    response.setData(output);
                } else {
                    response.setSuccess(false);
                    response.setTimedout(true);
                }

                resultsMap.put(id, response);

            } catch (Exception e) {
                System.out.println("we got killed");
                e.printStackTrace();
                JobResponse response = new JobResponse();
                response.setSuccess(true);
                response.setTimedout(false);
                resultsMap.put(id, response);
            }
        }

        public Integer getId() {
            return id;
        }
    }



}
