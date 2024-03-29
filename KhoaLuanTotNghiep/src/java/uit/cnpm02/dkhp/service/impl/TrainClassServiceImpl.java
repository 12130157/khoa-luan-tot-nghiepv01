package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.DetailTrainDAO;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.RuleDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.DAO.TrainProgramDAO;
import uit.cnpm02.dkhp.model.DetailTrain;
import uit.cnpm02.dkhp.model.Rule;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.model.TrainProDetail;
import uit.cnpm02.dkhp.model.TrainProgram;
import uit.cnpm02.dkhp.service.ITrainClassService;
import uit.cnpm02.dkhp.service.TrainClassStatus;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.Message;

/**
 * 
 * @author thanh
 */
public class TrainClassServiceImpl implements ITrainClassService {
    // DAO definition ///
    private TrainClassDAO classDAO = DAOFactory.getTrainClassDAO();
    private SubjectDAO subjectDAO = DAOFactory.getSubjectDao();
    private LecturerDAO lectureDAO = DAOFactory.getLecturerDao();
    private RuleDAO ruleDao = DAOFactory.getRuleDao();
    private String classStatus = "TrangThai";
    
    
    private static Object mutex = new Object();
    
    @Override
    public List<TrainClass> getTrainClass(String year, int semester) {
        List<TrainClass> results = new ArrayList<TrainClass>(10);
        try {
            //if (currentPage < 1) {
            //    currentPage = 1;
            //}
            /*trainClazzs = classDAO.findAll(Constants.ELEMENT_PER_PAGE_DEFAULT,
            currentPage,
            null, null);
             */
            // trainClazzs = classDAO.findByColumNames(new String[]{"NamHoc", "HocKy"}, new Object[]{year,semester});
           
            try {
                List<String> columnName = new ArrayList<String>(10);
                List<Object> values = new ArrayList<Object>(10);

                if ((year != null)
                        && !year.isEmpty()
                        && !year.equalsIgnoreCase("all")) {
                    columnName.add("NamHoc");
                    values.add(year);
                }
                if (semester > 0) {
                    columnName.add("HocKy");
                    values.add(semester);
                }

                String[] strColumnNames = (String[]) columnName.toArray(
                        new String[columnName.size()]);

                if ((strColumnNames == null) || (strColumnNames.length <= 0)) {
                    results = getAllClassOpen();
                } else {
                    results = classDAO.findByColumNames(
                            strColumnNames, values.toArray());
                }
            } catch (Exception ex) {
                Logger.getLogger(
                        ReporterImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Update External Information
            if ((results != null) && !results.isEmpty()) {
                try {
                    for (TrainClass t : results) {
                        String subName = subjectDAO.findById(t.getSubjectCode()).getSubjectName();
                        String lecturerName = lectureDAO.findById(t.getLecturerCode()).getFullName();

                        t.setSubjectName(subName);
                        t.setLectturerName(lecturerName);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    @Override
    public List<TrainClass> getTrainClass() {
        List<TrainClass> trainClazzs = new ArrayList<TrainClass>(10);
        try {
            trainClazzs = classDAO.findAll();
            // Update External Information
            if ((trainClazzs != null) && !trainClazzs.isEmpty()) {
                try {
                    for (TrainClass t : trainClazzs) {
                        String subName = subjectDAO.findById(t.getSubjectCode()).getSubjectName();
                        String lecturerName = lectureDAO.findById(t.getLecturerCode()).getFullName();

                        t.setSubjectName(subName);
                        t.setLectturerName(lecturerName);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trainClazzs;
    }

    @Override
    public ExecuteResult addNewTrainClass(TrainClass obj) {
        ExecuteResult result = null;
        try {
            result = internal_checkCreateNewTrainClass(obj);
            
            if (!result.isIsSucces()) {
                return result;
            }

            classDAO.add(obj);
            String className = subjectDAO.findById(obj.getSubjectCode()).getSubjectName();
            String lectureName = lectureDAO.findById(obj.getLecturerCode()).getFullName();
            obj.setSubjectName(className);
            obj.setLectturerName(lectureName);
            result.setData(obj);
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            result.setIsSucces(false);
            result.setMessage(Message.ADD_TRAINCLASS_ERROR);
        }
        
        return result;
    }
    
    @Override
    public ExecuteResult checkOpenClassCondition(TrainClass obj) {
        try {
            ExecuteResult er = internal_checkCreateNewTrainClass(obj);
            if (er.isIsSucces()) {
                er.setMessage("Lớp hợp lệ.");
            }
            return er;
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return new ExecuteResult(false, "Lỗi server: " + ex.toString());
        }
    }

    private ExecuteResult internal_checkCreateNewTrainClass(TrainClass obj) throws Exception {
        //ExecuteResult result = new ExecuteResult(true, Message.ADD_TRAINCLASS_SUCCSESS);
        //boolean checkPass = true;
         //
            //Check subject in program of faculty            
            //
            String subjectCode = obj.getSubjectCode();
            if(subjectDAO.findById(subjectCode).getType()==0){
            List<TrainProDetail> listDetailTrain = DAOFactory.getTrainProgDetailDAO().findByColumName("MaMH", subjectCode);    
            //String facultyCode= subjectDAO.findById(subjectCode).getFacultyCode();
            //List<String> proCodeList = getListProCodeByFaculty(facultyCode);
            //List<TrainProDetail> detailTrainProList = DAOFactory.getTrainProgDetailDAO().findByColumNameAndValueList("MaCTDT", proCodeList);
            //if(checkSubjectInListProgram(subjectCode, detailTrainProList)==false){
            if(listDetailTrain == null || listDetailTrain.isEmpty()){
              String message = "<b>Lớp không hợp lệ: Môn học " + subjectCode
                        + " không thuộc chương trình đào tạo nào</b>";
                        //result.setMessage(Message.ADD_TRAINCLASS_ERROR_ROOM_DUPLECATE);
                return new ExecuteResult(false, message);
            }
            }
            TrainClass classExisted;
            classExisted = classDAO.findById(obj.getId());
            if (classExisted != null) {
                return new ExecuteResult(false, Message.ADD_TRAINCLASS_ERROR_CLASS_EXISTED);
                //result.setMessage(Message.ADD_TRAINCLASS_ERROR_CLASS_EXISTED);
                //checkPass = false;
            }
            
            //
            //Check so SV toi da, toi thieu
            List<Rule> rules = ruleDao.findAll();
            int numMax = 120;
            int numMin = 30;
            if ((rules != null) && !rules.isEmpty()) {
                for (Rule r : rules) {
                    try {
                        if (r.getId().equalsIgnoreCase("SoSinhVienToiDa")) {
                            numMax = (int) r.getValue();
                        } else if (r.getId().equalsIgnoreCase("SoSinhVienToiThieu")) {
                            numMin = (int) r.getValue();
                        }
                    } catch (Exception ex) {
                    }
                }
            }
            
            if ((obj.getNumOfStudent() < numMin)
                ||(obj.getNumOfStudent() > numMax)) {
                String msg = "Số SV không thỏa mãn (" + numMin + " < SLSV < " + numMax +").";
                return new ExecuteResult(false, msg);
            }
            //////
            // Room - In a time --> 1 train class
            //
            List<TrainClass> existedClasses = classDAO.findByClassRoomAndTime(
                    obj.getClassRoom(), obj.getStudyDate(),
                    obj.getShift(), TrainClassStatus.OPEN.getValue());
            if ((existedClasses != null) && (!existedClasses.isEmpty())) {
                String message = "<b>Lớp không hợp lệ: Tại phòng " + obj.getClassRoom()
                        + ", ca " + obj.getShift() + ", ngày thứ " + obj.getStudyDate()
                        + " có lớp " + existedClasses.get(0).getId().getClassCode() + "</b>";
                       //result.setMessage(Message.ADD_TRAINCLASS_ERROR_ROOM_DUPLECATE);
                //result.setMessage(message);
                //checkPass = false;
                return new ExecuteResult(false, message);
            }
            //////
            // Lecturer - In a time --> 1 train class
            //
            existedClasses = classDAO.findByLecturerAndTime(obj.getLecturerCode(),
                    obj.getStudyDate(), obj.getShift(),
                    TrainClassStatus.OPEN.getValue());
            if ((existedClasses != null) && (!existedClasses.isEmpty())) {
                String message = "<b>Lớp không hợp lệ: Giảng viên " + obj.getLecturerCode()
                        + " bị trùng giờ tại lớp: " + existedClasses.get(0).getId().getClassCode() + "</b>";
                        //result.setMessage(Message.ADD_TRAINCLASS_ERROR_ROOM_DUPLECATE);
                //result.setMessage(message);
                //result.setMessage(Message.ADD_TRAINCLASS_ERROR_LECTURER_DUPLECATE);
                //checkPass = false;
                 return new ExecuteResult(false, message);
            }

            //if (!checkPass) {
              //  result.setIsSucces(false);
              //  return result;
           // }
            
            return new ExecuteResult(true, Message.ADD_TRAINCLASS_SUCCSESS);
    }
    private boolean checkSubjectInListProgram(String subjectCode, List<TrainProDetail> detailTrainProList){
        boolean result = false;
        for(int i=0; i<detailTrainProList.size(); i++){
            if(detailTrainProList.get(i).getId().getSubjectID().equalsIgnoreCase(subjectCode)){
             result=true;
             break;
            }                
        }
        return result;
    }
    private List<String> getListProCodeByFaculty(String facultyCode){
        List<String> result = new ArrayList<String>(20);
        try {
            TrainProgramDAO trainDao= DAOFactory.getTrainProgramDAO();
            List<TrainProgram> trainList = trainDao.findByColumName("MaKhoa", facultyCode);
            for(int i=0; i<trainList.size(); i++){
                if(checkItemInList(trainList.get(i).getId(), result)==false){
                    result.add(trainList.get(i).getId());
                }
            }
            return result;
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    private boolean checkItemInList(String item, List<String> list){
        boolean result= false;
        for(int i =0; i<list.size(); i++){
            if(list.get(i).equalsIgnoreCase(item)){
                result=true;
                break;
            }
        }
        return result;
    }
    @Override
    public ExecuteResult updateTrainClass(TrainClass obj) {
        //TODO: implementation.
        ExecuteResult result = new ExecuteResult();
        try {
            //
            // Query out real object from database
            //
            TrainClass trainClass = classDAO.findById(obj.getId());

            
            //
            // Check if object changed or not
            //
            
            //
            // Update to database
            //
            
            
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return result;
    }

    @Override
    public boolean isExisted(TrainClass obj) {
        // TODO: Implement
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean validate(TrainClass obj) {
        if ((obj == null) || (obj.getId() == null)) // Not instance
            return false;
        try {
            if (classDAO.findById(obj.getId()) != null) // Class existed
                return false;
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }

    @Override
    public TrainClass getClassInfomation(TrainClassID obj) {
        try {
            TrainClass trainClass = classDAO.findById(obj);
            trainClass.setLectturerName(lectureDAO.findById(trainClass.getLecturerCode()).getFullName());
            trainClass.setSubjectName(subjectDAO.findById(trainClass.getSubjectCode()).getSubjectName());
            return trainClass;
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
       
    }

    @Override
    public List<TrainClass> getCurrentTrainClass(String lecturer) {
        try {
            List<TrainClass> results = classDAO.findByColumNames(
                    new String[]{"MaGV", "TrangThai"},
                    new Object[] {lecturer, TrainClassStatus.OPEN.getValue()});
            updateTrainCLassInfo(results);
            sortTrainClassBySemeterAndYear(results);
            return results;
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private void updateTrainCLassInfo(List<TrainClass> tcs) {
        if ((tcs != null) && !tcs.isEmpty()) {
            try {
                    for (TrainClass t : tcs) {
                        String subName = subjectDAO.findById(t.getSubjectCode()).getSubjectName();
                        //String lecturerName = lectureDAO.findById(t.getLecturerCode()).getFullName();

                        t.setSubjectName(subName);
                        //t.setLectturerName(lecturerName);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(TrainClassServiceImpl.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
    }

    @Override
    public List<TrainClass> getAllClassOpen() {
        try {
            List<TrainClass> result = classDAO.findByOther(classStatus, String.valueOf(TrainClassStatus.OPEN.getValue()));
            return result;
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<TrainClass> getAllClassClose() {
       try {
            List<TrainClass> result = classDAO.findByOther(classStatus, String.valueOf(TrainClassStatus.CLOSE.getValue()));
            return result;
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

   @Override
    public List<TrainClass> getAllClassOpenByYearAndSemester(String year, int semester) {
        List<TrainClass> results = new ArrayList<TrainClass>(10);
        try {
           try {
                List<String> columnName = new ArrayList<String>(10);
                List<Object> values = new ArrayList<Object>(10);

                if ((year != null)
                        && !year.isEmpty()
                        && !year.equalsIgnoreCase("all")) {
                    columnName.add("NamHoc");
                    values.add(year);
                }
                if (semester > 0) {
                    columnName.add("HocKy");
                    values.add(semester);
                }
                columnName.add(classStatus);
                values.add(TrainClassStatus.OPEN.getValue());
                String[] strColumnNames = (String[]) columnName.toArray(
                        new String[columnName.size()]);

                if ((strColumnNames == null) || (strColumnNames.length <= 0)) {
                    results = getAllClassOpen();
                } else {
                    results = classDAO.findByColumNames(
                            strColumnNames, values.toArray());
                }
            } catch (Exception ex) {
                Logger.getLogger(
                        ReporterImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Update External Information
            if ((results != null) && !results.isEmpty()) {
                try {
                    for (TrainClass t : results) {
                        String subName = subjectDAO.findById(t.getSubjectCode()).getSubjectName();
                        String lecturerName = lectureDAO.findById(t.getLecturerCode()).getFullName();

                        t.setSubjectName(subName);
                        t.setLectturerName(lecturerName);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }
    @Override
    public List<TrainClass> getAllClassCloseByYearAndSemester(String year, int semester) {
        List<TrainClass> results = new ArrayList<TrainClass>(10);
        try {
           try {
                List<String> columnName = new ArrayList<String>(10);
                List<Object> values = new ArrayList<Object>(10);

                if ((year != null)
                        && !year.isEmpty()
                        && !year.equalsIgnoreCase("all")) {
                    columnName.add("NamHoc");
                    values.add(year);
                }
                if (semester > 0) {
                    columnName.add("HocKy");
                    values.add(semester);
                }
                columnName.add(classStatus);
                values.add(TrainClassStatus.CLOSE.getValue());
                String[] strColumnNames = (String[]) columnName.toArray(
                        new String[columnName.size()]);

                if ((strColumnNames == null) || (strColumnNames.length <= 0)) {
                    results = getAllClassOpen();
                } else {
                    results = classDAO.findByColumNames(
                            strColumnNames, values.toArray());
                }
            } catch (Exception ex) {
                Logger.getLogger(
                        ReporterImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Update External Information
            if ((results != null) && !results.isEmpty()) {
                try {
                    for (TrainClass t : results) {
                        String subName = subjectDAO.findById(t.getSubjectCode()).getSubjectName();
                        String lecturerName = lectureDAO.findById(t.getLecturerCode()).getFullName();

                        t.setSubjectName(subName);
                        t.setLectturerName(lecturerName);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    @Override
    public List<TrainClass> SearchOpenClassByColumName(String columnName, String value) {
       List<TrainClass> results = new ArrayList<TrainClass>(10);
        try {
           try {
                List<String> columnNames = new ArrayList<String>(10);
                List<Object> values = new ArrayList<Object>(10);

                if (columnName != null) {
                    columnNames.add(columnName);
                    values.add(value);
                }
                columnNames.add(classStatus);
                values.add(TrainClassStatus.OPEN.getValue());
                String[] strColumnNames = (String[]) columnNames.toArray(
                        new String[columnNames.size()]);

                if ((strColumnNames == null) || (strColumnNames.length <= 0)) {
                    results = getAllClassOpen();
                } else {
                    results = classDAO.searchByColumNames(
                            strColumnNames, values.toArray());
                }
            } catch (Exception ex) {
                Logger.getLogger(
                        ReporterImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Update External Information
            if ((results != null) && !results.isEmpty()) {
                try {
                    for (TrainClass t : results) {
                        String subName = subjectDAO.findById(t.getSubjectCode()).getSubjectName();
                        String lecturerName = lectureDAO.findById(t.getLecturerCode()).getFullName();

                        t.setSubjectName(subName);
                        t.setLectturerName(lecturerName);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }
    @Override
    public List<TrainClass> SearchCloseClassByColumName(String columnName, String value) {
       List<TrainClass> results = new ArrayList<TrainClass>(10);
        try {
           try {
                List<String> columnNames = new ArrayList<String>(10);
                List<Object> values = new ArrayList<Object>(10);

                if (columnName != null) {
                    columnNames.add(columnName);
                    values.add(value);
                }
                columnNames.add(classStatus);
                values.add(TrainClassStatus.CLOSE.getValue());
                String[] strColumnNames = (String[]) columnNames.toArray(
                        new String[columnNames.size()]);

                if ((strColumnNames == null) || (strColumnNames.length <= 0)) {
                    results = getAllClassOpen();
                } else {
                    results = classDAO.searchByColumNames(
                            strColumnNames, values.toArray());
                }
            } catch (Exception ex) {
                Logger.getLogger(
                        ReporterImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Update External Information
            if ((results != null) && !results.isEmpty()) {
                try {
                    for (TrainClass t : results) {
                        String subName = subjectDAO.findById(t.getSubjectCode()).getSubjectName();
                        String lecturerName = lectureDAO.findById(t.getLecturerCode()).getFullName();

                        t.setSubjectName(subName);
                        t.setLectturerName(lecturerName);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    @Override
    public List<TrainClass> getClosedTrainClass(String lecturer) {
        try {
            List<TrainClass> results = classDAO.findByColumNames(
                    new String[]{"MaGV", "TrangThai"},
                    new Object[] {lecturer, TrainClassStatus.CLOSE.getValue()});
            /*if ((results != null) && !results.isEmpty()) {
                for (int i = 0; i < results.size(); i++) {
                    if (results.get(i).getStatus().getValue() 
                            != TrainClassStatus.OPEN.getValue()) {
                        results.remove(i);
                        i--;
                    }
                }
            }*/
            
            updateTrainCLassInfo(results);
            sortTrainClassBySemeterAndYear(results);
            return results;
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private void sortTrainClassBySemeterAndYear(List<TrainClass> tcs) {
        Collections.sort(tcs, new Comparator<TrainClass>() {

            @Override
            public int compare(TrainClass o1, TrainClass o2) {
                return o1.getStartDate().before(o2.getStartDate()) ? 1 : -1;
            }
        });
    }

    @Override
    public List<TrainClass> getClassListByLecturerAndYear(String lecturerID, String year) {
        List<TrainClass> results = new ArrayList<TrainClass>(10);
        try {
           try {
                List<String> columnName = new ArrayList<String>(10);
                List<Object> values = new ArrayList<Object>(10);

                if ((lecturerID != null)
                        && !lecturerID.isEmpty()) {
                    columnName.add("MaGV");
                    values.add(lecturerID);
                }
                if ((year != null)
                        && !year.isEmpty()) {
                    columnName.add("NamHoc");
                    values.add(year);
                }
                columnName.add(classStatus);
                values.add(TrainClassStatus.OPEN.getValue());
                String[] strColumnNames = (String[]) columnName.toArray(
                        new String[columnName.size()]);

                if ((strColumnNames == null) || (strColumnNames.length <= 0)) {
                    results = getAllClassOpen();
                } else {
                    results = classDAO.findByColumNames(
                            strColumnNames, values.toArray());
                }
            } catch (Exception ex) {
                Logger.getLogger(
                        ReporterImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Update External Information
            if ((results != null) && !results.isEmpty()) {
                try {
                    for (TrainClass t : results) {
                        String subName = subjectDAO.findById(t.getSubjectCode()).getSubjectName();
                        String lecturerName = lectureDAO.findById(t.getLecturerCode()).getFullName();

                        t.setSubjectName(subName);
                        t.setLectturerName(lecturerName);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

}
