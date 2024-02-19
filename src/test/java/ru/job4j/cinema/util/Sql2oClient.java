package ru.job4j.cinema.util;

import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;

import java.io.IOException;
import java.util.Properties;

public class Sql2oClient {
    public static Sql2o create() throws IOException {
        var properties = new Properties();
        try (var inputStream = Sql2oClient.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        return configuration.databaseClient(datasource);
    }
}
