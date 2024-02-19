package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DefaultFilmService implements FilmService {
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;

    public DefaultFilmService(FilmRepository filmRepository, GenreRepository genreRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Collection<FilmDto> findAll() {
        var result = new ArrayList<FilmDto>();
        var genres = genreRepository.findAll().stream()
                .collect(Collectors.toMap(Genre::getId, genre -> genre));
        for (var film : filmRepository.findAll()) {
            result.add(createDto(film, genres));
        }
        return result;
    }

    @Override
    public Optional<FilmDto> findById(int id) {
        var result = Optional.<FilmDto>empty();
        var film = filmRepository.findById(id);
        if (film.isPresent()) {
            var genre = genreRepository.findById(film.get().getGenreId());
            result = Optional.of(
                    createDto(film.get(), genre.orElse(null))
            );
        }
        return result;
    }

    private FilmDto createDto(Film film,
                                     Map<Integer, Genre> genres) {
        var genre = genres.get(film.getGenreId());
        return createDto(film, genre);
    }

    private FilmDto createDto(Film film, Genre genre) {
        if (genre == null) {
            throw new IllegalStateException(
                    "Inconsistent model for film with id = " + film.getId());
        }
        return new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getYear(),
                film.getMinimalAge(),
                film.getDurationInMinutes(),
                genre.getName(),
                film.getFileId()
        );
    }
}
