package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.ticket.Sql2oTicketRepository;
import ru.job4j.cinema.util.Sql2oClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oTicketRepositoryTest {
    private static Sql2oTicketRepository sql2oTicketRepository;

    @BeforeAll
    public static void initRepositories() throws IOException {
        sql2oTicketRepository = new Sql2oTicketRepository(Sql2oClient.create());
    }

    @AfterEach
    public void clearTickets() {
        var tickets = sql2oTicketRepository.findAll();
        for (var ticket : tickets) {
            sql2oTicketRepository.deleteById(ticket.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var ticket = sql2oTicketRepository.save(new Ticket(
                        0, 1, 10, 20, 1))
                .orElseThrow();
        var savedTicket = sql2oTicketRepository.findById(ticket.getId())
                .orElseThrow();
        assertThat(savedTicket).usingRecursiveComparison().isEqualTo(ticket);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var ticket1 = sql2oTicketRepository.save(new Ticket(
                        0, 1, 10, 20, 1))
                .orElseThrow();
        var ticket2 = sql2oTicketRepository.save(new Ticket(
                        0, 2, 10, 20, 1))
                .orElseThrow();
        var ticket3 = sql2oTicketRepository.save(new Ticket(
                        0, 3, 10, 20, 1))
                .orElseThrow();
        var result = sql2oTicketRepository.findAll();
        assertThat(result).containsExactlyInAnyOrder(ticket1, ticket2, ticket3);
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oTicketRepository.findAll()).isEmpty();
        assertThat(sql2oTicketRepository.findById(1)).isEmpty();
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var ticket = sql2oTicketRepository.save(new Ticket(
                        0, 1, 10, 20, 1))
                .orElseThrow();
        assertThat(sql2oTicketRepository.deleteById(ticket.getId())).isTrue();
        assertThat(sql2oTicketRepository.findById(ticket.getId())).isEmpty();
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oTicketRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenSaveDuplicateTicketThenGetEmptyOptional() {
        var ticket = sql2oTicketRepository.save(new Ticket(
                        0, 1, 10, 20, 1))
                .orElseThrow();
        assertThat(sql2oTicketRepository.save(ticket)).isEmpty();
        var tickets = sql2oTicketRepository.findAll();
        assertThat(tickets).containsExactlyInAnyOrder(ticket);
    }
}
