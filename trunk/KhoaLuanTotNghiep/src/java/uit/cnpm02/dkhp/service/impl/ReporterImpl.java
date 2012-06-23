package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.RegistrationDAO;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.Registration;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.service.IReporter;
import uit.cnpm02.dkhp.service.TrainClassStatus;
import uit.cnpm02.dkhp.utilities.Constants;

/**
 *
 * @author LocNguyen
 */
public class ReporterImpl implements IReporter {
    private StudentDAO studentDao;
    private RegistrationDAO regDao;
    private TrainClassDAO classDao;
    private SubjectDAO subjectDao;
    
    /**
     * Keep back data for search purpose
     */
    private Map<String, List<TrainClass>> trainClasses = 
            new HashMap<String, List<TrainClass>>();
    //private List<TrainClass> trainClasses = new ArrayList<TrainClass>(10);
    
    private static Object mutex = new Object();
    
    public ReporterImpl() {
        super();

        studentDao = DAOFactory.getStudentDao();
        regDao = DAOFactory.getRegistrationDAO();
        classDao = DAOFactory.getTrainClassDAO();
        subjectDao = DAOFactory.getSubjectDao();
    }

    @Override
    public List<TrainClass> getTrainClass(TrainClassStatus status) {
        // TODO: implementation
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TrainClass> getOpened(String year) {
        // TODO: implementation
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TrainClass> getOpened(String year, int semeter) {
        // TODO: implementation
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Student> searchStudent(String key) {
        List<Student> students = new ArrayList<Student>(10);
        try {
            students = studentDao.findByColumName("HoTen", key);
                    
            List<Student> student_1 = studentDao.findByColumName("MSSV", key);
            
            if ((student_1 != null) && !student_1.isEmpty()) {
                for (Student s : student_1) {
                    if (!students.contains(s)) {
                        students.add(s);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(
                    ReporterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return students;
    }

    @Override
    public List<TrainClass> getTrainClassRegistered(String sessionId, String mssv,
                                                    boolean fullInfor) {
        List<TrainClass> results = new ArrayList<TrainClass>(10);

        try {
            // Retrieve student's registration
            List<Registration> regs = regDao.findByColumName("MSSV", mssv);
            if ((regs == null) || regs.isEmpty()) {
                return null;
            }

            // Retrivev correcspond trainclass
            for (Registration r : regs) {
                List<TrainClass> clazzTemp = classDao.findByColumName(
                        "MaLopHoc",r.getId().getClassCode());
                //
                // Note: In this case, the clazzTemp will be unique or empty.
                //
                if ((clazzTemp != null) && !clazzTemp.isEmpty()) {
                    // Update full information
                    if (fullInfor) {
                        for (TrainClass t : clazzTemp) {
                            String subjectName = subjectDao.findById(
                                        t.getSubjectCode()).getSubjectName();
                            t.setSubjectName(subjectName);
                        }
                    }
                    results.addAll(clazzTemp);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(ReporterImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        trainClasses.put(sessionId, results);
        return results;
        //TODO: tobe tested.
    }

    @Override
    public List<TrainClass> getTrainClassRegistered(
                                            String mssv,
                                            String year,
                                            int semeter) {
        // TODO: implementation
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TrainClass> getTrainClass(String year, int semeter,
                                                TrainClassStatus status) {
        List<TrainClass> results = new ArrayList<TrainClass>(10);
        try {
            List<String> columnName = new ArrayList<String>(10);
            List<Object> values = new ArrayList<Object>(10);
            
            if ((year != null)
                    && !year.isEmpty()
                    && !year.equalsIgnoreCase("all")) {
                columnName.add("NamHoc");
                values.add(year);
            }
            if (semeter > 0) {
                columnName.add("HocKy");
                values.add(semeter);
            }
            if (status != TrainClassStatus.ALL) {
                columnName.add("TrangThai");
                values.add(status.getValue());
            }

            String[] strColumnNames = (String[])columnName.toArray(
                                                new String[columnName.size()]);
            
            if ((strColumnNames == null) || (strColumnNames.length <= 0)) {
                results = classDao.findAll();
            } else {
                results = classDao.findByColumNames(
                                        strColumnNames, values.toArray());
            }
        } catch (Exception ex) {
            Logger.getLogger(
                    ReporterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    @Override
    public List<TrainClass> sort(String sessionId, final String by, final String type) {
        List<TrainClass> tcs = trainClasses.get(sessionId);
        if ((tcs == null) || tcs.isEmpty()) {
            return tcs;
        }
        
        Collections.sort(tcs, new Comparator<TrainClass>() {

            @Override
            public int compare(TrainClass o1, TrainClass o2) {
                if (type.equals("ASC"))
                    return o1.compare(o2, by);
                else
                    return o2.compare(o1, by);
            }

        });
        
        return tcs;
    }

    @Override
    public TrainClass getTrainClass(String classId) {
        String year = Constants.CURRENT_YEAR;
        int semeter = Constants.CURRENT_SEMESTER;
        TrainClassID id = new TrainClassID(classId, year, semeter);
        LecturerDAO lecturerDao = DAOFactory.getLecturerDao();
        try {
            TrainClass t = classDao.findById(id);
            if (t != null) {
                t.setSubjectName(subjectDao.findById(
                        t.getSubjectCode()).getSubjectName());
                String lecturer = t.getLecturerCode();
                try {
                    lecturer = lecturerDao.findById(t.getLecturerCode()).getFullName();
                } catch (Exception ex) {
                    
                }
                t.setLectturerName(lecturer);
            }
            return t;
        } catch (Exception ex) {
            Logger.getLogger(ReporterImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * 
     * @param classId
     * @param year
     * @param semeter
     * @return 
     */
    public TrainClass getTrainClass(String classId, String year, int semeter) {
        TrainClassID id = new TrainClassID(classId, year, semeter);
        LecturerDAO lecturerDao = DAOFactory.getLecturerDao();
        try {
            TrainClass t = classDao.findById(id);
            if (t != null) {
                t.setSubjectName(subjectDao.findById(
                        t.getSubjectCode()).getSubjectName());
                String lecturer = t.getLecturerCode();
                try {
                    lecturer = lecturerDao.findById(t.getLecturerCode()).getFullName();
                } catch (Exception ex) {
                    
                }
                t.setLectturerName(lecturer);
            }
            return t;
        } catch (Exception ex) {
            Logger.getLogger(ReporterImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
