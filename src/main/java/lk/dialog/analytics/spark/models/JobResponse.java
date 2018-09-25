package lk.dialog.analytics.spark.models;

import com.google.gson.JsonArray;

public class JobResponse {
    private boolean success;
    private boolean timedout;
    private JsonArray data;
    private Integer id;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isTimedout() {
        return timedout;
    }

    public void setTimedout(boolean timedout) {
        this.timedout = timedout;
    }

    public JsonArray getData() {
        return data;
    }

    public void setData(JsonArray data) {
        this.data = data;
    }
}
