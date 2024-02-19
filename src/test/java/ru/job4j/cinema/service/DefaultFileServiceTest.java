package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.provider.ContentProvider;
import ru.job4j.cinema.repository.FileRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultFileServiceTest {
    private DefaultFileService fileService;
    private FileRepository fileRepository;
    private ContentProvider contentProvider;

    @BeforeEach
    public void initServices() {
        fileRepository = mock(FileRepository.class);
        contentProvider = mock(ContentProvider.class);
        fileService = new DefaultFileService(fileRepository, contentProvider);
    }

    @Test
    public void whenGetFileByIdThenReturnFileDto() {
        var file = new File(1, "test.txt", "files\\test.txt");
        var content = new byte[] {1, 2, 3};
        when(fileRepository.findById(file.getId())).thenReturn(Optional.of(file));
        when(contentProvider.getContent(file.getPath())).thenReturn(content);
        var expectedFileDto = new FileDto(file.getName(), content);
        var actualFileDto = fileService.getFileById(file.getId()).orElseThrow();
        assertThat(actualFileDto).isEqualTo(expectedFileDto);
    }

    @Test
    public void whenCannotGetFileByIdThenReturnEmptyOptional() {
        when(fileRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThat(fileService.getFileById(1)).isEmpty();
    }
}
