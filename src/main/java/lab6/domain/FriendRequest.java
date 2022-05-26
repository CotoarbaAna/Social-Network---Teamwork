package lab6.domain;

import java.time.LocalDate;

public class FriendRequest extends Entity<Long>{
    private Long id_us1;
    private Long id_us2;
    private LocalDate data;

    private RequestStatus status;

    public FriendRequest(Long id_us1, Long id_us2, LocalDate data) {
        this.id_us1 = id_us1;
        this.id_us2 = id_us2;
        this.data = data;
        this.status = RequestStatus.PENDING;
    }

    public FriendRequest(Long id_us1, Long id_us2, LocalDate data, RequestStatus status) {
        this.id_us1 = id_us1;
        this.id_us2 = id_us2;
        this.data = data;
        this.status = status;
        this.status = status;
    }

    public Long getId_us1() {
        return id_us1;
    }

    public Long getId_us2() {
        return id_us2;
    }

    public LocalDate getData() {
        return data;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String toString() {
        return "" + this.getId() + ";" + id_us1 + ";" + id_us2 + ";" + status.toString();
    }
}
