package uit.cnpm02.dkhp.access.util;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * AbstractJdbcModel
 * @param <ID> model's id type.
 */
public abstract class AbstractJdbcModel<ID extends Serializable> implements
        IJdbcModel<ID> {

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
    public AbstractJdbcModel() {
        this.idClass = (Class<ID>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public int getIdType() {
        if (idClass.getSimpleName().equals("Integer")) {
            return INT_ID_TYPE;
        }
        if (idClass.getSimpleName().equals("Long")) {
            return LONG_ID_TYPE;
        }
        return STRING_ID_TYPE;
    }

    @Override
    public boolean isIdAutoIncrement() {
        return true;
    }

    @Override
    public String getTableName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getIdColumnName() {
        return "ID";
    }

    @Override
    public boolean validate() {
        return true;
    }
}
