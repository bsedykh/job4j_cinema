package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.util.Sql2oClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oUserRepositoryTest {
    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws IOException {
        sql2oUserRepository = new Sql2oUserRepository(Sql2oClient.create());
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var user = sql2oUserRepository.save(new User(
                        0, "test@test.com", "Test user", "password"))
                .orElseThrow();
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())
                .orElseThrow();
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var user1 = sql2oUserRepository.save(new User(
                        0, "test1@test.com", "Test user1", "password"))
                .orElseThrow();
        var user2 = sql2oUserRepository.save(new User(
                        0, "test2@test.com", "Test user2", "password"))
                .orElseThrow();
        var user3 = sql2oUserRepository.save(new User(
                        0, "test3@test.com", "Test user3", "password"))
                .orElseThrow();
        var result = sql2oUserRepository.findAll();
        assertThat(result).containsExactlyInAnyOrder(user1, user2, user3);
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oUserRepository.findByEmailAndPassword(
                "test@test.com", "password")).isEmpty();
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var user = sql2oUserRepository.save(new User(
                        0, "test@test.com", "Test user", "password"))
                .orElseThrow();
        assertThat(sql2oUserRepository.deleteById(user.getId())).isTrue();
        assertThat(sql2oUserRepository.findByEmailAndPassword(
                user.getEmail(), user.getPassword())).isEmpty();
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oUserRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenSaveDuplicateUserThenGetEmptyOptional() {
        var user = sql2oUserRepository.save(new User(
                        0, "test@test.com", "Test user", "password"))
                .orElseThrow();
        assertThat(sql2oUserRepository.save(user)).isEmpty();
        var users = sql2oUserRepository.findAll();
        assertThat(users).containsExactlyInAnyOrder(user);
    }
}
