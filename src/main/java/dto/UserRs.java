package dto;

public class UserRs extends POJO {

    private boolean success;

    private User user;

    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UserRs{" +
                "success=" + success +
                ", user=" + user +
                ", message='" + message + '\'' +
                '}';
    }
}
