/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.model.Rule;
import uit.cnpm02.dkhp.service.IRuleService;
import uit.cnpm02.dkhp.service.impl.RuleServiceImpl;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.StringUtils;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "ManageRuleController", urlPatterns = {"/ManageRuleController"})
public class ManageRuleController extends HttpServlet {

    private IRuleService ruleService = new RuleServiceImpl();
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            String action = request.getParameter("action");
            if (action.equalsIgnoreCase(RuleFunction.DEFAULT.value())) {
                loadDefault(session, response);
                return;
            } else if (action.equalsIgnoreCase(RuleFunction.ADD.value())) {
                doAddNewRule(out, request);
                return;
            } else if (action.equalsIgnoreCase(RuleFunction.DELETE.value())) {
                doDeleteRule(out, request);
                return;
            } else if (action.equalsIgnoreCase(RuleFunction.EDIT.value())) {
                doEditRule(out, request);
                return;
            }
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void loadDefault(HttpSession session, HttpServletResponse response) {
        try {
            List<Rule> preSubs = ruleService.getRules();
            
            if ((preSubs != null) && !preSubs.isEmpty()) {
                session.setAttribute("rules", preSubs);
            }

            String path = "./jsps/PDT/RuleManager.jsp";
            response.sendRedirect(path);
        } catch (Exception ex) {
            Logger.getLogger(ManageRuleController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    private void doAddNewRule(PrintWriter out, HttpServletRequest request) {
        String id = request.getParameter("id");
        float value = Float.parseFloat(request.getParameter("value"));
        String description = request.getParameter("description");
        
        Rule r = new Rule(id, value);
        r.setDescription(description);
        ExecuteResult er = null;
        try {
            er = ruleService.addRule(r);
        } catch (Exception ex) {
            Logger.getLogger(ManageRuleController.class.getName())
                    .log(Level.SEVERE, null, ex);
            out.println("Lỗi.");
        }
        
        List<Rule> rules = ruleService.getRules();
        HttpSession session = request.getSession();
        session.setAttribute("rules", rules);
        
        writeOutAddNewRuleRespond(out, er);
    }

    private void writeOutAddNewRuleRespond(PrintWriter out, ExecuteResult er) {
        if (!er.isIsSucces()) {
            out.println(er.getMessage());
        } else {
            Rule r = (Rule) er.getData();
            out.println("Thêm thành công Rule: </br>");
            out.println("- Qui định: <b>" + r.getDescription() + "</b></br>");
            out.println("- Giá trị: <b>" + r.getValue() + "</b></br>");
        }
    }

    private void doDeleteRule(PrintWriter out, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isEmpty(id)) {
            out.println("Id không hợp lệ");
            return;
        }
        try {
            ruleService.deleteRule(id);
            
            List<Rule> preSubs = ruleService.getRules();
            
            if ((preSubs != null) && !preSubs.isEmpty()) {
                HttpSession session = request.getSession();
                session.setAttribute("rules", preSubs);
            }
        } catch (Exception ex) {
            Logger.getLogger(ManageRuleController.class.getName())
                    .log(Level.SEVERE, null, ex);
            out.println("Đã có lỗi xảy ra trong quá trình xóa Rule.");
            return;
        }
        
        List<Rule> rules = ruleService.getRules();
        writeOutListRules(out, rules);
    }

    private void writeOutListRules(PrintWriter out, List<Rule> rules) {
        out.println("<table id=\"table-rules\" class=\"general-table\" style=\"width: 750px !important\">");
        String header = "<tr> <th> STT </th> <th> Mã </th> <th> Tên </th> <th> Giá trị </th> <th></th></tr>";
        out.println(header);
        if ((rules != null) && !rules.isEmpty()) {
            for (int i = 0; i < rules.size(); i++) {
                out.println("<tr>");
                out.println("<td> " + (i + 1) + " </td>");
                out.println("<td> " + rules.get(i).getId() + " </td>");
                out.println("<td  id=\"td-description" + i + "\"> " + rules.get(i).getDescription() + " </td>");
                out.println("<td id=\"td-value" + i + "\"> " 
                        + rules.get(i).getValue() 
                        + " </td>");
                out.println("<td id=\"td-edit" + i + "\"> " 
                    + "<a href=\"#\" onclick=\"edit(" + i + ", '" + rules.get(i).getId() + "')\"> Sửa </a>"
                    + " - <a href=\"#\" onclick=\"deleteRule('" + rules.get(i).getId() + "')\"> Xóa </a>");
                out.println("</td>");
                out.println("</tr>");
            }
        }
    }

    private void doEditRule(PrintWriter out, HttpServletRequest request) {
        String id = request.getParameter("id");
        float value = Float.parseFloat(request.getParameter("new_value"));
        String description = request.getParameter("new_description");
        
        if (StringUtils.isEmpty(id)) {
            return;
        }
        
        Rule r = new Rule(id, value);
        r.setId(id);
        r.setDescription(description);
        try {
            ruleService.updateRule(r);
        } catch (Exception ex) {
            Logger.getLogger(ManageRuleController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        List<Rule> rules = ruleService.getRules();
        HttpSession session = request.getSession();
        session.setAttribute("rules", rules);
    }

    public enum RuleFunction {
        DEFAULT("default"),
        ADD("add"),
        DELETE("delete"),
        EDIT("edit"),
        START_UPDATE("starti_update"),
        SAVE_UPDATE("save_update");

        private String function;
        private RuleFunction(String function) {
            this.function = function;
        }
        
        public String value() {
            return function;
        }
    }
}
