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
}
