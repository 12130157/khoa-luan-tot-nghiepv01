package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.Rule;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author LocNguyen
 */
public interface IRuleService {
    ExecuteResult addRule(Rule r) throws Exception;
    
    boolean deleteRule(String ruleId) throws Exception;
    
    Rule updateRule(Rule r) throws Exception;
    
    List<Rule> getRules();
    
    List<Rule> sort(final String by, final String type);
}
