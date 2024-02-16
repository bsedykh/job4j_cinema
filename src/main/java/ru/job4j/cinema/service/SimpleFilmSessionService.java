package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.dto.HallDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SimpleFilmSessionService implements FilmSessionService {
    @Override
    public List<FilmSessionDto> findAll() {
        return List.of(new FilmSessionDto(
                1,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new FilmDto(1, "Film", "Description", 2023, 18, 60, "Action", 1),
                new HallDto("Main", 10, 10),
                500));
    }

    @Override
    public Optional<FilmSessionDto> findById(int id) {
        return Optional.of(new FilmSessionDto(
                1,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new FilmDto(1, "Film", "Description", 2023, 18, 60, "Action", 1),
                new HallDto("Main", 10, 10),
                500));
    }
}
