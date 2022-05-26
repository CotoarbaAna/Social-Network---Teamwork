package lab6.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private ArrayList<User> friends;

    /**
     * Constructorul clasei
     * @param firstName:tipul String
     * @param lastName:tipul String
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new ArrayList<User>();
    }

    /**
     *Afla prenumele utilizatorului
     * @return: prenumele utilizatorului
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Afla numele utilizatorului
     * @return: numele utilizatorului
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Afla prietenii utilizatorului
     * @return: prietenii utilizatorului
     */
    public ArrayList<User> getFriends() {
        return friends;
    }

    /**
     * Schimba prenumele utilizatorului
     * @param firstName:tipul String
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Schimba numele utilizatorului
     * @param lastName: tipul String
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Adauga un prieten
     * @param user: tipul User
     */
    public void addFriend(User user){
        friends.add(user);
    }

    /**
     * Sterge un prieten
     * @param user: tipul User
     */
    public void deleteFriend(User user){
        friends.remove(user);
    }


    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                 '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }

    /**
     * Verifica egalitatea celor doua obiecte
     * @param obj: tipul Object
     * @return: adevarat sau fals in functie de egalitatea obiectelor
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if (!(obj instanceof User)) return false;
        User that = (User) obj;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());

    }
}
