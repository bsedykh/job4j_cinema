package ru.job4j.cinema.provider;

public interface ContentProvider {
    byte[] getContent(String path);
}
