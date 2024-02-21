package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.film.Sql2oFilmRepository;
import ru.job4j.cinema.util.Sql2oClient;

import java.io.IOException;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oFilmRepositoryTest {
    private static Sql2oFilmRepository sql2oFilmRepository;

    @BeforeAll
    public static void initRepositories() throws IOException {
        sql2oFilmRepository = new Sql2oFilmRepository(Sql2oClient.create());
    }

    @Test
    public void whenFindByExistingIdThenReturnFilm() {
        var film = sql2oFilmRepository.findById(1).orElseThrow();
        var expectedFilm = new Film(
                1,
                "Терминатор",
                "История противостояния солдата Кайла Риза и киборга-терминатора, "
                + "прибывших в 1984 год из пост-апокалиптического будущего, "
                + "где миром правят машины-убийцы, а человечество находится на грани вымирания.",
                1984, 1, 18, 108, 1);
        assertThat(film).usingRecursiveComparison().isEqualTo(expectedFilm);
    }

    @Test
    public void whenFindByMissingIdThenReturnEmptyOptional() {
        assertThat(sql2oFilmRepository.findById(100)).isEmpty();
    }

    @Test
    public void whenFindAllThenReturnValidIds() {
        var expectedFilms = IntStream.rangeClosed(1, 3)
                .mapToObj(id -> sql2oFilmRepository.findById(id).orElseThrow())
                .toList();
        assertThat(sql2oFilmRepository.findAll())
                .containsExactlyInAnyOrderElementsOf(expectedFilms);
    }
}
