package ru.job4j.cinema.model;

public class Ticket {
    private int id;
    private int filmSessionId;
    private int rowNumber;
    private int placeNumber;
    private int userId;

    public Ticket() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFilmSessionId() {
        return filmSessionId;
    }

    public void setFilmSessionId(int filmSessionId) {
        this.filmSessionId = filmSessionId;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(int placeNumber) {
        this.placeNumber = placeNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
