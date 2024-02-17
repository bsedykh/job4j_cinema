package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.HallDto;

import java.util.List;
import java.util.Optional;

public interface HallService {
    Optional<HallDto> findById(int id);

    List<HallDto> findAll();
}
