package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.HallDto;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.hall.HallRepository;
import ru.job4j.cinema.service.hall.DefaultHallService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultHallServiceTest {
    private DefaultHallService hallService;
    private HallRepository hallRepository;

    @BeforeEach
    public void initServices() {
        hallRepository = mock(HallRepository.class);
        hallService = new DefaultHallService(hallRepository);
    }

    @Test
    public void whenFindAll() {
        var halls = List.of(
                new Hall(1, "Hall1", "Description1", 10, 10),
                new Hall(2, "Hall2", "Description2", 20, 20),
                new Hall(3, "Hall3", "Description3", 30, 30)
        );
        var expectedHalls = halls.stream()
                .map(hall -> new HallDto(hall.getId(), hall.getName(),
                        hall.getRowCount(), hall.getPlaceCount()))
                .toList();
        when(hallRepository.findAll()).thenReturn(halls);
        assertThat(hallService.findAll()).containsExactlyInAnyOrderElementsOf(expectedHalls);
    }

    @Test
    public void whenFindById() {
        var hall = new Hall(1, "Hall1", "Description1", 10, 10);
        when(hallRepository.findById(hall.getId())).thenReturn(Optional.of(hall));
        var expectedHall = new HallDto(hall.getId(), hall.getName(),
                hall.getRowCount(), hall.getPlaceCount());
        var actualHall = hallService.findById(hall.getId()).orElseThrow();
        assertThat(actualHall).isEqualTo(expectedHall);
    }

    @Test
    public void whenCannotFindByIdThenEmptyOptional() {
        when(hallRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThat(hallService.findById(1)).isEmpty();
    }
}
