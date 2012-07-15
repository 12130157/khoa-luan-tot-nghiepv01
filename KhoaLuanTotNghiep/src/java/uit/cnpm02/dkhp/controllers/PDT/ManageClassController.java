package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.DetailTrainDAO;
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.RegistrationDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.DetailTrain;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Lecturer;
import uit.cnpm02.dkhp.model.Registration;
import uit.cnpm02.dkhp.model.RegistrationID;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.model.web.LecturerWeb;
import uit.cnpm02.dkhp.model.web.SubjectWeb;
import uit.cnpm02.dkhp.service.ITrainClassService;
import uit.cnpm02.dkhp.service.TrainClassStatus;
import uit.cnpm02.dkhp.service.impl.TrainClassServiceImpl;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.DateTimeUtil;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.StringUtils;

/**
 * Manage Class
 * + Open, set up new class
 * + Edit class information
 * + Cacel/delete class
 * 
 * @author LocNguyen
 */
@WebServlet(name = "ManageClassController", urlPatterns = {"/ManageClassController"})
public class ManageClassController extends HttpServlet {

    // DAO definition ///
    private TrainClassDAO classDAO = DAOFactory.getTrainClassDAO();
    private SubjectDAO subjectDAO = DAOFactory.getSubjectDao();
    private LecturerDAO lectureDAO = DAOFactory.getLecturerDao();
    private FacultyDAO facultyDAO = DAOFactory.getFacultyDao();
    private RegistrationDAO regDAO = DAOFactory.getRegistrationDAO();
    private ITrainClassService trainClassService = new TrainClassServiceImpl();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String requestAction;
        try {
            requestAction = (String) request.getParameter("action");
        } catch (Exception ex) {
            requestAction = ClassFunctionSupported.DEFAULT.getValue();
        }

        try {
            if (requestAction.equals(ClassFunctionSupported.DEFAULT.getValue())) {
                // List existed TrainClass, Pagging setup
                defaultAction(request, response);
            } else if (requestAction.equals(ClassFunctionSupported.PRECREATE.getValue())) {
                preCreateNewTrainClass(request);
                String path = "./jsps/PDT/AddTrainClass.jsp";
                response.sendRedirect(path);
                return;
            } else if (requestAction.equals(ClassFunctionSupported.CREATE.getValue())) {
                ExecuteResult result = createNewTrainClass(request);
                writeRespondCreateNewTrainClass(result, out);
                return;
            } else if (requestAction.equals(ClassFunctionSupported.CHECK_CREATE.getValue())) {
                ExecuteResult result = checkCreate(request);
                writeRespondCreateNewTrainClass(result, out);
                return;
            } else if (requestAction.equals(ClassFunctionSupported.DELETE.getValue())) {
                // Delete a TrainClass specified
            } else if (requestAction.equals(ClassFunctionSupported.PREUPDATE.getValue())) {
                // Prepare to update TrainClass
                preUpdateTrainClass(request, response);
            } else if (requestAction.equals(ClassFunctionSupported.UPDATE.getValue())) {
                // Update TrainClass
                updateTrainClass(request, response);
            } else if (requestAction.equals(ClassFunctionSupported.DETAIL.getValue())) {
                // view detail train class
                viewDetailClass(request, response);
            } else if (requestAction.equalsIgnoreCase(ClassFunctionSupported.FILTER_BY_YAS.getValue())) {
                filterTrainClass(request, response);
            } else if (requestAction.equalsIgnoreCase(ClassFunctionSupported.SEARCH.getValue())) {
                searchTrainClass(request, response);
            } else if (requestAction.equalsIgnoreCase(ClassFunctionSupported.FILTER_BY_FACULTY.getValue())) {
                FilterByFaculty(request, response);
            } else if (requestAction.equalsIgnoreCase(ClassFunctionSupported.FILTER_PAGE_BY_FACULTY.getValue())) {
                FilterPageByFaculty(request, response);
            } else if (requestAction.equalsIgnoreCase(ClassFunctionSupported.FILTER_PAGE_BY_INPUT.getValue())) {
                FilterPageByInputValue(request, response);
            } else if (requestAction.equalsIgnoreCase(ClassFunctionSupported.FILTER_PAGE_BY_YAS.getValue())) {
                FilterPageByYAS(request, response);
            } else if (requestAction.equalsIgnoreCase(ClassFunctionSupported.CLOSE_BY_INPUT.getValue())) {
                closeClassWithInputSearch(request, response);
            } else if (requestAction.equalsIgnoreCase(ClassFunctionSupported.CLOSE_BY_FACULTY.getValue())) {
                closeClassWithFilterByFaculty(request, response);
            } else if (requestAction.equalsIgnoreCase(ClassFunctionSupported.CLOSE_BY_YAS.getValue())) {
                closeClassWithFilterByYAS(request, response);
            } else if (requestAction.equalsIgnoreCase(ClassFunctionSupported.FILTER_SUBJECT_BY_FACULTY.getValue())) {
                reloadSubjectFollowFaculty(request, response);
            } else if (requestAction.equalsIgnoreCase(ClassFunctionSupported.FILTER_LECTURER_BY_SUBJECT.getValue())) {
                reloadLecturerBySubject(request, response);
            } else if (requestAction.equalsIgnoreCase(ClassFunctionSupported.CANCEL.getValue())) {
                cancelTrainClass(request, response);
            } else if (requestAction.equalsIgnoreCase(ClassFunctionSupported.AUTOCANCEL.getValue())) {
                autoCancelClass(request, response);
            } else if (requestAction.equalsIgnoreCase(ClassFunctionSupported.MOVESTUDENT.getValue())) {
                manualMoveStudent(request, response);
            }
        } finally {
            out.close();
        }
    }

    private void manualMoveStudent(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            //move student
            String studentCode = request.getParameter("studentCode");
            String sourceClass = request.getParameter("sourceClass");
            String desClass = request.getParameter("desClass");
            String classYear = request.getParameter("year");
            int semester = Integer.parseInt(request.getParameter("semester"));
            moveOneStudentToSameClass(studentCode, sourceClass, desClass, semester, classYear);
            // get student list registry
            TrainClassID id = new TrainClassID(sourceClass, classYear, semester);
            List<Registration> registration = DAOFactory.getRegistrationDAO().findAllByClassCode(id);
            List<Student> studentList = new ArrayList<Student>();
            for (int i = 0; i < registration.size(); i++) {
                Student student = DAOFactory.getStudentDao().findById(registration.get(i).getId().getStudentCode());
                studentList.add(student);
            }
            //print respone
            List<TrainClass> sameClass = getSameClassAsSubject(id);
            out.println("<tr>"
                    + "<th> STT</th>"
                    + "<th> MSSV </th>"
                    + "<th> Họ tên </th>"
                    + "<th> Lớp </th>"
                    + "<th> Lớp cùng môn </th>"
                    + "<th> Chuyển </th>"
                    + "</tr>");
            if (studentList != null && !studentList.isEmpty() && sameClass != null & !sameClass.isEmpty()) {
                for (int i = 0; i < studentList.size(); i++) {
                    out.println("<tr><td>" + (i + 1) + "</td>");
                    out.println("<td>" + studentList.get(i).getId() + "</td>");
                    out.println("<td>" + studentList.get(i).getFullName() + "</td>");
                    out.println("<td>" + studentList.get(i).getClassCode() + "</td>");
                    out.println("<td><select id=" + i + ">");
                    for (int j = 0; j < sameClass.size(); j++) {
                        out.println("<option value='" + sameClass.get(j).getId().getClassCode() + "'>" + sameClass.get(j).getId().getClassCode() + "</option>");
                    }
                    out.println("</select></td>");
                    out.println("<td><input type='button' value='  Chuyển  ' onclick=\"moveEachStudent('" + studentList.get(i).getId() + "','" + sourceClass + "','" + i + "','" + semester + "','" + classYear + "')\"/></td>");
                    out.println("</tr>");

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void moveOneStudentToSameClass(String studentCode, String sourceClass, String desClass, int semester, String year) {
        try {
            RegistrationID currentRegID = new RegistrationID(studentCode, sourceClass, semester, year);
            Registration currentReg = regDAO.findById(currentRegID);
            regDAO.delete(currentReg);
            currentReg.getId().setClassCode(desClass);
            regDAO.add(currentReg);
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private boolean checkRegistrationListWithSameClass(List<Registration> studentReg,TrainClass sameClass){
        boolean result= false;
        for(int i =0; i<studentReg.size();i++){
            try {
                TrainClassID classID = new TrainClassID(studentReg.get(i).getId().getClassCode(), Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
                TrainClass curentClass= classDAO.findById(classID);
                if(curentClass.getStudyDate()== sameClass.getStudyDate()){
                    result= true;
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    private String getValidTrainClassToMoveRegistration(Registration curentReg, List<TrainClass> sameClass ){
        try {
            String studentCode = curentReg.getId().getStudentCode();
            List<Registration> studentReg = regDAO.findAllByStudentCode(studentCode);
            //tim lop trong va khong trung lich
            for(int i =0; i<sameClass.size(); i++){
                int numAllow = sameClass.get(i).getNumOfStudent() - sameClass.get(i).getNumOfStudentReg();
                if(numAllow > 0 && checkRegistrationListWithSameClass(studentReg, sameClass.get(i))== false )
                return sameClass.get(i).getId().getClassCode();
            }
            //tim lop khong trung lich
            for(int i =0; i<sameClass.size(); i++){
                if(!checkRegistrationListWithSameClass(studentReg, sameClass.get(i)) )
                    return sameClass.get(i).getId().getClassCode();
            }
            return sameClass.get(0).getId().getClassCode();
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    private void moveAllStudentToSameClass(TrainClass trainclass, List<Registration> registration, List<TrainClass> sameClass) {
        while (registration != null && !registration.isEmpty()) {
            try {
                int moveReg = registration.size() - 1;
                Registration temp = registration.get(moveReg);
                regDAO.delete(temp);
                String newClassCode = getValidTrainClassToMoveRegistration(temp, sameClass);
                temp.getId().setClassCode(newClassCode);
                regDAO.add(temp);
                registration.remove(moveReg);
            } catch (Exception ex) {
                Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        /*for (int j = 0; j < sameClass.size(); j++) {
            int k = 0;
            int l = 0;
            if (registration != null && !registration.isEmpty()) {
                l = registration.size() - 1;
                int numAllow = sameClass.get(j).getNumOfStudent() - sameClass.get(j).getNumOfStudentReg();
                if (numAllow > 0) {
                    while (l > 0 && k < numAllow) {
                        try {
                            Registration temp = registration.get(l);
                            regDAO.delete(temp);
                            temp.getId().setClassCode(sameClass.get(j).getId().getClassCode());
                            regDAO.add(temp);
                            registration.remove(l);
                            k++;
                            l--;
                        } catch (Exception ex) {
                            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } else {
                try {
                    classDAO.delete(trainclass);
                    return;
                } catch (Exception ex) {
                    Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        int desClass = 0;
        while (registration != null && !registration.isEmpty()) {
            try {
                int moveReg = registration.size() - 1;
                Registration temp = registration.get(moveReg);
                regDAO.delete(temp);
                temp.getId().setClassCode(sameClass.get(desClass).getId().getClassCode());
                regDAO.add(temp);
                registration.remove(moveReg);
                desClass++;
                if (desClass >= sameClass.size() - 1) {
                    desClass = 0;
                }
            } catch (Exception ex) {
                Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
    }

    private void autoCancelClass(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String classCode = request.getParameter("classCode");
            int semester = Integer.parseInt(request.getParameter("classSemester"));
            String year = request.getParameter("classYear");
            TrainClassID id = new TrainClassID(classCode, year, semester);
            TrainClass trainclass = classDAO.findById(id);
            if (trainclass == null) {
                out.println("Lớp đã được hủy.");
            } else {
                trainclass.setSubjectName(subjectDAO.findById(trainclass.getSubjectCode()).getSubjectName());
                trainclass.setLectturerName(lectureDAO.findById(trainclass.getLecturerCode()).getFullName());
                List<Registration> registration = DAOFactory.getRegistrationDAO().findAllByClassCode(id);
                int numReg = registration.size();
                if (registration == null || registration.isEmpty()) {
                    classDAO.delete(trainclass);
                    out.println("Hủy lớp thành công (Lớp không có sinh viên nào đăng ký)");
                } else {
                    List<TrainClass> sameClass = getSameClassAsSubject(id);
                    if (sameClass == null || sameClass.isEmpty()) {
                        for (int i = 0; i < registration.size(); i++) {
                            regDAO.delete(registration.get(i));
                        }
                        classDAO.delete(trainclass);
                        out.println("Lớp có " + registration.size() + " sinh viên đăng ký.");
                        out.println("<br>");
                        out.println("Không có lớp học khác dạy môn học " + trainclass.getSubjectName() + ".");
                        out.println("<br>");
                        out.println("Đã hủy đăng ký của " + registration.size() + " sinh viên.");
                        out.println("<br>");
                        out.println("Đã hủy lớp thành công.");
                    } else {
                        moveAllStudentToSameClass(trainclass, registration, sameClass);
                        classDAO.delete(trainclass);
                        out.println("Lớp có " + numReg + " sinh viên đăng ký.");
                        out.println("<br>");
                        out.println("Hiện có " + sameClass.size() + " lớp học khác dạy môn học " + trainclass.getSubjectName() + ".");
                        out.println("<br>");
                        out.println("Đã chuyển đăng ký của " + numReg + " sinh viên qua các lớp khác.");
                        out.println("<br>");
                        out.println("Đã hủy lớp thành công.");
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }

    private List<TrainClass> getSameClassAsSubject(TrainClassID id) {
        try {
            List<TrainClass> sameClass;
            List<String> columnNames = new ArrayList<String>(3);
            List<Object> values = new ArrayList<Object>(3);
            columnNames.add("MaMH");
            columnNames.add("HocKy");
            columnNames.add("NamHoc");
            columnNames.add("TrangThai");
            values.add(classDAO.findById(id).getSubjectCode());
            values.add(id.getSemester());
            values.add(id.getYear());
            values.add(TrainClassStatus.OPEN.getValue());
            String[] strColumnNames = (String[]) columnNames.toArray(
                    new String[columnNames.size()]);
            sameClass = classDAO.findByColumNames(strColumnNames, values.toArray());
            for (int i = 0; i < sameClass.size(); i++) {
                if (sameClass.get(i).getId().getClassCode().equalsIgnoreCase(id.getClassCode())) {
                    sameClass.remove(i);
                }
            }
            setSubjectAndLecturer(sameClass);
            return sameClass;
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    private void cancelTrainClass(HttpServletRequest request, HttpServletResponse response) {
        String path = "";
        try {
            String classCode = request.getParameter("classID");
            int semester = Integer.parseInt(request.getParameter("semester"));
            String year = request.getParameter("year");
            TrainClassID id = new TrainClassID(classCode, year, semester);
            List<Registration> registration = DAOFactory.getRegistrationDAO().findAllByClassCode(id);
            List<Student> studentList = new ArrayList<Student>();
            for (int i = 0; i < registration.size(); i++) {
                Student student = DAOFactory.getStudentDao().findById(registration.get(i).getId().getStudentCode());
                studentList.add(student);
            }
            List<TrainClass> sameClass = getSameClassAsSubject(id);
            TrainClass trainClass = classDAO.findById(id);
            trainClass.setSubjectName(subjectDAO.findById(trainClass.getSubjectCode()).getSubjectName());
            trainClass.setLectturerName(lectureDAO.findById(trainClass.getLecturerCode()).getFullName());
            HttpSession session = request.getSession();
            session.setAttribute("trainclass", trainClass);
            session.setAttribute("studentList", studentList);
            session.setAttribute("classList", sameClass);
            path = "./jsps/PDT/CancelTrainClass.jsp";
            response.sendRedirect(path);

        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reloadLecturerBySubject(HttpServletRequest request, HttpServletResponse response) {
        try {
            DetailTrainDAO detailTrainDAO = DAOFactory.getDetainTrainDAO();
            PrintWriter out = response.getWriter();
            List<Lecturer> lecturerList = new ArrayList<Lecturer>(10);
            String subjectCode = request.getParameter("subjetCode");
            List<DetailTrain> detailTrainList = detailTrainDAO.findByColumName("MaMH", subjectCode);
            if (detailTrainList != null && !detailTrainList.isEmpty()) {
                for (int i = 0; i < detailTrainList.size(); i++) {
                    Lecturer temp = lectureDAO.findById(detailTrainList.get(i).getId().getLecturerCode());
                    lecturerList.add(temp);
                }
            }
            if (lecturerList != null && !lecturerList.isEmpty()) {
                for (int i = 0; i < lecturerList.size(); i++) {
                    out.println("<option value='" + lecturerList.get(i).getId() + "'>" + lecturerList.get(i).getFullName() + "</option>");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param request
     * @param response 
     */
    private void reloadSubjectFollowFaculty(HttpServletRequest request, HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            String facultyCode = request.getParameter("facultyCode");
            List<Subject> subjectList = subjectDAO.findByColumName("MaKhoa", facultyCode);
            if (subjectList != null && !subjectList.isEmpty()) {
                for (int i = 0; i < subjectList.size(); i++) {
                    out.println("<option value='" + subjectList.get(i).getId() + "'>" + subjectList.get(i).getSubjectName() + "</option>");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param request
     * @param response 
     */
    private void closeClassWithFilterByYAS(HttpServletRequest request, HttpServletResponse response) {
        try {
            int numPage = 0;
            PrintWriter out = response.getWriter();
            //get info close class
            String classCode = request.getParameter("classCode");
            String classYear = request.getParameter("classYear");
            int classSemester = Integer.parseInt(request.getParameter("classSemester"));
            //Close train class
            TrainClassID id = new TrainClassID(classCode, classYear, classSemester);
            TrainClass updateClass = classDAO.findById(id);
            updateClass.setStatus(TrainClassStatus.CLOSE);
            classDAO.update(updateClass);
            //get list class again
            int currentPage = Integer.parseInt(request.getParameter("curentPage"));
            String year = request.getParameter("year");
            int semester = Integer.parseInt(request.getParameter("semester"));
            List<TrainClass> trainclasslist = trainClassService.getAllClassOpenByYearAndSemester(year, semester);
            //set current page
            if (trainclasslist.size() % Constants.ELEMENT_PER_PAGE_DEFAULT == 0) {
                numPage = trainclasslist.size() / Constants.ELEMENT_PER_PAGE_DEFAULT;
            } else {
                numPage = trainclasslist.size() / Constants.ELEMENT_PER_PAGE_DEFAULT + 1;
            }
            if (currentPage > numPage) {
                currentPage = numPage;
            }

            List<TrainClass> trainClazzs = getClassListByPage(trainclasslist, currentPage);
            writeFilterTrainClass(trainClazzs, out, currentPage);
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param request
     * @param response 
     */
    private void closeClassWithFilterByFaculty(HttpServletRequest request, HttpServletResponse response) {
        try {
            int numpage = 0;
            PrintWriter out = response.getWriter();
            //get info close class
            String classCode = request.getParameter("classCode");
            String classYear = request.getParameter("classYear");
            int classSemester = Integer.parseInt(request.getParameter("classSemester"));
            //Close train class
            TrainClassID id = new TrainClassID(classCode, classYear, classSemester);
            TrainClass updateClass = classDAO.findById(id);
            updateClass.setStatus(TrainClassStatus.CLOSE);
            classDAO.update(updateClass);
            //get list class again
            List<TrainClass> trainClassList = new ArrayList<TrainClass>(10);
            int currentPage = Integer.parseInt(request.getParameter("curentPage"));
            String facultyCode = request.getParameter("value");
            if (facultyCode.equalsIgnoreCase("All")) {
                trainClassList = getAllClassOpen();
            } else {
                trainClassList = classDAO.findOpenClassByFaculty(facultyCode);
            }
            // set current page
            if (trainClassList.size() % Constants.ELEMENT_PER_PAGE_DEFAULT == 0) {
                numpage = trainClassList.size() / Constants.ELEMENT_PER_PAGE_DEFAULT;
            } else {
                numpage = trainClassList.size() / Constants.ELEMENT_PER_PAGE_DEFAULT + 1;
            }
            if (currentPage > numpage) {
                currentPage = numpage;
            }

            List<TrainClass> result = getClassListByPage(trainClassList, currentPage);
            writeFilterTrainClass(result, out, currentPage);
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param request
     * @param response 
     */
    private void closeClassWithInputSearch(HttpServletRequest request, HttpServletResponse response) {
        try {
            int numPage = 0;
            PrintWriter out = response.getWriter();
            //get info close class
            String classCode = request.getParameter("classCode");
            String classYear = request.getParameter("classYear");
            int classSemester = Integer.parseInt(request.getParameter("classSemester"));
            //Close train class
            TrainClassID id = new TrainClassID(classCode, classYear, classSemester);
            TrainClass updateClass = classDAO.findById(id);
            updateClass.setStatus(TrainClassStatus.CLOSE);
            classDAO.update(updateClass);
            //get list class again
            int currentPage = Integer.parseInt(request.getParameter("curentPage"));
            String value = request.getParameter("value");
            List<TrainClass> trainClassList = new ArrayList<TrainClass>(10);
            if (value == null || value.isEmpty()) {
                trainClassList = getAllClassOpen();
            } else {
                List<TrainClass> temp = trainClassService.SearchOpenClassByColumName("MaLopHoc", value);
                if (temp != null && !temp.isEmpty()) {
                    trainClassList.addAll(temp);
                }
                List<Subject> subjectList = subjectDAO.findByColumName("TenMH", value);
                for (int i = 0; i < subjectList.size(); i++) {
                    temp = trainClassService.SearchOpenClassByColumName("MaMH", subjectList.get(i).getId());
                    if (temp != null && !temp.isEmpty()) {
                        for (int j = 0; j < temp.size(); j++) {
                            TrainClass trainClass = temp.get(j);
                            if (!checkTrainClassInList(trainClassList, trainClass)) {
                                trainClassList.add(trainClass);
                            }
                        }
                    }
                }
                List<Lecturer> lectturerList = lectureDAO.findByColumName("HoTen", value);
                for (int i = 0; i < lectturerList.size(); i++) {
                    temp = trainClassService.SearchOpenClassByColumName("MaGV", lectturerList.get(i).getId());
                    if (temp != null && !temp.isEmpty()) {
                        for (int j = 0; j < temp.size(); j++) {
                            TrainClass trainClass = temp.get(j);
                            if (!checkTrainClassInList(trainClassList, trainClass)) {
                                trainClassList.add(trainClass);
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < trainClassList.size(); i++) {
                trainClassList.get(i).setSubjectName(subjectDAO.findById(trainClassList.get(i).getSubjectCode()).getSubjectName());
                trainClassList.get(i).setLectturerName(lectureDAO.findById(trainClassList.get(i).getLecturerCode()).getFullName());
            }
            //set current page
            if (trainClassList.size() % Constants.ELEMENT_PER_PAGE_DEFAULT == 0) {
                numPage = trainClassList.size() / Constants.ELEMENT_PER_PAGE_DEFAULT;
            } else {
                numPage = trainClassList.size() / Constants.ELEMENT_PER_PAGE_DEFAULT + 1;
            }
            if (currentPage > numPage) {
                currentPage = numPage;
            }

            List<TrainClass> result = getClassListByPage(trainClassList, currentPage);
            writeFilterTrainClass(result, out, currentPage);

        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param trainclasslist
     * @param trainClass
     * @return 
     */
    private boolean checkTrainClassInList(List<TrainClass> trainclasslist, TrainClass trainClass) {
        boolean result = false;
        for (int i = 0; i < trainclasslist.size(); i++) {
            if (trainclasslist.get(i).getId().getClassCode().equalsIgnoreCase(trainClass.getId().getClassCode())
                    && trainclasslist.get(i).getId().getYear().equalsIgnoreCase(trainClass.getId().getYear())
                    && trainclasslist.get(i).getId().getSemester() == trainClass.getId().getSemester()) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 
     * @param request
     * @param response 
     */
    private void FilterPageByYAS(HttpServletRequest request, HttpServletResponse response) {
        try {
            int numPage = 0;
            PrintWriter out = response.getWriter();
            int currentPage = Integer.parseInt(request.getParameter("curentPage"));
            String year = request.getParameter("year");
            int semester = Integer.parseInt(request.getParameter("semester"));
            List<TrainClass> trainclasslist = trainClassService.getAllClassOpenByYearAndSemester(year, semester);
            //set current page
            if (trainclasslist.size() % Constants.ELEMENT_PER_PAGE_DEFAULT == 0) {
                numPage = trainclasslist.size() / Constants.ELEMENT_PER_PAGE_DEFAULT;
            } else {
                numPage = trainclasslist.size() / Constants.ELEMENT_PER_PAGE_DEFAULT + 1;
            }
            if (currentPage > numPage) {
                currentPage = numPage;
            }

            List<TrainClass> trainClazzs = getClassListByPage(trainclasslist, currentPage);
            writeFilterTrainClass(trainClazzs, out, currentPage);
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param request
     * @param response 
     */
    private void FilterPageByInputValue(HttpServletRequest request, HttpServletResponse response) {
        try {
            int numPage = 0;
            PrintWriter out = response.getWriter();
            int currentPage = Integer.parseInt(request.getParameter("curentPage"));
            String value = request.getParameter("value");
            List<TrainClass> trainClassList = new ArrayList<TrainClass>(10);
            if (value == null || value.isEmpty()) {
                trainClassList = getAllClassOpen();
            } else {
                List<TrainClass> temp = trainClassService.SearchOpenClassByColumName("MaLopHoc", value);
                if (temp != null && !temp.isEmpty()) {
                    trainClassList.addAll(temp);
                }
                List<Subject> subjectList = subjectDAO.findByColumName("TenMH", value);
                for (int i = 0; i < subjectList.size(); i++) {
                    temp = trainClassService.SearchOpenClassByColumName("MaMH", subjectList.get(i).getId());
                    if (temp != null && !temp.isEmpty()) {
                        for (int j = 0; j < temp.size(); j++) {
                            TrainClass trainClass = temp.get(j);
                            if (!checkTrainClassInList(trainClassList, trainClass)) {
                                trainClassList.add(trainClass);
                            }
                        }
                    }
                }
                List<Lecturer> lectturerList = lectureDAO.findByColumName("HoTen", value);
                for (int i = 0; i < lectturerList.size(); i++) {
                    temp = trainClassService.SearchOpenClassByColumName("MaGV", lectturerList.get(i).getId());
                    if (temp != null && !temp.isEmpty()) {
                        for (int j = 0; j < temp.size(); j++) {
                            TrainClass trainClass = temp.get(j);
                            if (!checkTrainClassInList(trainClassList, trainClass)) {
                                trainClassList.add(trainClass);
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < trainClassList.size(); i++) {
                trainClassList.get(i).setSubjectName(subjectDAO.findById(trainClassList.get(i).getSubjectCode()).getSubjectName());
                trainClassList.get(i).setLectturerName(lectureDAO.findById(trainClassList.get(i).getLecturerCode()).getFullName());
            }
            //set current page
            if (trainClassList.size() % Constants.ELEMENT_PER_PAGE_DEFAULT == 0) {
                numPage = trainClassList.size() / Constants.ELEMENT_PER_PAGE_DEFAULT;
            } else {
                numPage = trainClassList.size() / Constants.ELEMENT_PER_PAGE_DEFAULT + 1;
            }
            if (currentPage > numPage) {
                currentPage = numPage;
            }

            List<TrainClass> result = getClassListByPage(trainClassList, currentPage);
            writeFilterTrainClass(result, out, currentPage);

        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param request
     * @param response 
     */
    private void FilterPageByFaculty(HttpServletRequest request, HttpServletResponse response) {
        try {
            int numpage = 0;
            PrintWriter out = response.getWriter();
            List<TrainClass> trainClassList = new ArrayList<TrainClass>(10);
            int currentPage = Integer.parseInt(request.getParameter("curentPage"));
            String facultyCode = request.getParameter("value");
            if (facultyCode.equalsIgnoreCase("All")) {
                trainClassList = getAllClassOpen();
            } else {
                trainClassList = classDAO.findOpenClassByFaculty(facultyCode);
            }
            // set current page
            if (trainClassList.size() % Constants.ELEMENT_PER_PAGE_DEFAULT == 0) {
                numpage = trainClassList.size() / Constants.ELEMENT_PER_PAGE_DEFAULT;
            } else {
                numpage = trainClassList.size() / Constants.ELEMENT_PER_PAGE_DEFAULT + 1;
            }
            if (currentPage > numpage) {
                currentPage = numpage;
            }

            List<TrainClass> result = getClassListByPage(trainClassList, currentPage);
            writeFilterTrainClass(result, out, currentPage);
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param request
     * @param response 
     */
    private void FilterByFaculty(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        List<TrainClass> trainClassList = null;
        try {
            String facultyCode = request.getParameter("value");
            if (facultyCode.equalsIgnoreCase("All")) {
                trainClassList = getAllClassOpen();
            } else {
                trainClassList = classDAO.findOpenClassByFaculty(facultyCode);
            }
            out = response.getWriter();
            List<TrainClass> result = getClassListByPage(trainClassList, 1);
            writeFilterTrainClass(result, out, 1);
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }

    /**
     * 
     * @param request
     * @param response 
     */
    private void searchTrainClass(HttpServletRequest request, HttpServletResponse response) {
        try {
            String value = request.getParameter("value");
            List<TrainClass> trainClassList = new ArrayList<TrainClass>(10);
            if (value == null || value.isEmpty()) {
                trainClassList = getAllClassOpen();
            } else {
                List<TrainClass> temp = trainClassService.SearchOpenClassByColumName("MaLopHoc", value);
                if (temp != null && !temp.isEmpty()) {
                    trainClassList.addAll(temp);
                }
                List<Subject> subjectList = subjectDAO.findByColumName("TenMH", value);
                for (int i = 0; i < subjectList.size(); i++) {
                    temp = trainClassService.SearchOpenClassByColumName("MaMH", subjectList.get(i).getId());
                    if (temp != null && !temp.isEmpty()) {
                        for (int j = 0; j < temp.size(); j++) {
                            TrainClass trainClass = temp.get(j);
                            if (!checkTrainClassInList(trainClassList, trainClass)) {
                                trainClassList.add(trainClass);
                            }
                        }
                    }
                }
                List<Lecturer> lectturerList = lectureDAO.findByColumName("HoTen", value);
                for (int i = 0; i < lectturerList.size(); i++) {
                    temp = trainClassService.SearchOpenClassByColumName("MaGV", lectturerList.get(i).getId());
                    if (temp != null && !temp.isEmpty()) {
                        for (int j = 0; j < temp.size(); j++) {
                            TrainClass trainClass = temp.get(j);
                            if (!checkTrainClassInList(trainClassList, trainClass)) {
                                trainClassList.add(trainClass);
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < trainClassList.size(); i++) {
                trainClassList.get(i).setSubjectName(subjectDAO.findById(trainClassList.get(i).getSubjectCode()).getSubjectName());
                trainClassList.get(i).setLectturerName(lectureDAO.findById(trainClassList.get(i).getLecturerCode()).getFullName());
            }
            PrintWriter out = response.getWriter();
            List<TrainClass> result = getClassListByPage(trainClassList, 1);
            writeFilterTrainClass(result, out, 1);

        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void filterTrainClass(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String year = request.getParameter("year");
        int semester = Integer.parseInt(request.getParameter("semester"));
        List<TrainClass> trainclasslist = trainClassService.getAllClassOpenByYearAndSemester(year, semester);
        List<TrainClass> trainClazzs = getClassListByPage(trainclasslist, 1);
        PrintWriter out = response.getWriter();
        writeFilterTrainClass(trainClazzs, out, 1);
    }

    private void writeFilterTrainClass(List<TrainClass> trainclasslist, PrintWriter out, int currentPgae) {
        out.println("<tr>"
                + "<th> STT</th>"
                + "<th> Lớp học </th>"
                + "<th> Môn học </th>"
                + "<th> Giảng viên </th>"
                + "<th> Thứ </th>"
                + "<th> Phòng </th>"
                + "<th> Đăng ký </th>"
                + "<th>Ngày thi</th>"
                + "<th>Hủy</th>"
                + "<th>Đóng</th>"
                + "</tr>");
        if ((trainclasslist == null) || trainclasslist.isEmpty()) {
            out.println("<tr>Không tìm thấy dữ liệu</tr>");
        } else {
            for (int i = 0; i < trainclasslist.size(); i++) {
                StringBuffer result = new StringBuffer();
                result.append("<tr><td>").append(((currentPgae - 1) * Constants.ELEMENT_PER_PAGE_DEFAULT + 1 + i)).append("</td>");
                result.append("<td><a href= '../../ManageClassController?action=detail&classID=").append(trainclasslist.get(i).getId().getClassCode()).append("&year=").append(trainclasslist.get(i).getId().getYear()).append("&semester=").append(trainclasslist.get(i).getId().getSemester()).append("'>").append(trainclasslist.get(i).getId().getClassCode()).append("</a></td>");
                result.append("<td> ").append(trainclasslist.get(i).getSubjectName()).append("</td>");
                result.append(" <td> ").append(trainclasslist.get(i).getLectturerName()).append(" </td>");
                result.append("<td> ").append(trainclasslist.get(i).getStudyDate()).append("</td>");
                result.append("<td> ").append(trainclasslist.get(i).getClassRoom()).append(" </td>");
                result.append("<td> ").append(trainclasslist.get(i).getNumOfStudentReg()).append("/").append(trainclasslist.get(i).getNumOfStudent()).append(" </td>");
                if (trainclasslist.get(i).getTestDate() == null) {
                    result.append("<td>Chưa có</td>");
                } else {
                    result.append("<td>").append(trainclasslist.get(i).getTestDate()).append("</td>");
                }
                result.append("<td><a href='../../ManageClassController?action=cancel&classID=").append(trainclasslist.get(i).getId().getClassCode()).append("&semester=").append(trainclasslist.get(i).getId().getSemester()).append("&year=").append(trainclasslist.get(i).getId().getYear()).append("'>Hủy</a></td>");
                if (trainclasslist.get(i).getUpdateScore() == 1) {
                    String method = String.format(" onclick=closeClass('%s','%s','%s')", trainclasslist.get(i).getId().getClassCode(), trainclasslist.get(i).getId().getSemester(), trainclasslist.get(i).getId().getYear());
                    result.append("<td><span class='atag'" + method + ">Đóng</span></td>");
                } else {
                    result.append("<td></td>");
                }
                result.append("</tr>");
                out.println(result.toString());
            }
        }
    }

    private void preUpdateTrainClass(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String path = "";
        try {
            HttpSession session = request.getSession();
            String ClassCode = (String) request.getParameter("classId");
            String year = (String) request.getParameter("year");
            int semester = Integer.parseInt((String) request.getParameter("semester"));
            TrainClassID classID = new TrainClassID(ClassCode, year, semester);
            TrainClass trainClass = trainClassService.getClassInfomation(classID);
            ArrayList<Lecturer> lecturers = (ArrayList<Lecturer>) lectureDAO.findByColumName("MaKhoa",
                    subjectDAO.findById(classDAO.findById(classID).getSubjectCode()).getFacultyCode());
            if ((lecturers != null) && (!lecturers.isEmpty())) {
                ArrayList<LecturerWeb> lws = new ArrayList<LecturerWeb>(10);

                for (Lecturer l : lecturers) {
                    LecturerWeb lw = new LecturerWeb(l.getId(), l.getFullName());
                    lws.add(lw);
                }
                session.setAttribute("lecturers", lws);
            }
            session.setAttribute("trainclass", trainClass);
            path = "./jsps/PDT/UpdateTrainClass.jsp";
        } catch (Exception ex) {
            path = "./jsps/Message.jsp";
        }
        response.sendRedirect(path);
    }

    /**
     * this function to update train class
     * @param request
     * @param response 
     */
    private void updateTrainClass(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = "";
        try {
            HttpSession session = request.getSession();
            String classCode = (String) request.getParameter("classId");
            String lecturerCode = (String) request.getParameter("lecturer");
            int studyDate = Integer.parseInt((String) request.getParameter("Date"));
            int shift = Integer.parseInt((String) request.getParameter("Shift"));
            String room = (String) request.getParameter("room");
            Date testDate = DateTimeUtil.parse(request.getParameter("testDate"));
            String testRoom = (String) request.getParameter("testroom");
            String testTime = (String) request.getParameter("hh") + ":" + (String) request.getParameter("hh");
            String year = (String) request.getParameter("year");
            int semester = Integer.parseInt((String) request.getParameter("semester"));
            Date startDate = DateTimeUtil.parse(request.getParameter("startDate"));
            Date endDate = DateTimeUtil.parse(request.getParameter("endDate"));

            TrainClassID classID = new TrainClassID(classCode, year, semester);
            TrainClass trainClass = trainClassService.getClassInfomation(classID);
            trainClass.setLecturerCode(lecturerCode);
            trainClass.setStudyDate(studyDate);
            trainClass.setShift(shift);
            trainClass.setClassRoom(room);
            trainClass.setTestDate(testDate);
            trainClass.setTestRoom(testRoom);
            trainClass.setTestHours(testTime);
            trainClass.setStartDate(startDate);
            trainClass.setEndDate(endDate);
            trainClass = classDAO.update(trainClass);

            session.setAttribute("trainclass", trainClass);
            session.setAttribute("students", getStudents(classCode, year, semester));

            path = "./jsps/PDT/TrainClassDetail.jsp";
        } catch (Exception ex) {
            path = "./jsps/Message.jsp";
        }
        response.sendRedirect(path);
    }

    /**
     * This function use to get class to use view detail
     * @param request
     * @param response 
     */
    private void viewDetailClass(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = "";
        try {
            String classCode = (String) request.getParameter("classID");
            String year = (String) request.getParameter("year");
            int semester = Integer.parseInt((String) request.getParameter("semester"));
            TrainClassID classID = new TrainClassID(classCode, year, semester);
            TrainClass trainClass = trainClassService.getClassInfomation(classID);
            HttpSession session = request.getSession();
            session.setAttribute("trainclass", trainClass);
            session.setAttribute("students", getStudents(classCode, year, semester));
            path = "./jsps/PDT/TrainClassDetail.jsp";
        } catch (Exception ex) {
            path = "./jsps/Message.jsp";
        }
        response.sendRedirect(path);
    }
    
    List<Student> getStudents(String trainClassID, String year, int semeter) {
        if (StringUtils.isEmpty(trainClassID) || 
                StringUtils.isEmpty(year)) {
            return null;
        }

        try {
            RegistrationDAO regDao = DAOFactory.getRegistrationDAO();
            List<Registration> regs = regDao.findByColumNames(new String[]{"MaLopHoc", "HocKy", "NamHoc"},
                    new Object[] {trainClassID, semeter, year});
            if (regs == null || regs.isEmpty()) {
                return null;
            }
            
            List<String> studentIds = new ArrayList<String>(10);
            for (Registration reg : regs) {
                studentIds.add(reg.getId().getStudentCode());
            }
            return DAOFactory.getStudentDao().findByIds(studentIds);
            
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Default action
     * This just list trainclass with pagging
     * and send it back to callback function.
     * 
     * @param request request object
     * @param response respone object
     */
    private void defaultAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int numPage = 0;
            List<TrainClass> classList = getAllClassOpen();
            List<TrainClass> trainClazzs = getClassListByPage(classList, 1);
            List<String> yearList = getYear(trainClazzs);
            List<Faculty> facultyList = DAOFactory.getFacultyDao().findAll();
            if (classList != null && !classList.isEmpty()) {
                if (classList.size() % Constants.ELEMENT_PER_PAGE_DEFAULT == 0) {
                    numPage = classList.size() / Constants.ELEMENT_PER_PAGE_DEFAULT;
                } else {
                    numPage = classList.size() / Constants.ELEMENT_PER_PAGE_DEFAULT + 1;
                }
            } else {
                numPage = 0;
            }
            HttpSession session = request.getSession();
            session.setAttribute("train-clazzs", trainClazzs);
            session.setAttribute("yearList", yearList);
            session.setAttribute("numpage", classList.size());
            session.setAttribute("facultyList", facultyList);
            session.setAttribute("numpage", numPage);


            String path = "./jsps/PDT/OpenTrainClassManager.jsp";
            response.sendRedirect(path);

            return;
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<TrainClass> getClassListByPage(List<TrainClass> classList, int currentPage) {
        List<TrainClass> result = new ArrayList<TrainClass>(Constants.ELEMENT_PER_PAGE_DEFAULT);
        if (classList.size() <= Constants.ELEMENT_PER_PAGE_DEFAULT) {
            result = classList;
        } else {
            int beginIndex = (currentPage - 1) * Constants.ELEMENT_PER_PAGE_DEFAULT;
            int endIndex = (currentPage - 1) * Constants.ELEMENT_PER_PAGE_DEFAULT + Constants.ELEMENT_PER_PAGE_DEFAULT;
            for (int i = 0; i < classList.size(); i++) {
                if (i >= beginIndex && i < endIndex) {
                    result.add(classList.get(i));
                }
            }
        }
        return result;
    }

    /**
     * Get all class is opening
     * @return 
     */
    private List<TrainClass> getAllClassOpen() {
        List<TrainClass> trainClazzs = trainClassService.getAllClassOpen();
        try {
            setSubjectAndLecturer(trainClazzs);
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trainClazzs;
    }

    /**
     * 
     * @param trainClassList
     * @return 
     */
    private List<String> getYear(List<TrainClass> trainClassList) {
        ArrayList<String> yearList = new ArrayList<String>();
        for (int i = 0; i < trainClassList.size(); i++) {
            if (checkStringExist(trainClassList.get(i).getId().getYear(), yearList) == false) {
                yearList.add(trainClassList.get(i).getId().getYear());
            }
        }

        return yearList;
    }

    private boolean checkStringExist(String value, List<String> list) {
        boolean result = false;
        for (int i = 0; i < list.size(); i++) {
            if (value.equalsIgnoreCase(list.get(i))) {
                result = true;
            }
        }
        return result;
    }

    private void retrieveTrainClassForAjaxQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {

        /*int currentPage = 1;
        try {
        currentPage = Integer.parseInt((String)request.getAttribute("current-page"));
        } catch (Exception ex) {
        currentPage = 1;
        }*/

        List<TrainClass> trainClazzs = trainClassService.getTrainClass(Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
        //
        // TODO: Should accept only TrainClass of current semeter.
        //
        if ((trainClazzs != null) && (!trainClazzs.isEmpty())) {
            HttpSession session = request.getSession();
            session.setAttribute("train-clazzs", trainClazzs);
        }

        String path = "./jsps/PDT/TrainClassManager.jsp";
        response.sendRedirect(path);

        return;
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
        return "This Serverlet support functionality of Class controller.";
    }// </editor-fold>

    /**
     * Create new Class
     * Information about new one get from Request object.
     */
    private ExecuteResult createNewTrainClass(HttpServletRequest req) {
        // Information need:
        // MaLopHoc	HocKy	NamHoc	MaMH	MaGV	SLSV	SLDK	NgayHoc	CaHoc	PhongHoc
        String id = req.getParameter("classcode");
        int semester = Constants.CURRENT_SEMESTER;
        String year = Constants.CURRENT_YEAR;
        String subjectCode = req.getParameter("subject");
        String lectureCode = req.getParameter("lecturer");
        int SLSV = Integer.parseInt(req.getParameter("slsv"));
        int SLDK = 0;
        int date = Integer.parseInt(req.getParameter("Date"));
        int shift = Integer.parseInt(req.getParameter("Shift"));
        String room = req.getParameter("room");
        Date startDate = DateTimeUtil.parse(req.getParameter("startDate"));
        Date endDate = DateTimeUtil.parse(req.getParameter("endDate"));

        TrainClass clazz = null;
        try {
            TrainClassID classID = new TrainClassID(id, year, semester);
            clazz = new TrainClass(id, year, semester, subjectCode, lectureCode,
                    room, SLSV, SLDK, date, shift, null, "", "", startDate, endDate);
            clazz.setId(classID);

            return trainClassService.addNewTrainClass(clazz);
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
            return new ExecuteResult(false, "Lỗi server: " + ex.toString());
        }

    }

    private ExecuteResult checkCreate(HttpServletRequest req) {
        String id = req.getParameter("classcode");
        int semester = Constants.CURRENT_SEMESTER;
        String year = Constants.CURRENT_YEAR;
        String subjectCode = req.getParameter("subject");
        String lectureCode = req.getParameter("lecturer");
        int SLSV = Integer.parseInt(req.getParameter("slsv"));
        int SLDK = 0;
        int date = Integer.parseInt(req.getParameter("Date"));
        int shift = Integer.parseInt(req.getParameter("Shift"));
        String room = req.getParameter("room");
        Date startDate = DateTimeUtil.parse(req.getParameter("startDate"));
        Date endDate = DateTimeUtil.parse(req.getParameter("endDate"));

        TrainClass clazz = null;
        try {
            TrainClassID classID = new TrainClassID(id, year, semester);
            clazz = new TrainClass(id, year, semester, subjectCode, lectureCode,
                    room, SLSV, SLDK, date, shift, null, "", "", startDate, endDate);
            clazz.setId(classID);

            return trainClassService.checkOpenClassCondition(clazz);
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
            return new ExecuteResult(false, "Lỗi server: " + ex.toString());
        }

    }

    private void createNewClassRespond(HttpServletResponse resp, TrainClass clazz) {
        // Just write the result
        // If clzz is NUll, just notify an error message.
    }

    private void preCreateNewTrainClass(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session == null) {
            //
        }

        try {
            ArrayList<Subject> subjects = (ArrayList<Subject>) subjectDAO.findAll();
            ArrayList<Lecturer> lecturers = (ArrayList<Lecturer>) lectureDAO.findAll();
            ArrayList<Faculty> faculty = (ArrayList<Faculty>) facultyDAO.findAll();

            if ((subjects != null) && (!subjects.isEmpty())) {
                ArrayList<SubjectWeb> sws = new ArrayList<SubjectWeb>(10);
                for (Subject s : subjects) {
                    SubjectWeb sw = new SubjectWeb(s.getId(), s.getSubjectName());
                    sws.add(sw);
                }
                session.setAttribute("subjects", sws);
            }

            if ((lecturers != null) && (!lecturers.isEmpty())) {
                ArrayList<LecturerWeb> lws = new ArrayList<LecturerWeb>(10);

                for (Lecturer l : lecturers) {
                    LecturerWeb lw = new LecturerWeb(l.getId(), l.getFullName());
                    lws.add(lw);
                }
                session.setAttribute("lecturers", lws);
            }
            session.setAttribute("faculty", faculty);
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param trainClass
     * @throws Exception 
     */
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
     * @param result
     * @param out 
     */
    private void writeRespondCreateNewTrainClass(ExecuteResult result, PrintWriter out) {
        out.println(result.getMessage());
        if (result.isIsSucces() && (result.getData() == null)) {
            return;
        } else if (result.isIsSucces()) {
            out.println("<b>Tạo mới thành công lớp học:</b>");
            TrainClass data = (TrainClass) result.getData();
            out.println("<table id = \"table-list-train-class\" name = \"table-list-train-class\" class=\"general-table\">");

            out.println("<tr>"
                    + //"<th> STT </th>" +
                    "<th> Lớp học </th>"
                    + "<th> Môn học </th>"
                    + "<th> Giảng viên </th>"
                    + "<th> Thứ </th>"
                    + "<th> Phòng </th>"
                    + "<th> Số lượng </th>"
                    + "</tr>");
            out.println("<td> " + data.getId().getClassCode() + " </td>");
            out.println("<td> " + data.getSubjectName() + " </td>");
            out.println("<td> " + data.getLectturerName() + " </td>");
            out.println("<td> " + data.getStudyDate() + " </td>");
            out.println("<td> " + data.getClassRoom() + " </td>");
            out.println("<td> " + data.getNumOfStudent() + " </td>");
            out.println("</tr>");
            out.println("</table>");
        }
    }

    /**
     * 
     */
    public enum ClassFunctionSupported {

        DEFAULT("default"), // List first page of class opened.
        PRECREATE("pre_create"),
        CHECK_CREATE("check_create"),
        CREATE("create"), // Create new class form support
        DELETE("delete"), // Remove class
        DETAIL("detail"), // view detail class
        PREUPDATE("pre_update"), // prepare update class
        CANCEL("Cancel"), // Cancel train class
        FILTER_BY_YAS("filterByYAS"),
        FILTER_BY_FACULTY("filterByFaculty"),
        FILTER_PAGE_BY_INPUT("FilterPageByInput"),
        FILTER_PAGE_BY_FACULTY("FilterPageByFaculty"),
        FILTER_PAGE_BY_YAS("FilterPageByYAS"),
        CLOSE_BY_INPUT("CloseByInput"),
        CLOSE_BY_FACULTY("CloseByFaculty"),
        CLOSE_BY_YAS("ClosePageByYAS"),
        FILTER_SUBJECT_BY_FACULTY("FilterClassByFaculty"),
        FILTER_LECTURER_BY_SUBJECT("FilterLecturerBySubject"),
        SEARCH("search"),
        AUTOCANCEL("autoCancel"),
        MOVESTUDENT("moveStudent"),
        UPDATE("update");   // Update
        private String description;

        ClassFunctionSupported(String description) {
            this.description = description;
        }

        public String getValue() {
            return description;
        }
    }
}
