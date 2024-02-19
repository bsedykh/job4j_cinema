package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.util.Sql2oClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oFilmSessionRepositoryTest {
    private static Sql2oFilmSessionRepository sql2oFilmSessionRepository;

    @BeforeAll
    public static void initRepositories() throws IOException {
        sql2oFilmSessionRepository = new Sql2oFilmSessionRepository(Sql2oClient.create());
    }

    @Test
    public void whenFindByExistingIdThenReturnFilm() {
        var filmSession = sql2oFilmSessionRepository.findById(1).orElseThrow();
        var expectedFilmSession = new FilmSession(
                1, 1, 1,
                LocalDateTime.of(2024, 2, 15, 12, 0),
                LocalDateTime.of(2024, 2, 15, 14, 0),
                500);
        assertThat(filmSession).usingRecursiveComparison().isEqualTo(expectedFilmSession);
    }

    @Test
    public void whenFindByMissingIdThenReturnEmptyOptional() {
        assertThat(sql2oFilmSessionRepository.findById(100)).isEmpty();
    }

    @Test
    public void whenFindAllThenReturnValidIds() {
        var expectedFilms = IntStream.rangeClosed(1, 4)
                .mapToObj(id -> sql2oFilmSessionRepository.findById(id).orElseThrow())
                .toList();
        assertThat(sql2oFilmSessionRepository.findAll())
                .containsExactlyInAnyOrderElementsOf(expectedFilms);
    }
}
