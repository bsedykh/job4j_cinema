package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/film-sessions")
public class FilmSessionController {
    private final FilmSessionService filmSessionService;
    private final TicketService ticketService;

    public FilmSessionController(FilmSessionService filmSessionService,
                                 TicketService ticketService) {
        this.filmSessionService = filmSessionService;
        this.ticketService = ticketService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("filmSessions", filmSessionService.findAll());
        return "film-sessions/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var filmSession = filmSessionService.findById(id);
        if (filmSession.isEmpty()) {
            model.addAttribute("message",
                    "Сеанс с указанным идентификатором не найден");
            return "errors/404";
        }
        model.addAttribute("filmSession", filmSession.get());
        return "film-sessions/one";
    }

    @PostMapping("/buy")
    public String buy(Model model, @ModelAttribute Ticket ticket, HttpSession session) {
        var user = (User) session.getAttribute("user");
        ticket.setUserId(user.getId());
        var result = ticketService.save(ticket);
        String message;
        if (result.isEmpty()) {
            message = "Не удалось приобрести билет на заданное место. "
                    + "Вероятно, оно уже занято. "
                    + "Перейдите на страницу бронирования билетов и попробуйте снова.";
        } else {
            message = "Вы успешно приобрели билет: "
                    + "ряд " + result.get().getRowNumber() + ", "
                    + "место " + result.get().getPlaceNumber() + ".";
        }
        model.addAttribute("message", message);
        return "film-sessions/buy";
    }
}
