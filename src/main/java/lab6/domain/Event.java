package lab6.domain;

import java.time.LocalDate;

public class Event extends Entity<Long>{
    private String name;
    private LocalDate date;

    public Event(Long id, String name, LocalDate date) {
        this.name = name;
        this.date = date;
        this.setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
