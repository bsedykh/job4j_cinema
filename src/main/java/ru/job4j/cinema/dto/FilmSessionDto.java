package ru.job4j.cinema.dto;

import java.time.LocalDateTime;

public record FilmSessionDto(
        int id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        FilmDto film,
        HallDto hall,
        int price) {
}
