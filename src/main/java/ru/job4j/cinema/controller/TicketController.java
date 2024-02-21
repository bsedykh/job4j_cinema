package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.ticket.TicketService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/buy")
    public String buy(Model model, @ModelAttribute Ticket ticket, HttpSession session) {
        var user = (User) session.getAttribute("user");
        ticket.setUserId(user.getId());
        var result = ticketService.save(ticket);
        String message;
        if (result.isEmpty()) {
            message = """
                    Не удалось приобрести билет на заданное место. Вероятно, оно уже занято.
                    Перейдите на страницу бронирования билетов и попробуйте снова.
                    """;
        } else {
            message = String.format("Вы успешно приобрели билет: ряд %d, место %d.",
                    result.get().getRowNumber(), result.get().getPlaceNumber());
        }
        model.addAttribute("message", message);
        return "tickets/buy";
    }
}
