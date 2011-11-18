package uit.cnpm02.dkhp.access.JDBC;

import java.io.Serializable;

/**
 * A model normally shall be used to store somewhere as database, file system or network... So 
 * it should explicitly implement the <code>Serializable</code> interface.
 * @param <ID> The model ID type.
 *
 */
public interface IModel<ID extends Serializable> extends Serializable {

    /**
     * Integer id type.
     */
    public static final int INT_ID_TYPE = 1;
    /**
     * Long id type.
     */
    public static final int LONG_ID_TYPE = 2;
    /**
     * String id type.
     */
    public static final int STRING_ID_TYPE = 3;

    /**
     * @return the Id
     */
    ID getId();

    /**
     * @param id
     */
    void setId(ID id);

    /**
     * The following type shall be returned:
     * <ul>
     * <li>{@link IModel#INT_ID_TYPE}</li>
     * <li>{@link IModel#LONG_ID_TYPE}</li>
     * <li>{@link IModel#STRING_ID_TYPE}</li>
     * </ul>
     * @return the type of the ID.
     */
    int getIdType();

    /**
     * 
     * @return true if the model is validated, otherwise false.
     */
    boolean validate();
}
