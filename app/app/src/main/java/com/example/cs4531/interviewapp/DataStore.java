package com.example.cs4531.interviewapp;

/**
 * @author David Gagliardi, Cunni513
 *  This is a Data Storage class that uses a Singleton approach to persist data between various activities
 *
 */
public class DataStore {

    /**
     * @author cunni513
     * @return the id of the question
     */
    public Integer getQuestionId() {
        return questionId;
    }

    /**
     * @author cunni513
     * @param id = Set the question id
     * @return true (more functionality to be added?
     */
    public boolean setQuestionId(Integer id) {
        questionId = id;
        return true;
    }

    /**
     * @author David Gagliardi
     * @return the user email
     */
    public String getEmail() {
        return userEmail;
    }

    /**
     * @author David Gagliardi
     * @param email = Properly instantiated user email
     * @return true
     */
    public boolean setEmail(String email) {
        userEmail = email;
        return true;
    }

    private String userEmail;
    private Integer questionId;

    private static final DataStore data = new DataStore();
    public static DataStore getInstance() {return data;}
}
