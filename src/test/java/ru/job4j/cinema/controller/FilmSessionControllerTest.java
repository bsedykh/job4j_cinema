package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.dto.HallDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmSessionControllerTest {
    private static final LocalDateTime NOW = LocalDateTime.now();

    private FilmSessionService filmSessionService;
    private TicketService ticketService;
    private FilmSessionController filmSessionController;

    @BeforeEach
    public void initServices() {
        filmSessionService = mock(FilmSessionService.class);
        ticketService = mock(TicketService.class);
        filmSessionController = new FilmSessionController(filmSessionService, ticketService);
    }

    @Test
    public void whenGetAllThenPageWithFilmSessions() {
        var filmSessions = List.of(
                new FilmSessionDto(1, NOW, NOW,
                        new FilmDto(1, "Film1", "Description1",
                                2024, 18, 120,
                                "Genre1", 1),
                        new HallDto(1, "Hall1", 10, 10),
                        500),
        new FilmSessionDto(2, NOW, NOW,
                new FilmDto(2, "Film2", "Description2",
                        2024, 18, 120,
                        "Genre2", 2),
                new HallDto(1, "Hall1", 10, 10),
                500)
        );
        when(filmSessionService.findAll()).thenReturn(filmSessions);
        var model = new ConcurrentModel();
        var view = filmSessionController.getAll(model);
        assertThat(view).isEqualTo("film-sessions/list");
        assertThat(model.getAttribute("filmSessions")).isEqualTo(filmSessions);
    }

    @Test
    public void whenGetByIdThenPageWithFilmSession() {
        var filmSession = new FilmSessionDto(1, NOW, NOW,
                new FilmDto(1, "Film1", "Description1",
                        2024, 18, 120,
                        "Genre1", 1),
                new HallDto(1, "Hall1", 10, 10),
                500);
        when(filmSessionService.findById(filmSession.id()))
                .thenReturn(Optional.of(filmSession));
        var model = new ConcurrentModel();
        var view = filmSessionController.getById(model, filmSession.id());
        assertThat(view).isEqualTo("film-sessions/one");
        assertThat(model.getAttribute("filmSession")).isEqualTo(filmSession);
    }

    @Test
    public void whenGetByMissingIdThenPageWithError() {
        when(filmSessionService.findById(anyInt()))
                .thenReturn(Optional.empty());
        var model = new ConcurrentModel();
        var view = filmSessionController.getById(model, 1);
        assertThat(view).isEqualTo("errors/404");
        assertThat(model.getAttribute("message"))
                .isEqualTo("Сеанс с указанным идентификатором не найден");
    }

    @Test
    public void whenBuyTicketThenSetUserIdInTicket() {
        var user = new User(1, "test@test.com", "Test", "password");
        var session = new MockHttpSession();
        session.setAttribute("user", user);

        var ticket = new Ticket(1, 1, 10, 10, 0);
        var ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        when(ticketService.save(ticketCaptor.capture())).thenReturn(Optional.empty());

        filmSessionController.buy(new ConcurrentModel(), ticket, session);
        assertThat(ticketCaptor.getValue().getUserId()).isEqualTo(user.getId());
    }

    @Test
    public void whenBuyTicketThenPageWithPurchaseInfo() {
        var user = new User(1, "test@test.com", "Test", "password");
        var session = new MockHttpSession();
        session.setAttribute("user", user);

        var ticket = new Ticket(1, 1,
                10, 10, user.getId());
        when(ticketService.save(ticket)).thenReturn(Optional.of(ticket));

        var model = new ConcurrentModel();
        var view = filmSessionController.buy(model, ticket, session);
        assertThat(view).isEqualTo("film-sessions/buy");
        assertThat(model.getAttribute("message"))
                .isEqualTo("Вы успешно приобрели билет: "
                        + "ряд " + ticket.getRowNumber() + ", "
                        + "место " + ticket.getPlaceNumber() + ".");
    }

    @Test
    public void whenByTicketToOccupiedSeatThenPageWithError() {
        var user = new User(1, "test@test.com", "Test", "password");
        var session = new MockHttpSession();
        session.setAttribute("user", user);

        var ticket = new Ticket(1, 1,
                10, 10, user.getId());
        when(ticketService.save(ticket)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = filmSessionController.buy(model, ticket, session);
        assertThat(view).isEqualTo("film-sessions/buy");
        assertThat(model.getAttribute("message"))
                .isEqualTo("Не удалось приобрести билет на заданное место. "
                        + "Вероятно, оно уже занято. "
                        + "Перейдите на страницу бронирования билетов и попробуйте снова.");

    }
}
