/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.access.mapper;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LocNguyen
 */
public class SQLUtils {
    private static String[][] queryName = new String[][]{
        {"sql.delete", "delete from {0} where {1} = {2}"},
        {"sql.delete.multi", "delete from {0} where {1} in ({2})"},
        {"sql.insert", "insert into {0}({1}) values ({2})"},
        {"sql.insert.multi", "insert into {0} ({1}) values {2}"},
        {"sql.select", "select * from {0} where {1} = {2}"},
        {"sql.select.multi", "select * from {0} where {1} in ({2})"},
        {"sql.select.all", "select * from {0}"},
        {"sql.maxId", "select max({0}) from {1}"},
        {"sql.update", "update {0} set {1} where {2} = {3}"},
        {"sql.update.advanced", "update {0} set {1} where {2}"},
        {"sql.update.multi", "update {0} set {1} where {2} in ({3})"},
        {"log.insert", "insert into Log(col1, col2) values (?,?)"},
        {"log.update", "update Log set col1=value1 where id = ?"},
        {"sql.select.rowsCount", "select count(*) from {0}"},
        {"sql.select.rows", "select * from {0} order by {1} {2} limit {3} offset {4}"}
    };
    private static Map<String, String> sqlQuery = new HashMap<String, String>(10);
    private static boolean queriesLoaded = false;
        /**
     * @param queryName query name.
     * @return found query or null if not found.
     */
    public static String getSql(String queryName) {
        if(!queriesLoaded) {
            loadQuery();
            queriesLoaded = true;
        }
        
        return sqlQuery.get(queryName);
    }
    
    private static void loadQuery() {
        sqlQuery = new HashMap<String, String>();
        for (int i = 0; i < queryName.length; i++) {
            sqlQuery.put(queryName[i][0], queryName[i][1]);
        }
    }

}
