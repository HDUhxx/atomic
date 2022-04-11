package com.example.rdb.Dao;

public class Person {
    private String BASE_URI;
    private String DATA_PATH;
    private String DB_COLUMN_PERSON_ID;
    private String DB_COLUMN_NAME;
    private String DB_COLUMN_GENDER;
    private String DB_COLUMN_AGE;

    public Person() { }

    public Person(String BASE_URI, String DATA_PATH, String DB_COLUMN_PERSON_ID, String DB_COLUMN_NAME, String DB_COLUMN_GENDER, String DB_COLUMN_AGE) {
        this.BASE_URI = BASE_URI;
        this.DATA_PATH = DATA_PATH;
        this.DB_COLUMN_PERSON_ID = DB_COLUMN_PERSON_ID;
        this.DB_COLUMN_NAME = DB_COLUMN_NAME;
        this.DB_COLUMN_GENDER = DB_COLUMN_GENDER;
        this.DB_COLUMN_AGE = DB_COLUMN_AGE;
    }

    public String getBASE_URI() {
        return BASE_URI;
    }

    public void setBASE_URI(String BASE_URI) {
        this.BASE_URI = BASE_URI;
    }

    public void setDATA_PATH(String DATA_PATH) {
        this.DATA_PATH = DATA_PATH;
    }

    public void setDB_COLUMN_PERSON_ID(String DB_COLUMN_PERSON_ID) {
        this.DB_COLUMN_PERSON_ID = DB_COLUMN_PERSON_ID;
    }

    public void setDB_COLUMN_NAME(String DB_COLUMN_NAME) {
        this.DB_COLUMN_NAME = DB_COLUMN_NAME;
    }

    public void setDB_COLUMN_GENDER(String DB_COLUMN_GENDER) {
        this.DB_COLUMN_GENDER = DB_COLUMN_GENDER;
    }

    public void setDB_COLUMN_AGE(String DB_COLUMN_AGE) {
        this.DB_COLUMN_AGE = DB_COLUMN_AGE;
    }

    public String getDATA_PATH() {
        return DATA_PATH;
    }

    public String getDB_COLUMN_PERSON_ID() {
        return DB_COLUMN_PERSON_ID;
    }

    public String getDB_COLUMN_NAME() {
        return DB_COLUMN_NAME;
    }

    public String getDB_COLUMN_GENDER() {
        return DB_COLUMN_GENDER;
    }

    public String getDB_COLUMN_AGE() {
        return DB_COLUMN_AGE;
    }
}
