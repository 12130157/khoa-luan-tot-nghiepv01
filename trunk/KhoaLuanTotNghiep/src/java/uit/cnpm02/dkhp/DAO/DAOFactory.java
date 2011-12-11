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
    private static TrainClassDAO trainClassDao=null;
    
    private static PreSubjectDAO preSubDao = null;

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
}
