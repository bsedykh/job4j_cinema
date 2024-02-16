package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

@Service
public class SimpleTicketService implements TicketService {
    @Override
    public Optional<Ticket> save(Ticket ticket) {
        return Optional.of(ticket);
    }
}
