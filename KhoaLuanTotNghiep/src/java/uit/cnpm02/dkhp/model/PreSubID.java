package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.advancedJDBC.IID;

/**
 *
 * @author LocNguyen
 */
public class PreSubID implements IID {

    private String sudId;
    private String preSudId;

    public PreSubID() {
        super();
    }

    public PreSubID(String sudId, String preSudId) {
        this.sudId = sudId;
        this.preSudId = preSudId;
  
    }

    public String getPreSudId() {
        return preSudId;
    }

    public void setPreSudId(String preSudId) {
        this.preSudId = preSudId;
    }

    public String getSudId() {
        return sudId;
    }

    public void setSudId(String sudId) {
        this.sudId = sudId;
    }

    @Override
    public String[] getIDNames() {
        return new String[] {
          "MaMH",
          "MaMHTQ"
        };
    }

    @Override
    public Object[] getIDValues() {
        return new Object[] {
            sudId,
            preSudId
        };
    }

    @Override
    public void setIDValues(Object[] idValues) {
        sudId = idValues[0].toString();
        preSudId = idValues[1].toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PreSubID)) {
            return false;
        }
        if ((this == null) && (obj == null)) {
            return true;
        }
        
        PreSubID other = (PreSubID) obj;
        return (this.getSudId().equals(other.getSudId())
                && this.getPreSudId().equals(other.getPreSudId()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.sudId != null ? this.sudId.hashCode() : 0);
        hash = 19 * hash + (this.preSudId != null ? this.preSudId.hashCode() : 0);
        return hash;
    }
}
