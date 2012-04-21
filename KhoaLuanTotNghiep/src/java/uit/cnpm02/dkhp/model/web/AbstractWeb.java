package uit.cnpm02.dkhp.model.web;

/**
 *
 * @author LocNguyen
 */
public abstract class AbstractWeb {
    private String id;
    private String name;

    public AbstractWeb() {
    }
    
    public AbstractWeb(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
