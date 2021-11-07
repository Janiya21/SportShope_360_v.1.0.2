package model;

public class UserDetail {
    private String userId;
    private String userName;
    private String address;
    private String email;
    private String accType;
    private String password;

    public UserDetail(String userName, double tot){

    }

    public UserDetail(String userId, String accType, String password) {
        this.userId = userId;
        this.accType = accType;
        this.password = password;
    }

    public UserDetail(String userId, String userName, String address, String email, String accType, String password) {
        this.userId = userId;
        this.userName = userName;
        this.address = address;
        this.email = email;
        this.accType = accType;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
