package com.example.azramahmic.tinderapp.data.api.base_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Error {

    @SerializedName("target")
    @Expose
    private Target target;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("property")
    @Expose
    private String property;
    @SerializedName("children")
    @Expose
    private List<Object> children = null;
    @SerializedName("constraints")
    @Expose
    private Constraints constraints;

    public Target getTarget() {
        return target;
    }

    public String getValue() {
        return value;
    }

    public String getProperty() {
        return property;
    }

    public List<Object> getChildren() {
        return children;
    }

    public Constraints getConstraints() {
        return constraints;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setChildren(List<Object> children) {
        this.children = children;
    }

    public void setConstraints(Constraints constraints) {
        this.constraints = constraints;
    }
}
