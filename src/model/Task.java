package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected int id;
    protected String title;
    protected String description;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;

    // count means last id
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

    public LocalDateTime getEndTime() {
        return getStartTime().plus(Duration.ofMinutes(getDuration()));
    }

    public LocalDateTime getStartTime() {
        if (startTime == null) {
            // hardcoded start time instead of null
            return LocalDateTime.of(2000, 10, 25, 19, 59);
        } else {
            return startTime;
        }
    }

    public long getDuration() {
        if (duration == null) {
            return 0;
        } else {
            return duration.toMinutes();
        }
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

    public void setId(int newId) {
        this.id = newId;

        if (newId >= count) {
            count = newId;
        }
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(long durationInMinutes) {
        this.duration = Duration.ofMinutes(durationInMinutes);
    }

    @Override
    public String toString() {
        return TaskType.TASK + "," + id + "," + status + "," + title + "," + description + ","
                + getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")) + ","
                + getDuration();
    }
}
