package uit.cnpm02.dkhp.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.http.HttpSession;

/**
 *
 * @author LocNguyen
 */
public class SessionManager {
    private Map<String, HttpSession> sessionsMapper =
                    new HashMap<String, HttpSession>(10);
    private final Object mutex = new Object();

    /**
     * Default constructor.
     */
    public SessionManager() {
        super();
        Timer t = new Timer("Session cleaer");
        TimerTask sessionCleaner = new TimerTask() {

            @Override
            public void run() {
                for (String key : sessionsMapper.keySet()) {
                    HttpSession s = sessionsMapper.get(key);
                    long lastTime = s.getLastAccessedTime();
                    long currentTime = System.currentTimeMillis();
                    if ((currentTime-lastTime) > (s.getMaxInactiveInterval()*1000)) {
                        sessionsMapper.remove(s.getId());
                    }
                }
            }
        };
        
        t.schedule(sessionCleaner, 10*60*1000, 10*60*1000);
    }

    public void addSession(HttpSession session) {
        synchronized (mutex) {
            if (!sessionsMapper.containsKey(session.getId())) {
                sessionsMapper.put(session.getId(), session);
            }
            for (String key : sessionsMapper.keySet()) {
                HttpSession s = sessionsMapper.get(key);
                s.setAttribute("user-online", sessionsMapper.size());
            }
        }
    }

    public void removeSession(String sessionId) {
        synchronized (mutex) {
            HttpSession s = sessionsMapper.get(sessionId);
            if (s != null) {
                sessionsMapper.remove(sessionId);
            }
        }
    }
    
    public int count() {
        return sessionsMapper.size();
    }
}
