package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private final UserService userService = mock(UserService.class);
    private final UserController userController = new UserController(userService);

    @Test
    public void whenGetRegistrationPageThenRegistrationView() {
        assertThat(userController.getRegistrationPage()).isEqualTo("users/register");
    }

    @Test
    public void whenRegisterUserThenRedirectToLogin() {
        var user = new User(1, "ivan@test.com", "Ivan", "123");
        when(userService.save(user)).thenReturn(Optional.of(user));
        assertThat(userController.register(new ConcurrentModel(), user))
                .isEqualTo("redirect:/users/login");
    }

    @Test
    public void whenRegisterDuplicateUserThenRegistrationViewWithError() {
        var expectedError = "Пользователь с такой почтой уже существует";
        var user = new User(1, "ivan@test.com", "Ivan", "123");
        when(userService.save(user)).thenReturn(Optional.empty());
        var model = new ConcurrentModel();
        assertThat(userController.register(model, user))
                .isEqualTo("users/register");
        assertThat(model.getAttribute("error")).isEqualTo(expectedError);
    }

    @Test
    public void whenGetLoginPageThenLoginView() {
        assertThat(userController.getLoginPage()).isEqualTo("users/login");
    }

    @Test
    public void whenLoginUserThenSessionHasUserAndRedirectToIndex() {
        var user = new User(1, "ivan@test.com", "Ivan", "123");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .thenReturn(Optional.of(user));
        var session = new MockHttpSession();
        assertThat(userController.loginUser(new ConcurrentModel(), user, session))
                .isEqualTo("redirect:/");
        assertThat(session.getAttribute("user")).isEqualTo(user);
    }

    @Test
    public void whenLoginMissingUserThenLoginViewWithError() {
        var expectedError = "Почта или пароль введены неверно";
        var user = new User(1, "ivan@test.com", "Ivan", "123");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .thenReturn(Optional.empty());
        var model = new ConcurrentModel();
        assertThat(userController.loginUser(model, user, new MockHttpSession()))
                .isEqualTo("users/login");
        assertThat(model.getAttribute("error")).isEqualTo(expectedError);
    }

    @Test
    public void whenLogoutThenInvalidateSessionAndRedirectToLogin() {
        var session = new MockHttpSession();
        assertThat(userController.logout(session))
                .isEqualTo("redirect:/users/login");
        assertThat(session.isInvalid()).isTrue();
    }
}
