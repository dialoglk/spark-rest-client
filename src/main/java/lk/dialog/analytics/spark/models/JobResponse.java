package lk.dialog.analytics.spark.models;

public class JobResponse {
    private boolean success;
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
}
