package ModelRequest;

public class TaskCreate {
    public int id;
    public String task;
    public String task_description;

    public TaskCreate(String task, String task_description) {
        this.task = task;
        this.task_description = task_description;
    }
}
