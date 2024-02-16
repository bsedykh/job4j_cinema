package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;

import java.util.List;

@Service
public class SimpleFilmService implements FilmService {
    @Override
    public List<FilmDto> findAll() {
        return List.of(new FilmDto(1, "Film", "Description", 2023, 18, 60, "Action", 1));
    }
}
