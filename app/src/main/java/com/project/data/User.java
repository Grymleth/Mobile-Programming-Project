package com.project.data;

public class User {
    private int userId;
    private String loginId;
    private String firstName;
    private String lastName;

    public User(String loginId, String firstName, String lastName){
        this.userId = -1;
        this.loginId = loginId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(int userId, String loginId, String firstName, String lastName){
        this(loginId, firstName, lastName);
        this.userId = userId;
    }

    /**
     * SQL lite constructor. Instantiates by using userId to query the row needed
     * @param userId
     */
    public User(int userId) {
        // TODO
    }
    @Override
    public String toString(){
        return String.format("ID: %d, LoginID: %s, First Name: %s, Last Name: %s", userId, loginId, firstName, lastName);
    }

    public int getId(){
        return userId;
    }

    public String getLoginId(){
        return loginId;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }
}
