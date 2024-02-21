package ru.job4j.cinema.service.file;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.provider.ContentProvider;
import ru.job4j.cinema.repository.file.FileRepository;

import java.util.Optional;

@Service
public class DefaultFileService implements FileService {
    private final FileRepository fileRepository;
    private final ContentProvider contentProvider;

    public DefaultFileService(FileRepository sql2oFileRepository, ContentProvider contentProvider) {
        this.fileRepository = sql2oFileRepository;
        this.contentProvider = contentProvider;
    }

    @Override
    public Optional<FileDto> getFileById(int id) {
        var fileOptional = fileRepository.findById(id);
        return fileOptional.map(file -> new FileDto(file.getName(),
                contentProvider.getContent(file.getPath())));
    }
}
