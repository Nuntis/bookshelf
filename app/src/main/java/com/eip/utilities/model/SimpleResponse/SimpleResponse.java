
package com.eip.utilities.model.SimpleResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimpleResponse {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("errors")
    @Expose
    private Object errors;
    @SerializedName("meta")
    @Expose
    private Object meta;
    @SerializedName("title")
    @Expose
    private String title;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public Object getMeta() {
        return meta;
    }

    public void setMeta(Object meta) {
        this.meta = meta;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}