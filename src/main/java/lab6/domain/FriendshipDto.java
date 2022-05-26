package lab6.domain;

import lab6.repository.Repository;

import java.time.LocalDate;

public class FriendshipDto {
    private LocalDate date;
    private String nume, prenume;

    /**
     * Constructorul clasei
     * @param nume:tipul String
     * @param prenume:tipul String
     * @param date: tipul LocalDate
     */
    public FriendshipDto(String nume, String prenume, LocalDate date){
        this.nume = nume;
        this.prenume = prenume;
        this.date = date;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public LocalDate getDate() {
        return date;
    }

    /**
     * Suprascrie metoda toString
     * @return: numele,prenumele user-ului si data de la care sunt prieteni
     */
    @Override
    public String toString() {
        return nume + "|" + prenume + "|" + date.toString();
    }
}
