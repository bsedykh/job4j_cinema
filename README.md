# job4j_cinema
В рамках проекта разработан сайт для покупки билетов в кинотеатр. Основные функции:
- Регистрация / вход пользователей
- Просмотр киносеансов и списка показываемых фильмов
- Покупка билета на сеанс

## Используемые технологии
- Java 17, Spring Boot 2.7.18 (Web, Thymeleaf, Test)
-  PostgreSQL 16.1, Liquibase 4.15.0, Sql2o 1.6.0, DBCP 2.9.0

## Требования к окружению
- Java 17
- Maven 3.9.5
- PostgreSQL 16.1

## Запуск проекта
1. Создать новую базу данных **cinema**.
2. Перейти в папку с проектом.
3. В файлах **db/liquibase.properties**, **src/main/resources/application.properties** скорректировать настройки подключения к БД **cinema**.
4. Создать схему базы данных:

    `mvn -P production liquibase:update`

5. Создать jar:

    `mvn package`

6. Запустить программу: 

    `java -jar target/job4j_cinema-1.0-SNAPSHOT.jar`

7. Перейти в браузере по адресу: http://localhost:8080/

## Примеры страниц
### Расписание сеансов:
![film-sessions](https://github.com/bsedykh/job4j_cinema/assets/84812761/cf81f16d-7be8-40a0-b79d-754b4e46d1c0)

### Покупка билета:
![buy](https://github.com/bsedykh/job4j_cinema/assets/84812761/4457a170-cd00-4b57-bad1-884a6e6bb63b)

### Кинотека:
![films](https://github.com/bsedykh/job4j_cinema/assets/84812761/5e462eaa-950f-43b9-ade6-01cf7161e1d5)

### Регистрация пользователя:
![register](https://github.com/bsedykh/job4j_cinema/assets/84812761/d51fea21-cb97-427a-919f-1afcc8bb71cd)
