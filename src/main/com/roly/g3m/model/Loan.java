package com.roly.g3m.model;

public class Loan {

    public enum Type{
        money,
        object
    }

    public static final boolean SENS_TO_ME = true;
    public static final boolean SENS_FROM_ME = false;

    private int id;
    private String contactName;
    private boolean sens;
    private long lastModified;
    private long limitDate;
    private Type type;
    private float value;
    private String objectName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public boolean getSens() {
        return sens;
    }

    public void setSens(boolean sens) {
        this.sens = sens;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(long limitDate) {
        this.limitDate = limitDate;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value =  value;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

}
