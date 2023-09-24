public class Task {
    protected int ID;
    protected String title;
    protected String description;
    protected Status status;

    public Task(int ID, String title, String description, Status status) {
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }
}
