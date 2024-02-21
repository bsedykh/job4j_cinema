package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.user.UserRepository;
import ru.job4j.cinema.service.user.DefaultUserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultUserServiceTest {
    private DefaultUserService userService;
    private UserRepository userRepository;

    @BeforeEach
    public void initServices() {
        userRepository = mock(UserRepository.class);
        userService = new DefaultUserService(userRepository);
    }

    @Test
    public void whenSuccessfulSaveThenUser() {
        var user = new User(1, "test@test.com", "User", "password");
        when(userRepository.save(user)).thenReturn(Optional.of(user));
        assertThat(userService.save(user)).isEqualTo(Optional.of(user));
    }

    @Test
    public void whenFailedSaveThenEmptyOptional() {
        var user = new User(1, "test@test.com", "User", "password");
        when(userRepository.save(user)).thenReturn(Optional.empty());
        assertThat(userService.save(user)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSuccessfulFindThenUser() {
        var user = new User(1, "test@test.com", "User", "password");
        when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .thenReturn(Optional.of(user));
        assertThat(userService.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .isEqualTo(Optional.of(user));
    }

    @Test
    public void whenFailedFindTheEmptyOptional() {
        var user = new User(1, "test@test.com", "User", "password");
        when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .thenReturn(Optional.empty());
        assertThat(userService.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .isEqualTo(Optional.empty());
    }
}
