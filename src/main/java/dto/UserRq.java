package dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRq extends POJO{

    private String authorization;

    private String email;

    private String password;

    private String name;

    public UserRq(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return authorization;
    }

    public void setToken(String token) {
        this.authorization = token;
    }

    @Override
    public String toString() {
        return "UserRq{" +
                "authorization='" + authorization + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}