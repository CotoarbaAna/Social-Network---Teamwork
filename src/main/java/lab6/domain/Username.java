package lab6.domain;

public class Username extends Entity<Long> {
    String username;

    public Username(String username) {
        this.username= username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setId() {

    }

    @Override
    public String toString() {
        return "Username{" + " username: '" + username + '\'' + '}';
    }
}
