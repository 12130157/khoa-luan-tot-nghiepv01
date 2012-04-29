package uit.cnpm02.dkhp.utilities.password;

/**
 *
 * @author LocNguyen
 */
public enum EncryptType {
    MD5("MD5"),
    SHA1("SHA"),
    SHA2("SHA-2");
    
    private String value;
    
    EncryptType(String value) {
        this.value = value;
    }
    
    public String value() {
        return this.value;
    }
}
