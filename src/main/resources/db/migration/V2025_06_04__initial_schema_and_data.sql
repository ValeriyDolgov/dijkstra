create table if not exists route
(
    id            serial primary key,
    route_coords  JSONB,
    start_address text,
    end_address   text,
    start_date    timestamp,
    cargo_name    text,
    weight        decimal,
    status_id     serial not null
        constraint route_status_fk references route_status
);

create table if not exists users
(
    id              serial primary key,
    fio             varchar(255),
    email           varchar(255),
    password        varchar(255),
    mobile_number   varchar(255),
    roles           varchar(2048),
    drive_id_number varchar
);

create table if not exists route_status
(
    id   serial primary key,
    name varchar
);

insert into route_status(id, name)
values (1, 'NEW'),
       (2, 'TAKEN');

