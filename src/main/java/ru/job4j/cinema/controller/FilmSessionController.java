package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.service.filmsession.FilmSessionService;

@Controller
@RequestMapping("/film-sessions")
public class FilmSessionController {
    private final FilmSessionService filmSessionService;

    public FilmSessionController(FilmSessionService filmSessionService) {
        this.filmSessionService = filmSessionService;
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
}
