package uit.cnpm02.dkhp.DAO;

/**
 *
 * @author LocNguyen
 */
public class DAOFactory {

    private static AccountDAO accountDao = null;
    private static NewsDAO newsDao = null;
    private static StudentDAO studentDao = null;
    private static FacultyDAO facultyDao = null;
    private static CourseDAO courseDao = null;
    private static ClassDAO classDao = null;
    private static DiaryDAO diaryDao = null;
    private static SubjectDAO subjectDao = null;
    private static CommentDao commentDao = null;
    private static RegistrationDAO registrationDao = null;
    private static TrainClassDAO trainClassDao = null;
    private static RuleDAO ruleDao = null;
    private static PreSubjectDAO preSubDao = null;
    private static StudyResultDAO studyResultDAO = null;
    private static LecturerDAO lecturerDao = null;
    private static RegistrationTimeDAO timeDao = null;
    private static TaskDAO taskDao = null;
    private static FileInfoDAO fileInfoDao = null;
    private static DetailTrainDAO detailTrainfoDao = null;
    private static TrainProgramDAO trainProDao = null;
    private static TrainProgDetailDAO trainProDetailDao = null;
    
    
    public static FileInfoDAO getFileInfoDao() {
        if (fileInfoDao == null) {
            fileInfoDao = new FileInfoDAO();
        }
        return fileInfoDao;
    }
    
    public static TrainProgDetailDAO getTrainProgDetailDAO() {
        if (trainProDetailDao == null) {
            trainProDetailDao = new TrainProgDetailDAO();
        }
        return trainProDetailDao;
    }
    
    public static TrainProgramDAO getTrainProgramDAO() {
        if (trainProDao == null) {
            trainProDao = new TrainProgramDAO();
        }
        return trainProDao;
    }

    public static DetailTrainDAO getDetainTrainDAO() {
        if (detailTrainfoDao == null) {
            detailTrainfoDao = new DetailTrainDAO();
        }
        return detailTrainfoDao;
    }
    
    public static PreSubjectDAO getPreSubDao() {
        if (preSubDao == null) {
            preSubDao = new PreSubjectDAO();
        }
        return preSubDao;
    }

    public static SubjectDAO getSubjectDao() {
        if (subjectDao == null) {
            subjectDao = new SubjectDAO();
        }
        return subjectDao;
    }

    public static DiaryDAO getDiaryDao() {
        if (diaryDao == null) {
            diaryDao = new DiaryDAO();
        }
        return diaryDao;
    }

    public static ClassDAO getClassDao() {
        if (classDao == null) {
            classDao = new ClassDAO();
        }
        return classDao;
    }

    public static CourseDAO getCourseDao() {
        if (courseDao == null) {
            courseDao = new CourseDAO();
        }
        return courseDao;
    }

    public static FacultyDAO getFacultyDao() {
        if (facultyDao == null) {
            facultyDao = new FacultyDAO();
        }

        return facultyDao;
    }

    public static StudentDAO getStudentDao() {
        if (studentDao == null) {
            studentDao = new StudentDAO();
        }

        return studentDao;
    }

    public static AccountDAO getAccountDao() {
        if (accountDao == null) {
            accountDao = new AccountDAO();
        }
        return accountDao;
    }

    public static NewsDAO getNewsDao() {
        if (newsDao == null) {
            newsDao = new NewsDAO();
        }

        return newsDao;
    }

    public static CommentDao getCommentDao() {
        if (commentDao == null) {
            commentDao = new CommentDao();
        }
        return commentDao;
    }

    public static RegistrationDAO getRegistrationDAO() {
        if (registrationDao == null) {
            registrationDao = new RegistrationDAO();
        }
        return registrationDao;
    }

    public static TrainClassDAO getTrainClassDAO() {
        if (trainClassDao == null) {
            trainClassDao = new TrainClassDAO();
        }
        return trainClassDao;
    }

    public static RuleDAO getRuleDao() {
        if (ruleDao == null) {
            ruleDao = new RuleDAO();
        }
        return ruleDao;
    }

    public static StudyResultDAO getStudyResultDao() {
        if (studyResultDAO == null) {
            studyResultDAO = new StudyResultDAO();
        }
        return studyResultDAO;
    }

    public static LecturerDAO getLecturerDao() {
        if (lecturerDao == null) {
            lecturerDao = new LecturerDAO();
        }
        return lecturerDao;
    }
    
    public static RegistrationTimeDAO getRegistrationTimeDAO() {
        if (timeDao == null) {
            timeDao = new RegistrationTimeDAO();
        }
        return timeDao;
    }
    
    public static TaskDAO getTaskDAO() {
        if (taskDao == null) {
            taskDao = new TaskDAO();
        }
        return taskDao;
    }
}
