package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultTicketServiceTest {
    private DefaultTicketService ticketService;
    private TicketRepository ticketRepository;

    @BeforeEach
    public void initServices() {
        ticketRepository = mock(TicketRepository.class);
        ticketService = new DefaultTicketService(ticketRepository);
    }

    @Test
    public void whenSuccessfulSaveThenTicket() {
        var ticket = new Ticket(1, 1, 10, 10, 1);
        when(ticketRepository.save(ticket)).thenReturn(Optional.of(ticket));
        assertThat(ticketService.save(ticket)).isEqualTo(Optional.of(ticket));
    }

    @Test
    public void whenFailedSaveThenEmptyOptional() {
        var ticket = new Ticket(1, 1, 10, 10, 1);
        when(ticketRepository.save(ticket)).thenReturn(Optional.empty());
        assertThat(ticketService.save(ticket)).isEqualTo(Optional.empty());
    }
}
