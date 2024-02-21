package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.dto.HallDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.repository.filmsession.FilmSessionRepository;
import ru.job4j.cinema.service.film.FilmService;
import ru.job4j.cinema.service.filmsession.DefaultFilmSessionService;
import ru.job4j.cinema.service.hall.HallService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultFilmSessionServiceTest {
    private static final LocalDateTime NOW = LocalDateTime.now();

    private DefaultFilmSessionService filmSessionService;
    private FilmSessionRepository filmSessionRepository;
    private FilmService filmService;
    private HallService hallService;

    @BeforeEach
    public void initServices() {
        filmSessionRepository = mock(FilmSessionRepository.class);
        filmService = mock(FilmService.class);
        hallService = mock(HallService.class);
        filmSessionService = new DefaultFilmSessionService(
                filmSessionRepository, filmService, hallService);
    }

    @Test
    public void whenFindAll() {
        var films = Map.of(
                1, new FilmDto(1, "Film1", "Description1",
                        2024, 18, 120, "Genre1", 1),
                2, new FilmDto(2, "Film2", "Description2",
                        2024, 18, 120, "Genre2", 2),
                3, new FilmDto(3, "Film3", "Description3",
                        2024, 18, 120, "Genre3", 3)
        );
        var halls = Map.of(
          1, new HallDto(1, "Hall1", 10, 10)
        );
        var filmSessions = List.of(
                new FilmSession(1, 1, 1, NOW, NOW, 500),
                new FilmSession(2, 2, 1, NOW, NOW, 500),
                new FilmSession(3, 3, 1, NOW, NOW, 500)
        );
        when(filmSessionRepository.findAll()).thenReturn(filmSessions);
        when(filmService.findAll()).thenReturn(films.values());
        when(hallService.findAll()).thenReturn(halls.values());
        var expectedFilmSessions = filmSessions.stream()
                .map(filmSession -> new FilmSessionDto(
                        filmSession.getId(),
                        filmSession.getStartTime(),
                        filmSession.getEndTime(),
                        films.get(filmSession.getFilmId()),
                        halls.get(filmSession.getHallId()),
                        filmSession.getPrice()))
                .toList();
        assertThat(filmSessionService.findAll())
                .containsExactlyInAnyOrderElementsOf(expectedFilmSessions);
    }

    @Test
    public void whenFindById() {
        var film = new FilmDto(1, "Film", "Description",
                2024, 18, 120, "Genre", 1);
        var hall = new HallDto(1, "Hall1", 10, 10);
        var filmSession = new FilmSession(1, 1, 1, NOW, NOW, 500);
        var expectedFilmSession = new FilmSessionDto(
                filmSession.getId(),
                filmSession.getStartTime(),
                filmSession.getEndTime(),
                film, hall, filmSession.getPrice());
        when(filmSessionRepository.findById(filmSession.getId()))
                .thenReturn(Optional.of(filmSession));
        when(filmService.findById(film.id()))
                .thenReturn(Optional.of(film));
        when(hallService.findById(hall.id()))
                .thenReturn(Optional.of(hall));
        var actualFilmSession = filmSessionService.findById(filmSession.getId()).orElseThrow();
        assertThat(actualFilmSession).isEqualTo(expectedFilmSession);
    }

    @Test
    public void whenCannotFindByIdThenEmptyOptional() {
        when(filmSessionRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThat(filmSessionService.findById(1)).isEmpty();
    }

    @Test
    public void whenInconsistentModelThenThrowException() {
        var filmSession = new FilmSession(1, 1, 1, NOW, NOW, 500);
        when(filmSessionRepository.findById(filmSession.getId()))
                .thenReturn(Optional.of(filmSession));
        when(filmService.findById(filmSession.getFilmId())).thenReturn(Optional.empty());
        when(hallService.findById(filmSession.getHallId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> filmSessionService.findById(filmSession.getId()))
                .isInstanceOf(IllegalStateException.class);
    }
}
