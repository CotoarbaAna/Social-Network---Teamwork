package lab6.domain;

import java.time.LocalDate;

public class MessageDto {
    private LocalDate date;
    private String username, message;

    public MessageDto(String username, LocalDate date, String message) {
        this.username = username;
        this.date = date;
        this.message = message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
