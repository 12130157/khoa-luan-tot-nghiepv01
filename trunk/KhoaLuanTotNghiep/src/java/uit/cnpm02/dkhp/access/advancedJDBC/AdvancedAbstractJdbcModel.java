package uit.cnpm02.dkhp.access.advancedJDBC;

import java.lang.reflect.ParameterizedType;

/**
 *
 * @author LocNguyen
 */
public abstract class AdvancedAbstractJdbcModel<ID extends IID> implements IAdvancedJdbcModel <ID> {

   private static final long serialVersionUID = -4657416425482604941L;
    private ID id;

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public void setId(ID id) {
        this.id = id;
    }
    private Class<ID> idClass = null;

    /**
     * Default constructor.
     */
    @SuppressWarnings(value = {"unchecked"})
    public AdvancedAbstractJdbcModel() {
        this.idClass = (Class<ID>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public String getTableName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getIdColumnName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] getIdColumnValues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean validate() {
        return true;
    }

}
