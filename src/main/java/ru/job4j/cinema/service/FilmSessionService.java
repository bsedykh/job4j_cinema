package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmSessionDto;

import java.util.List;
import java.util.Optional;

public interface FilmSessionService {
    List<FilmSessionDto> findAll();

    Optional<FilmSessionDto> findById(int id);
}
