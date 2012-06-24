/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.PDT;

import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.utilities.Constants;

/**
 *
 * @author thanh
 */
@WebServlet(name = "ManageSchedule", urlPatterns = {"/ManageSchedule"})
public class ManageSchedule extends HttpServlet {

    /** 
     * 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            String action = request.getParameter("action");
            if (action.equalsIgnoreCase("view")) {
                getSchedule(response, session);
            } else if (action.equalsIgnoreCase("reload")) {
                reloadSchedule(request, response);
            }
        } finally {
            out.close();
        }
    }

    /**
     * 
     * @param request
     * @param response 
     */
    private void reloadSchedule(HttpServletRequest request, HttpServletResponse response) {
        TrainClassDAO trainClassDao = new TrainClassDAO();
        int date = Integer.parseInt(request.getParameter("date"));
        String faculty = request.getParameter("faculty");
        List<TrainClass> monday = null;
        List<TrainClass> tuesday = null;
        List<TrainClass> wednesday = null;
        List<TrainClass> thursday = null;
        List<TrainClass> friday = null;
        List<TrainClass> saturday = null;
        try {
            PrintWriter out = response.getWriter();
            // get all
            if (date == 0 && faculty.equalsIgnoreCase("All")) {
                monday = trainClassDao.findAllByStudyDate(Constants.MONDAY);
                setSubjectAndLecturer(monday);
                tuesday = trainClassDao.findAllByStudyDate(Constants.TUESDAY);
                setSubjectAndLecturer(tuesday);
                wednesday = trainClassDao.findAllByStudyDate(Constants.WEDNESDAY);
                setSubjectAndLecturer(wednesday);
                thursday = trainClassDao.findAllByStudyDate(Constants.THURSDAY);
                setSubjectAndLecturer(thursday);
                friday = trainClassDao.findAllByStudyDate(Constants.FRIDAY);
                setSubjectAndLecturer(friday);
                saturday = trainClassDao.findAllByStudyDate(Constants.SATURDAY);
                setSubjectAndLecturer(saturday);
            }//get follow faculty
            else if (date == 0 && !faculty.equalsIgnoreCase("All")) {
                monday = trainClassDao.findAllByStudyDateAndOnlyFaculty(Constants.MONDAY, faculty);
                setSubjectAndLecturer(monday);
                tuesday = trainClassDao.findAllByStudyDateAndOnlyFaculty(Constants.TUESDAY, faculty);
                setSubjectAndLecturer(tuesday);
                wednesday = trainClassDao.findAllByStudyDateAndOnlyFaculty(Constants.WEDNESDAY, faculty);
                setSubjectAndLecturer(wednesday);
                thursday = trainClassDao.findAllByStudyDateAndOnlyFaculty(Constants.THURSDAY, faculty);
                setSubjectAndLecturer(thursday);
                friday = trainClassDao.findAllByStudyDateAndOnlyFaculty(Constants.FRIDAY, faculty);
                setSubjectAndLecturer(friday);
                saturday = trainClassDao.findAllByStudyDateAndOnlyFaculty(Constants.SATURDAY, faculty);
                setSubjectAndLecturer(saturday);
            }//get follow date
            else if (date != 0 && faculty.equalsIgnoreCase("All")) {
                if (date == 2) {
                    monday = trainClassDao.findAllByStudyDate(Constants.MONDAY);
                    setSubjectAndLecturer(monday);
                } else if (date == 3) {
                    tuesday = trainClassDao.findAllByStudyDate(Constants.TUESDAY);
                    setSubjectAndLecturer(tuesday);
                } else if (date == 4) {
                    wednesday = trainClassDao.findAllByStudyDate(Constants.WEDNESDAY);
                    setSubjectAndLecturer(wednesday);
                } else if (date == 5) {
                    thursday = trainClassDao.findAllByStudyDate(Constants.THURSDAY);
                    setSubjectAndLecturer(thursday);
                } else if (date == 6) {
                    friday = trainClassDao.findAllByStudyDate(Constants.FRIDAY);
                    setSubjectAndLecturer(friday);
                } else {
                    saturday = trainClassDao.findAllByStudyDate(Constants.SATURDAY);
                    setSubjectAndLecturer(saturday);
                }

            }//get follow date and faculty
            else {
                if (date == 2) {
                    monday = trainClassDao.findAllByStudyDateAndOnlyFaculty(Constants.MONDAY, faculty);
                    setSubjectAndLecturer(monday);
                } else if (date == 3) {
                    tuesday = trainClassDao.findAllByStudyDateAndOnlyFaculty(Constants.TUESDAY, faculty);
                    setSubjectAndLecturer(tuesday);
                } else if (date == 4) {
                    wednesday = trainClassDao.findAllByStudyDateAndOnlyFaculty(Constants.WEDNESDAY, faculty);
                    setSubjectAndLecturer(wednesday);
                } else if (date == 5) {
                    thursday = trainClassDao.findAllByStudyDateAndOnlyFaculty(Constants.THURSDAY, faculty);
                    setSubjectAndLecturer(thursday);
                } else if (date == 6) {
                    friday = trainClassDao.findAllByStudyDateAndOnlyFaculty(Constants.FRIDAY, faculty);
                    setSubjectAndLecturer(friday);
                } else {
                    saturday = trainClassDao.findAllByStudyDateAndOnlyFaculty(Constants.SATURDAY, faculty);
                    setSubjectAndLecturer(saturday);
                }
            }
            //print output
            out.println("<tr><th width='50px'></th><th width='50px'>Mã lớp</th><th width='200px'>Môn học</th><th width='50px'>Phòng</th><th width='50px'>Buổi</th><th width='200px'>Giảng viên</th></tr>");
            out.println("<tr><th><b><u>Thứ 2:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
            if (monday != null && !monday.isEmpty()) {
                for (int i = 0; i < monday.size(); i++) {
                    StringBuffer output = new StringBuffer();
                    output.append("<tr><td></td><td>");
                    output.append(monday.get(i).getId().getClassCode());
                    output.append("</td><td>");
                    output.append(monday.get(i).getSubjectName());
                    output.append("</td><td>");
                    output.append(monday.get(i).getClassRoom());
                    if (monday.get(i).getShift() == 1) {
                        output.append("</td><td>Sáng</td><td>");
                    } else {
                        output.append("</td><td>Chiều</td><td>");
                    }
                    output.append(monday.get(i).getLectturerName());
                    output.append("</td></tr>");
                    out.println(output.toString());
                }
            }
            out.println("<tr><th><b><u>Thứ 3:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
            if (tuesday != null && !tuesday.isEmpty()) {
                for (int i = 0; i < tuesday.size(); i++) {
                    StringBuffer output = new StringBuffer();
                    output.append("<tr><td></td><td>");
                    output.append(tuesday.get(i).getId().getClassCode());
                    output.append("</td><td>");
                    output.append(tuesday.get(i).getSubjectName());
                    output.append("</td><td>");
                    output.append(tuesday.get(i).getClassRoom());
                    if (tuesday.get(i).getShift() == 1) {
                        output.append("</td><td>Sáng</td><td>");
                    } else {
                        output.append("</td><td>Chiều</td><td>");
                    }
                    output.append(tuesday.get(i).getLectturerName());
                    output.append("</td></tr>");
                    out.println(output.toString());
                }
            }
            out.println("<tr><th><b><u>Thứ 4:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
            if (wednesday != null && !wednesday.isEmpty()) {
                for (int i = 0; i < wednesday.size(); i++) {
                    StringBuffer output = new StringBuffer();
                    output.append("<tr><td></td><td>");
                    output.append(wednesday.get(i).getId().getClassCode());
                    output.append("</td><td>");
                    output.append(wednesday.get(i).getSubjectName());
                    output.append("</td><td>");
                    output.append(wednesday.get(i).getClassRoom());
                    if (wednesday.get(i).getShift() == 1) {
                        output.append("</td><td>Sáng</td><td>");
                    } else {
                        output.append("</td><td>Chiều</td><td>");
                    }
                    output.append(wednesday.get(i).getLectturerName());
                    output.append("</td></tr>");
                    out.println(output.toString());
                }
            }
            out.println("<tr><th><b><u>Thứ 5:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
            if (thursday != null && !thursday.isEmpty()) {
                for (int i = 0; i < thursday.size(); i++) {
                    StringBuffer output = new StringBuffer();
                    output.append("<tr><td></td><td>");
                    output.append(thursday.get(i).getId().getClassCode());
                    output.append("</td><td>");
                    output.append(thursday.get(i).getSubjectName());
                    output.append("</td><td>");
                    output.append(thursday.get(i).getClassRoom());
                    if (thursday.get(i).getShift() == 1) {
                        output.append("</td><td>Sáng</td><td>");
                    } else {
                        output.append("</td><td>Chiều</td><td>");
                    }
                    output.append(thursday.get(i).getLectturerName());
                    output.append("</td></tr>");
                    out.println(output.toString());
                }
            }
            out.println("<tr><th><b><u>Thứ 6:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
            if (friday != null && !friday.isEmpty()) {
                for (int i = 0; i < friday.size(); i++) {
                    StringBuffer output = new StringBuffer();
                    output.append("<tr><td></td><td>");
                    output.append(friday.get(i).getId().getClassCode());
                    output.append("</td><td>");
                    output.append(friday.get(i).getSubjectName());
                    output.append("</td><td>");
                    output.append(friday.get(i).getClassRoom());
                    if (friday.get(i).getShift() == 1) {
                        output.append("</td><td>Sáng</td><td>");
                    } else {
                        output.append("</td><td>Chiều</td><td>");
                    }
                    output.append(friday.get(i).getLectturerName());
                    output.append("</td></tr>");
                    out.println(output.toString());
                }
            }
            out.println("<tr><th><b><u>Thứ 7:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
            if (saturday != null && !saturday.isEmpty()) {
                for (int i = 0; i < saturday.size(); i++) {
                    StringBuffer output = new StringBuffer();
                    output.append("<tr><td></td><td>");
                    output.append(saturday.get(i).getId().getClassCode());
                    output.append("</td><td>");
                    output.append(saturday.get(i).getSubjectName());
                    output.append("</td><td>");
                    output.append(saturday.get(i).getClassRoom());
                    if (saturday.get(i).getShift() == 1) {
                        output.append("</td><td>Sáng</td><td>");
                    } else {
                        output.append("</td><td>Chiều</td><td>");
                    }
                    output.append(saturday.get(i).getLectturerName());
                    output.append("</td></tr>");
                    out.println(output.toString());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManageSchedule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param response
     * @param session
     * @throws IOException 
     */
    private void getSchedule(HttpServletResponse response, HttpSession session) throws IOException {
        TrainClassDAO trainClassDao = new TrainClassDAO();
        String path = "";
        try {
            List<TrainClass> monday = trainClassDao.findAllByStudyDate(Constants.MONDAY);
            setSubjectAndLecturer(monday);
            List<TrainClass> tuesday = trainClassDao.findAllByStudyDate(Constants.TUESDAY);
            setSubjectAndLecturer(tuesday);
            List<TrainClass> wednesday = trainClassDao.findAllByStudyDate(Constants.WEDNESDAY);
            setSubjectAndLecturer(wednesday);
            List<TrainClass> thursday = trainClassDao.findAllByStudyDate(Constants.THURSDAY);
            setSubjectAndLecturer(thursday);
            List<TrainClass> friday = trainClassDao.findAllByStudyDate(Constants.FRIDAY);
            setSubjectAndLecturer(friday);
            List<TrainClass> saturday = trainClassDao.findAllByStudyDate(Constants.SATURDAY);
            setSubjectAndLecturer(saturday);
            List<Faculty> facultyList = DAOFactory.getFacultyDao().findAll();
            session.setAttribute("monday", monday);
            session.setAttribute("tuesday", tuesday);
            session.setAttribute("wednesday", wednesday);
            session.setAttribute("thursday", thursday);
            session.setAttribute("friday", friday);
            session.setAttribute("saturday", saturday);
            session.setAttribute("year", Constants.CURRENT_YEAR);
            session.setAttribute("semester", Constants.CURRENT_SEMESTER);
            session.setAttribute("facultyList", facultyList);
            path = "./jsps/PDT/Schedule.jsp";
        } catch (Exception ex) {
            Logger.getLogger(ManageSchedule.class.getName()).log(Level.SEVERE, null, ex);
            path = "./jsps/Message.jsp";
        }
        response.sendRedirect(path);
    }

    private void setSubjectAndLecturer(List<TrainClass> trainClass) throws Exception {
        SubjectDAO subjectDao = new SubjectDAO();
        LecturerDAO lecturerDao = new LecturerDAO();
        for (int i = 0; i < trainClass.size(); i++) {
            trainClass.get(i).setSubjectName(subjectDao.findById(trainClass.get(i).getSubjectCode()).getSubjectName());
            trainClass.get(i).setLectturerName(lecturerDao.findById(trainClass.get(i).getLecturerCode()).getFullName());
            trainClass.get(i).setNumTC(subjectDao.findById(trainClass.get(i).getSubjectCode()).getnumTC());
        }
    }

    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * 
     * @return 
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
