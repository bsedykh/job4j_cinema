package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.util.Sql2oClient;

import java.io.IOException;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oHallRepositoryTest {
    private static Sql2oHallRepository sql2oHallRepository;

    @BeforeAll
    public static void initRepositories() throws IOException {
        sql2oHallRepository = new Sql2oHallRepository(Sql2oClient.create());
    }

    @Test
    public void whenFindByExistingIdThenReturnFilm() {
        var hall = sql2oHallRepository.findById(1).orElseThrow();
        var expectedHall = new Hall(
                1, "Основной", "Основной зал", 10, 10);
        assertThat(hall).usingRecursiveComparison().isEqualTo(expectedHall);
    }

    @Test
    public void whenFindByMissingIdThenReturnEmptyOptional() {
        assertThat(sql2oHallRepository.findById(100)).isEmpty();
    }

    @Test
    public void whenFindAllThenReturnValidIds() {
        var expectedHalls = IntStream.rangeClosed(1, 1)
                .mapToObj(id -> sql2oHallRepository.findById(id).orElseThrow())
                .toList();
        assertThat(sql2oHallRepository.findAll())
                .containsExactlyInAnyOrderElementsOf(expectedHalls);
    }
}
