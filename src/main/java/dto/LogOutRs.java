package dto;

public class LogOutRs extends POJO{

    private boolean success;

    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LogOutRs{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
