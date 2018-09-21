/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lk.dialog.analytics.spark.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lk.dialog.analytics.spark.models.JobResponse;
import lk.dialog.analytics.spark.ops.QueryExecutor;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Base64;
import java.util.logging.LogManager;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 0.1
 */
@Path("/")
public class SparkService {

    private QueryExecutor executor;
    private Logger logger;
    public SparkService() {
        executor = new QueryExecutor();
        logger = Logger.getLogger(getClass());

        logger.info("Ready for execution");
    }

    @GET
    @Path("/query/{database}")
    @Produces("application/json")
    public String get(@PathParam("database")String database, @QueryParam("q")String query) {
        System.out.println("get called");

        Base64.Decoder decoder = Base64.getDecoder();
        String given = new String(decoder.decode(query));
        int id = (int) (Math.random() * 10000);
        boolean result = executor.submit(database, given, id);

        JobResponse response = new JobResponse();
        response.setSuccess(result);
        if (result) {
            response.setId(id);
        }
        return new Gson().toJson(response);
    }

    @GET
    @Path("/status/{id}")
    @Produces("application/json")
    public String get(@PathParam("id")Integer id) {

        JsonElement data = executor.getResult(id);
        if (data == null) {
            JobResponse response = new JobResponse();
            response.setSuccess(false);
            return new Gson().toJson(response);
        }

        return new Gson().toJson(data);
    }
}
