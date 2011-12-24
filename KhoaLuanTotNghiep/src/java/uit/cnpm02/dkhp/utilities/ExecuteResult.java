package uit.cnpm02.dkhp.utilities;

/**
 *
 * @author LocNguyen
 */
public class ExecuteResult {
    private boolean isSucces;
    private String message;

    public ExecuteResult() {
    }

    public ExecuteResult(boolean isSucces, String message) {
        this.isSucces = isSucces;
        this.message = message;
    }

    public boolean isIsSucces() {
        return isSucces;
    }

    public void setIsSucces(boolean isSucces) {
        this.isSucces = isSucces;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
