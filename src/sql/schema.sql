create table event
(
  id bigserial not null,
  title character varying(255),
  subtitle character varying(255),
  dateandtime timestamp,
  url character varying(255),
  place character varying(255),
  abstract character varying(255),
  slideshow character varying(255)
);
alter table only event add constraint event_pkey primary key (id);

create table person
(
  id bigserial not null,
  name character varying(255),
  surname character varying(255),
  email character varying(255),
  url character varying(255)
);
alter table only person add constraint person_pkey primary key (id);

create table speaker
(
  id bigserial not null,
  event bigint NOT NULL,
  person bigint NOT NULL
);
alter table only speaker add constraint speaker_pkey primary key (id);