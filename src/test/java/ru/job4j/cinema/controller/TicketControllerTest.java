package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.ticket.TicketService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketControllerTest {
    private TicketService ticketService;
    private TicketController ticketController;

    @BeforeEach
    public void initServices() {
        ticketService = mock(TicketService.class);
        ticketController = new TicketController(ticketService);
    }

    @Test
    public void whenBuyTicketThenSetUserIdInTicket() {
        var user = new User(1, "test@test.com", "Test", "password");
        var session = new MockHttpSession();
        session.setAttribute("user", user);

        var ticket = new Ticket(1, 1, 10, 10, 0);
        var ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        when(ticketService.save(ticketCaptor.capture())).thenReturn(Optional.empty());

        ticketController.buy(new ConcurrentModel(), ticket, session);
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
        var view = ticketController.buy(model, ticket, session);
        assertThat(view).isEqualTo("tickets/buy");
        assertThat(model.getAttribute("message")).isEqualTo(
                String.format("Вы успешно приобрели билет: ряд %d, место %d.",
                        ticket.getRowNumber(), ticket.getPlaceNumber())
        );
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
        var view = ticketController.buy(model, ticket, session);
        assertThat(view).isEqualTo("tickets/buy");
        assertThat(model.getAttribute("message"))
                .isEqualTo("""
                    Не удалось приобрести билет на заданное место. Вероятно, оно уже занято.
                    Перейдите на страницу бронирования билетов и попробуйте снова.
                    """);
    }
}
