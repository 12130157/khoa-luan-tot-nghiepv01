package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.RuleDAO;
import uit.cnpm02.dkhp.model.Rule;
import uit.cnpm02.dkhp.service.IRuleService;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.Message;
import uit.cnpm02.dkhp.utilities.StringUtils;

/**
 *
 * @author LocNguyen
 */
public class RuleServiceImpl implements IRuleService{
    private List<Rule> rules = new ArrayList<Rule>(10);
    private RuleDAO ruleDao;
    
    public RuleServiceImpl() {
        ruleDao = DAOFactory.getRuleDao();
    }
    

    @Override
    public ExecuteResult addRule(Rule r) throws Exception {
        ExecuteResult result = new ExecuteResult(true, "");
        // Validate
        if (StringUtils.isEmpty(r.getId()) 
                || StringUtils.isEmpty(r.getDescription())) {
            return new ExecuteResult(false,
                    Message.RULE_ADD_ERROR_ID_or_DESCRIPTION_EMPTY);
        }
        
        if (ruleDao.findById(r.getId()) != null) {
            return new ExecuteResult(false, Message.RULE_ADD_ERROR_ID_EXISTED);
        }
        // Add
        String id = ruleDao.add(r);
        r.setId(id);
        rules.add(r);
        
        result.setData(r);
        return result;
    }

    @Override
    public Rule updateRule(Rule r) throws Exception {
        // Validate
        
        // Update
        Rule rule = ruleDao.update(r);
        rules = ruleDao.findAll();
        return rule;
    }

    @Override
    public List<Rule> getRules() {
        
            /*if (rules == null) {
                rules = new ArrayList<Rule>(10);
            }
            
            if (rules.isEmpty()) {
                try {
                    rules = ruleDao.findAll();
                } catch (Exception ex) {
                    Logger.getLogger(RuleServiceImpl.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }*/
        try {
            rules = ruleDao.findAll();
            Collections.sort(rules, new Comparator<Rule>() {
            @Override
            public int compare(Rule o1, Rule o2) {
                return o1.compare(o2, "Ma");
            }
        });
            
        } catch (Exception ex) {
            Logger.getLogger(RuleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rules;
    }

    @Override
    public List<Rule> sort(final String by, final String type) {
        if ((rules == null) || rules.isEmpty()) {
            return null;
        }
        
        Collections.sort(rules, new Comparator<Rule>() {
            @Override
            public int compare(Rule o1, Rule o2) {
                if (type.equals("ASC"))
                    return o1.compare(o2, by);
                else
                    return o2.compare(o1, by);
            }
        });
        
        return rules;
    }

    @Override
    public boolean deleteRule(String ruleId) throws Exception {
        Rule r = ruleDao.findById(ruleId);
        if (r == null) {
            return false;
        }
        
        ruleDao.delete(r);
        rules = ruleDao.findAll();
        return true;
    }
    
}
