package ru.job4j.cinema.dto;

public record HallDto(
        int id,
        String name,
        int rowCount,
        int placeCount) {
}
