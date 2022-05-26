package lab6.domain;

import java.time.LocalDate;

public class FrReqUsername  {
    private Username username;
    private RequestStatus status;
    private LocalDate data;

    public FrReqUsername(Username username, RequestStatus status, LocalDate data) {
        this.username = username;
        this.status = status;
        this.data = data;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String getUsername() {
        return username.getUsername();
    }

    public LocalDate getData() {
        return data;
    }

    @Override
    public String toString() {
        return "FrReqUsername{" +
                "username='" + username + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
