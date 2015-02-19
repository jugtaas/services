create table event
(
id bigserial not null,
title character varying(255),
subtitle character varying(255)
);
alter table only event add constraint event_pkey primary key (id);
