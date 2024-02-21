package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.repository.file.Sql2oFileRepository;
import ru.job4j.cinema.util.Sql2oClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oFileRepositoryTest {
    private static Sql2oFileRepository sql2oFileRepository;

    @BeforeAll
    public static void initRepositories() throws IOException {
        sql2oFileRepository = new Sql2oFileRepository(Sql2oClient.create());
    }

    @Test
    public void whenFindByExistingIdThenReturnFile() {
        var file = sql2oFileRepository.findById(1).orElseThrow();
        var expectedFile = new File(1, "terminator.png", "files\\terminator.png");
        assertThat(file).usingRecursiveComparison().isEqualTo(expectedFile);
    }

    @Test
    public void whenFindByMissingIdThenReturnEmptyOptional() {
        assertThat(sql2oFileRepository.findById(100)).isEmpty();
    }
}
