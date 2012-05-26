package uit.cnpm02.dkhp.model;

import java.util.Date;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.model.type.TaskStatus;
import uit.cnpm02.dkhp.model.type.TaskType;

/**
 *
 * @author LocNguyen
 */
public class Task extends AbstractJdbcModel<Integer> {
    private String sender;
    private String reciever;
    private String content;
    private Date created;
    private TaskStatus status;
    private TaskType taskType;
    
    public Task() {
        
    }

    public Task(String sender, String reciever,
            String content, Date created, TaskStatus status, TaskType type) {
        this.sender = sender;
        this.reciever = reciever;
        this.content = content;
        this.created = created;
        this.status = status;
        this.taskType = type;
    }
    
     public Task(Task task) {
        this.sender = task.getSender();
        this.reciever = task.getReciever();
        this.content = task.getContent();
        this.created = task.getCreated();
        this.status = task.getStatus();
        this.taskType = task.getTaskType();
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    
    @Override
    public String[] getColumnNames() {
        return new String[] {
            "NoiDung",
            "NguoiGui",
            "NguoiNhan",
            "NgayGui",
            "TrangThai",
            "Loai"
        };
    }

    @Override
    public Object[] getColumnValues() {
        return new Object[] {
            content,
            sender,
            reciever,
            created,
            (status == null) ? TaskStatus.UNKNOWN.value() : status.value(),
            (taskType == null) ? TaskType.UNKNOWN.value() : taskType.value()
        };
    }

    @Override
    public void setColumnValues(Object[] values) {
        content = values[0].toString();
        sender = values[1].toString();
        reciever = values[2].toString();
        created = (Date) values[3];
        status = TaskStatus.getTaskStatus((Integer) values[4]);
        taskType = TaskType.getTaskType((Integer) values[5]);
    }

    @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".ThongBao";
    }
    
}
