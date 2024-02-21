package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.film.FilmRepository;
import ru.job4j.cinema.repository.genre.GenreRepository;
import ru.job4j.cinema.service.film.DefaultFilmService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultFilmServiceTest {
    private DefaultFilmService filmService;
    private FilmRepository filmRepository;
    private GenreRepository genreRepository;

    @BeforeEach
    public void initServices() {
        filmRepository = mock(FilmRepository.class);
        genreRepository = mock(GenreRepository.class);
        filmService = new DefaultFilmService(filmRepository, genreRepository);
    }

    @Test
    public void whenFindAll() {
        var genre1 = new Genre(1, "Genre1");
        var genre2 = new Genre(2, "Genre2");
        var genre3 = new Genre(3, "Genre3");
        var genres = Map.of(
                genre1.getId(), genre1,
                genre2.getId(), genre2,
                genre3.getId(), genre3
        );
        var films = List.of(
                new Film(1, "Film1", "Description1",
                        2024, 1, 18, 120, 1),
                new Film(2, "Film2", "Description2",
                        2024, 2, 18, 120, 2),
                new Film(3, "Film3", "Description3",
                        2024, 3, 18, 120, 3)
        );
        when(filmRepository.findAll()).thenReturn(films);
        when(genreRepository.findAll()).thenReturn(genres.values());
        var expectedFilms = films.stream()
                .map(film -> new FilmDto(film.getId(), film.getName(), film.getDescription(),
                        film.getYear(), film.getMinimalAge(), film.getDurationInMinutes(),
                        genres.get(film.getGenreId()).getName(), film.getFileId()))
                .toList();
        assertThat(filmService.findAll()).containsExactlyInAnyOrderElementsOf(expectedFilms);
    }

    @Test
    public void whenFindById() {
        var genre = new Genre(1, "Genre");
        var film = new Film(1, "Film", "Description",
                2024, 1, 18, 120, 1);
        when(filmRepository.findById(film.getId())).thenReturn(Optional.of(film));
        when(genreRepository.findById(genre.getId())).thenReturn(Optional.of(genre));
        var expectedFilm = new FilmDto(film.getId(), film.getName(), film.getDescription(),
                film.getYear(), film.getMinimalAge(), film.getDurationInMinutes(),
                genre.getName(), film.getFileId());
        var actualFilm = filmService.findById(film.getId()).orElseThrow();
        assertThat(actualFilm).isEqualTo(expectedFilm);
    }

    @Test
    public void whenCannotFindByIdThenEmptyOptional() {
        when(filmRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThat(filmService.findById(1)).isEmpty();
    }

    @Test
    public void whenInconsistentModelThenThrowException() {
        var film = new Film(1, "Film", "Description",
                2024, 1, 18, 120, 1);
        when(filmRepository.findById(film.getId())).thenReturn(Optional.of(film));
        when(genreRepository.findById(film.getGenreId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> filmService.findById(film.getId()))
                .isInstanceOf(IllegalStateException.class);
    }
}
