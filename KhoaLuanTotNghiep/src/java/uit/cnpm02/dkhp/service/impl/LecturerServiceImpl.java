package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.Lecturer;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.service.ILecturerService;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.StringUtils;

/**
 *
 * @author LocNguyen
 */
public class LecturerServiceImpl implements ILecturerService {
    private int rowPerPage = Constants.ELEMENT_PER_PAGE_DEFAULT;
    
    private LecturerDAO lecturerDao;
    private TrainClassDAO tClassDao;
    /**
     * Backup for import from file case (include
     * students have correct information)
     * 
     * SessionID - List<Student>
     **/
    private Map<String, List<Lecturer>> importLecturers
            = new HashMap<String, List<Lecturer>>();
    
    private Map<String, List<Lecturer>> currentLecturers
            = new HashMap<String, List<Lecturer>>();
    /**SORT TYPE**/
    private Map<String, String> sortTypes
            = new HashMap<String, String>();
    
    private Object mutex = new Object();
    
    /**
     * Default constructor
     */
    public LecturerServiceImpl() {
        lecturerDao = DAOFactory.getLecturerDao();
        tClassDao = DAOFactory.getTrainClassDAO();
    }
    
    @Override
    public Lecturer getLecturer(String id) {
        try {
            return lecturerDao.findById(id);
        } catch (Exception ex) {
            Logger.getLogger(LecturerServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Lecturer> getLecturers(int page, String session) {
        List<Lecturer> results = null;
        try {
            results = lecturerDao.findAll(rowPerPage, page, "HoTen", null);
            if ((results != null) && !results.isEmpty()) {
                try {
                    currentLecturers.remove(session);
                } catch (Exception ex) {
                    // new session, maybe...
                }
                currentLecturers.put(session, results);
            }
        } catch (Exception ex) {
            Logger.getLogger(LecturerServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return results;
    }

    @Override
    public List<Lecturer> getLecturers(String session) {
        List<Lecturer> results = currentLecturers.get(session);
        if ((results == null) && results.isEmpty()) {
            results = getLecturers(1, session);
            currentLecturers.put(session, results);
        }
        return results;
    }

    @Override
    public List<Lecturer> getLecturer(String classID, String year, int semeter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ExecuteResult addLecturer(Lecturer l) {
        ExecuteResult result = new ExecuteResult(true, "");
        try {
            if (lecturerDao.findById(l.getId()) != null) {
                return new ExecuteResult(false, "GV đã tồn tại.");
            }
            lecturerDao.add(l);
            result.setData(l);
        } catch (Exception ex) {
            Logger.getLogger(LecturerServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return new ExecuteResult(false,
                    "[LecturerService] lỗi server: " + ex.toString());
        }
        return result;
    }

    @Override
    public ExecuteResult addLecturers(List<Lecturer> lecturers,
                                boolean addIfPossible, String sessionId) {
        try {
            if (addIfPossible) {
                List<Lecturer> s_temps = importLecturers.get(sessionId);
                if ((s_temps == null) || s_temps.isEmpty()) {
                    return new ExecuteResult(false, "Dữ liệu không tồn tại, "
                            + "có thể do phiên làm việc hết hiệu lực, vui lòng"
                            + "thử lại bằng cách submit lại file.");
                } else {
                    lecturerDao.addAll(s_temps);
                    importLecturers.remove(sessionId);
                    ExecuteResult e = new ExecuteResult(true, "");
                    e.setData(s_temps);
                    
                    return e;
                }
            }
            
            List<Lecturer> existedLecturers = new ArrayList<Lecturer>(10);
            for (Lecturer s : lecturers) {
                if ((lecturerDao.findById(s.getId()) != null)
                        || lecturerDao.findByIdentifier(s.getIdentityCard()) != null) {
                    existedLecturers.add(s);
                }
            }

            ExecuteResult result = new ExecuteResult(true, "");
            if (existedLecturers.size() > 0) {
                lecturers.removeAll(existedLecturers);
                // Incase these are some information incorrect
                // please send it back to user
                result.setIsSucces(false);
                result.setMessage("Một số GV có mã hoặc số CMND bị trùng");
                result.setData(existedLecturers);

                importLecturers.put(sessionId, lecturers);

            } else {
                lecturerDao.addAll(lecturers);
                result.setData(lecturers);
            }
            return result;
        } catch (Exception ex) {
            Logger.getLogger(LecturerServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return new ExecuteResult(
                    false, "[Error][LecturerService]: " + ex.toString());
        }
    }

    @Override
    public ExecuteResult validateNewLecturer(Lecturer l) {
        ExecuteResult er = new ExecuteResult(true, "");
        try {
            // Check mssv existed.
            if (lecturerDao.findById(l.getId()) != null) {
                er.setIsSucces(false);
                er.setMessage("Mã GV bị trùng");
            }
        } catch (Exception ex) {
            Logger.getLogger(LecturerServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            er.setIsSucces(false);
            er.setMessage("Đã có lỗi xảy ra: " + ex.toString());
        }
        
        return er;
    }

    @Override
    public ExecuteResult deleteLecturer(String lecturerId,
                            boolean deleteAnyway, String sessionId) {
        ExecuteResult result = new ExecuteResult(true, "");
        try {
            Lecturer lecturer = lecturerDao.findById(lecturerId);
            if (lecturer == null) {
                return new ExecuteResult(false, "Không tìm thấy GV cần xóa.");
            }
            
            // It's not allowed to delete lecturer 
            // if he/she's in some class
            List<TrainClass> clazzes = tClassDao.findByColumName("MaGV", lecturerId);
            if ((clazzes != null) && !clazzes.isEmpty()) {
                return new ExecuteResult(false, "Không thể xóa GV đã nhận lớp.");
            }
            
            lecturerDao.delete(lecturer);
            result.setData(lecturer);
            
            List<Lecturer> lecturers = currentLecturers.get(sessionId);
            if ((lecturers != null) && lecturers.contains(lecturer)) {
                lecturers.remove(lecturer);
            }
        } catch (Exception ex) {
            Logger.getLogger(LecturerServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            result.setIsSucces(false);
            result.setMessage("Lỗi server: " + ex.toString());
        }
        
        return result;
    }
    
    @Override
    public ExecuteResult deleteLecturers(List<String> lecturerIds,
                                boolean deleteAnyway, String sessionId) {
        ExecuteResult result = new ExecuteResult(true, "");

        try {
            List<Lecturer> lecturers = lecturerDao.findByIds(lecturerIds);
            if ((lecturers == null) || lecturers.isEmpty()) {
                return new ExecuteResult(false, "Không tìm thấy giảng viên cần xóa.");
            }
            //
            // TODO: 
            // >>>>
            //
            // Không thể xóa danh sách gồm có GV đã nhận lópw
            List<Lecturer> registeredClass = new ArrayList<Lecturer>(10);
            for (Lecturer s : lecturers) {
                if (!tClassDao.findByColumName("MaGV", s.getId()).isEmpty()) {
                    registeredClass.add(s);
                }
            }
            
            if (!registeredClass.isEmpty()) {
                String msg = "Không thể xóa GV đã nhận lớp: ";
                for (Lecturer l : registeredClass) {
                    msg += "- " + l.getFullName() + " (" + l.getId() + ")";
                }
                result.setIsSucces(false);
                result.setMessage(msg);
                
                // Doen't support delete any way
                return result;
            }
            ////
            lecturerDao.delete(lecturers);
            currentLecturers.remove(sessionId);
            lecturers = getLecturers(1, sessionId);
            currentLecturers.put(sessionId, lecturers);
        } catch (Exception ex) {
            Logger.getLogger(LecturerServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            result.setIsSucces(false);
            result.setMessage("[LecturerService][DeleteLecturers]: " + ex.toString());
        }
        
        return result;
    }

    @Override
    public List<Lecturer> search(String key, String session) {
        List<Lecturer> results = new ArrayList<Lecturer>(10);
        List<Lecturer> results_temp = new ArrayList<Lecturer>(10);
        try {
            // Search by FullName
            results_temp = lecturerDao.findByColumName("HoTen", key);
            addList(results, results_temp);
            
            // Search by MSSV
            results_temp = lecturerDao.findByColumName("MaGV", key);
            addList(results, results_temp);
            
            if ((results != null) && !results.isEmpty()) {
                try {
                    currentLecturers.remove(session);
                } catch (Exception ex) {
                    // new session, maybe...
                }
                currentLecturers.put(session, results);
            }
        } catch (Exception ex) {
            Logger.getLogger(StudentServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return results;
    }
    
    private void addList(List<Lecturer> parent, List<Lecturer> child) {
        if ((child == null) || child.isEmpty()) {
            return;
        }
        
        if (parent == null) { // first time
            parent = new ArrayList<Lecturer>(10);
            parent.addAll(child);
            return;
        }
        
        for (Lecturer l : child) {
            if (!parent.contains(l)) {
                parent.add(l);
            }
        }
    }

    @Override
    public List<Lecturer> sort(String sessionId, final String by) {
        String sortType = "ASC";
        try {
            sortType = sortTypes.get(sessionId);
            sortTypes.remove(sessionId);
        } catch (Exception ex) {
            sortType = "ASC";
        }
        if (StringUtils.isEmpty(sortType)) {
            sortType = "ASC";
        }
        
        if (sortType.equalsIgnoreCase("ASC")) {
            sortTypes.put(sessionId, "DES");
        } else {
            sortTypes.put(sessionId, "ASC");
        }

        final String sortType_final = sortType;
        List<Lecturer> lecturers = currentLecturers.get(sessionId);
        if ((lecturers == null) || lecturers.isEmpty()) {
            lecturers = getLecturers(1, sessionId);
        }
        
        if ((lecturers != null) && (!lecturers.isEmpty())) {
            Collections.sort(lecturers, new Comparator<Lecturer>() {
            @Override
            public int compare(Lecturer o1, Lecturer o2) {
                if (sortType_final.equals("ASC"))
                    return o1.compare(o2, by);
                else
                    return o2.compare(o1, by);
            }
        });
        }
        currentLecturers.put(sessionId, lecturers);
        return lecturers;
    }

    @Override
    public ExecuteResult updateLecturer(String sessionId, Lecturer l) {
        ExecuteResult er = new ExecuteResult(true, "Update thành công.");
        try {
            Lecturer persistObj = lecturerDao.findById(l.getId());
            if (persistObj == null) {
                er.setIsSucces(false);
                er.setMessage("[Error][LecturerService] Không tìm thấy GV cần update");
            } else {
                lecturerDao.update(l);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(LecturerServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            er.setIsSucces(false);
            er.setMessage("Đã có lỗi xảy ra: " + ex.toString());
        }
        return er;
    }

    @Override
    public int getNumberPage() {
        try {
            int rows = lecturerDao.getRowsCount();

            int numPage = 1;
            if (rows % rowPerPage == 0) {
                numPage = rows / rowPerPage;
            } else {
                numPage = rows / rowPerPage + 1;
            }
            return numPage;
        } catch (Exception ex) {
            Logger.getLogger(LecturerServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return 1;
    }

    @Override
    public ExecuteResult updateLecturer(String sessionId, String id, String name,
                        String cmnd, String email, Date birthday, String phone) {
        ExecuteResult er = new ExecuteResult(true, "Successful.");
        try {
            Lecturer persistObj = lecturerDao.findById(id);
            if (persistObj == null) {
                er.setIsSucces(false);
                er.setMessage("error - Không tìm thấy GV cần update");
            } else {
                persistObj.setFullName(name);
                persistObj.setIdentityCard(cmnd);
                persistObj.setEmail(email);
                persistObj.setBirthday(birthday);
                persistObj.setPhone(phone);
                
                lecturerDao.update(persistObj);
                er.setData(persistObj);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(LecturerServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            er.setIsSucces(false);
            er.setMessage("error - Đã có lỗi xảy ra: " + ex.toString());
        }
        return er;
    }
    
}
