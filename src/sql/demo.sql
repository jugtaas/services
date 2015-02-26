insert into person (id, name, surname) values (1, 'Tiziano', 'Lattisi');
insert into person (id, name, surname) values (2, 'Riccardo', 'Tasso');
select setval('person_id_seq', 3);

insert into event (id, title, abstract) values (1, 'Sito Jugtaas', 'Breve instroduzione a...');
select setval('event_id_seq', 2);

insert into speaker (id, person, event) values (1, 1, 1);
insert into speaker (id, person, event) values (2, 2, 1);
select setval('speaker_id_seq', 3);
