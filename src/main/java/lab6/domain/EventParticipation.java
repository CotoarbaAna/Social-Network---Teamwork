package lab6.domain;

public class EventParticipation extends Entity<Long>{
    private Long eventId, userId;

    public EventParticipation(Long id, Long eventId, Long userId) {
        this.eventId = eventId;
        this.userId = userId;
        this.setId(id);
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
