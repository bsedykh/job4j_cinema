package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.util.Sql2oClient;

import java.io.IOException;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oGenreRepositoryTest {
    private static Sql2oGenreRepository sql2oGenreRepository;

    @BeforeAll
    public static void initRepositories() throws IOException {
        sql2oGenreRepository = new Sql2oGenreRepository(Sql2oClient.create());
    }

    @Test
    public void whenFindByExistingIdThenReturnFilm() {
        var genre = sql2oGenreRepository.findById(1).orElseThrow();
        var expectedGenre = new Genre(
                1,
                "Боевик");
        assertThat(genre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @Test
    public void whenFindByMissingIdThenReturnEmptyOptional() {
        assertThat(sql2oGenreRepository.findById(100)).isEmpty();
    }

    @Test
    public void whenFindAllThenReturnValidIds() {
        var expectedGenres = IntStream.rangeClosed(1, 3)
                .mapToObj(id -> sql2oGenreRepository.findById(id).orElseThrow())
                .toList();
        assertThat(sql2oGenreRepository.findAll())
                .containsExactlyInAnyOrderElementsOf(expectedGenres);
    }
}
