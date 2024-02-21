package ru.job4j.cinema.service.hall;

import ru.job4j.cinema.dto.HallDto;

import java.util.Collection;
import java.util.Optional;

public interface HallService {
    Optional<HallDto> findById(int id);

    Collection<HallDto> findAll();
}
