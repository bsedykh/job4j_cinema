package ru.job4j.cinema.repository.ticket;

import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketRepository {
    Collection<Ticket> findAll();

    Optional<Ticket> findById(int id);

    Optional<Ticket> save(Ticket ticket);

    boolean deleteById(int id);
}
