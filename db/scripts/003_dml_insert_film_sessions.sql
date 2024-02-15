insert into halls(name, row_count, place_count, description)
values('Основной', 10, 10, 'Основной зал');

insert into film_sessions(film_id, halls_id, start_time, end_time, price)
values(1, 1, '2024-02-15 12:00:00', '2024-02-15 14:00:00', 500);
insert into film_sessions(film_id, halls_id, start_time, end_time, price)
values(1, 1, '2024-02-15 14:00:00', '2024-02-15 16:00:00', 500);
insert into film_sessions(film_id, halls_id, start_time, end_time, price)
values(2, 1, '2024-02-16 12:00:00', '2024-02-16 17:00:00', 600);
insert into film_sessions(film_id, halls_id, start_time, end_time, price)
values(3, 1, '2024-02-17 12:00:00', '2024-02-17 17:00:00', 600);
