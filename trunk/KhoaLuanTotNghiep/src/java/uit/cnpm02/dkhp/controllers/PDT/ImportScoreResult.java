package uit.cnpm02.dkhp.controllers.PDT;

import java.util.List;
import uit.cnpm02.dkhp.model.Student;

/**
 *
 * @author LocNguyen
 */
public class ImportScoreResult {
    private String msg;
    private List<Student> added;
    private List<Student> updated;
    private List<Student> error;
    private List<Student> inoged;

    public List<Student> getInoged() {
        return inoged;
    }

    public void setInoged(List<Student> inoged) {
        this.inoged = inoged;
    }

    public ImportScoreResult() {
    }
    
    public ImportScoreResult(String msg) {
        this.msg = msg;
    }

    public List<Student> getAdded() {
        return added;
    }

    public void setAdded(List<Student> added) {
        this.added = added;
    }

    public List<Student> getError() {
        return error;
    }

    public void setError(List<Student> error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Student> getUpdated() {
        return updated;
    }

    public void setUpdated(List<Student> updated) {
        this.updated = updated;
    }
}
