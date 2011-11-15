/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

/**
 *
 * @author LocNguyen
 */
public class Rule {
    
    private String ruleCode;
    private int value;

    public Rule() {
    }

    public Rule(String ruleCode, int value) {
        this.ruleCode = ruleCode;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }
    
}
