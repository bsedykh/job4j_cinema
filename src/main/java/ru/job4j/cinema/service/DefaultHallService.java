package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.HallDto;
import ru.job4j.cinema.repository.HallRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DefaultHallService implements HallService {
    private final HallRepository hallRepository;

    public DefaultHallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    @Override
    public Optional<HallDto> findById(int id) {
        var result = Optional.<HallDto>empty();
        var hall = hallRepository.findById(id);
        if (hall.isPresent()) {
            result = Optional.of(
                    new HallDto(hall.get().getId(),
                            hall.get().getName(),
                            hall.get().getRowCount(),
                            hall.get().getPlaceCount())
            );
        }
        return result;
    }

    @Override
    public Collection<HallDto> findAll() {
        return hallRepository.findAll().stream()
                .map(hall -> new HallDto(
                        hall.getId(),
                        hall.getName(),
                        hall.getRowCount(),
                        hall.getPlaceCount()))
                .collect(Collectors.toList());
    }
}
