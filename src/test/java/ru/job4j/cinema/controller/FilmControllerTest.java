package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmControllerTest {
    private FilmService filmService;
    private FilmController filmController;

    @BeforeEach
    public void initServices() {
        filmService = mock(FilmService.class);
        filmController = new FilmController(filmService);
    }

    @Test
    public void whenGetAllThenPageWithFilms() {
        var films = List.of(
                new FilmDto(1, "Film1", "Description1",
                        2024, 18, 120,
                        "Genre1", 1),
                new FilmDto(2, "Film2", "Description2",
                        2024, 18, 120,
                        "Genre2", 2)
        );
        when(filmService.findAll()).thenReturn(films);
        var model = new ConcurrentModel();
        var view = filmController.getAll(model);
        assertThat(view).isEqualTo("films/list");
        assertThat(model.getAttribute("films")).isEqualTo(films);
    }
}
