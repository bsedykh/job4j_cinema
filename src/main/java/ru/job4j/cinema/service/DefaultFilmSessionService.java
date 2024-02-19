package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.dto.HallDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.repository.FilmSessionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DefaultFilmSessionService implements FilmSessionService {
    private final FilmSessionRepository filmSessionRepository;
    private final FilmService filmService;
    private final HallService hallService;

    public DefaultFilmSessionService(FilmSessionRepository filmSessionRepository,
                                     FilmService filmService,
                                     HallService hallService) {
        this.filmSessionRepository = filmSessionRepository;
        this.filmService = filmService;
        this.hallService = hallService;
    }

    @Override
    public Collection<FilmSessionDto> findAll() {
        var result = new ArrayList<FilmSessionDto>();
        var films = filmService.findAll().stream()
                .collect(Collectors.toMap(FilmDto::id, filmDto -> filmDto));
        var halls = hallService.findAll().stream()
                .collect(Collectors.toMap(HallDto::id, hallDto -> hallDto));
        for (var filmSession : filmSessionRepository.findAll()) {
            result.add(createDto(filmSession, films, halls));
        }
        return result;
    }

    @Override
    public Optional<FilmSessionDto> findById(int id) {
        var result = Optional.<FilmSessionDto>empty();
        var filmSession = filmSessionRepository.findById(id);
        if (filmSession.isPresent()) {
            var film = filmService.findById(filmSession.get().getFilmId());
            var hall = hallService.findById(filmSession.get().getHallId());
            result = Optional.of(
                    createDto(filmSession.get(),
                            film.orElse(null),
                            hall.orElse(null))
            );
        }
        return result;
    }

    private FilmSessionDto createDto(FilmSession filmSession,
                                     Map<Integer, FilmDto> films,
                                     Map<Integer, HallDto> halls) {
        var film = films.get(filmSession.getFilmId());
        var hall = halls.get(filmSession.getHallId());
        return createDto(filmSession, film, hall);
    }

    private FilmSessionDto createDto(FilmSession filmSession,
                                     FilmDto film,
                                     HallDto hall) {
        if (film == null || hall == null) {
            throw new IllegalStateException(
                    "Inconsistent model for film session with id = " + filmSession.getId());
        }
        return new FilmSessionDto(
                filmSession.getId(),
                filmSession.getStartTime(),
                filmSession.getEndTime(),
                film,
                hall,
                filmSession.getPrice());
    }
}
