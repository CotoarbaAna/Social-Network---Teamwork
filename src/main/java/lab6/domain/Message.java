package lab6.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    private Long from;
    private Long to;
    private String message;
    private LocalDate data;
    private Long reply;

    public Message(Long from,Long to, String message, LocalDate date,Long reply) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = date;
        this.reply = reply;
    }

    public LocalDate getDate() {
        return data;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public Long getReply() {
        return reply;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        if(reply!=0)
            return "from " + from + " | to " + to + " | message: '" + message + '\'' + " | data: " + data + " | replied to " + reply ;
        else
            return "from " + from + " | to " + to + " | message: '" + message + '\'' + " | data: " + data + " | No reply";
    }
}
