package model;

public class Task {
    protected int id;
    protected String title;
    protected String description;
    protected Status status;
    private static int count = 0;

    public Task(String title, String description) {
        this.id = generateId();
        this.title = title;
        this.description = description;
        status = Status.NEW;
    }

    private int generateId() {
        return ++count;
    }

    public int getId() {
        return id;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task ID-" + id + " -- " + status + " -- " + title + ": " + description;
    }
}
