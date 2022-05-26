package lab6.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Friendship extends Entity<Long>{

    private Long idUser1;
    private Long idUser2;
    private LocalDate date;

    /**
     * Constructorul clasei
     * @param idUser1: tipul Long
     * @param idUser2:tipul Long
     */
    public Friendship(Long idUser1, Long idUser2, LocalDate date) {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.date = date;
    }

    /**
     * Afla id-ul utilizatorului
     * @return: id-ul utilizatorului
     */
    public Long getIdUser1() {
        return idUser1;
    }

    /**
     * Schimba id-ul utilizatorului
     * @param idUser1:tipul Long
     */
    public void setIdUser1(Long idUser1) {
        this.idUser1 = idUser1;
    }

    /**
     * Afla id-ul utilizatorului
     * @return:id-ul utilizatorului
     */
    public Long getIdUser2() {
        return idUser2;
    }

    /**
     * Schimba id-ul utilizatorului
     * @param idUser2: tipul Long
     */
    public void setIdUser2(Long idUser2) {
        this.idUser2 = idUser2;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Suprascrie metoda toString
     * @return: id-urile utilizatorilor
     */
    @Override
    public String toString() {
        return idUser1 + " " + idUser2;
    }

}
