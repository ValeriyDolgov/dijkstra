create table if not exists route_status
(
    id   serial primary key,
    name varchar
);

insert into route_status(id, name)
values (1, 'NEW'),
       (2, 'TAKEN');

create table if not exists users
(
    id              serial primary key,
    fio             varchar(255),
    email           varchar(255),
    password        varchar(255),
    mobile_number   varchar(255),
    roles           varchar(2048),
    driver_id_number varchar
);

insert into users(fio, email, password, mobile_number, roles, driver_id_number)
VALUES ('Admin Admin', 'admin@email.com', '{bcrypt}$2a$10$011/UrsbOoZNLSknzP.Ng.G6YBo7vvAaxswdWUnGnRwaeDiJA8gL2', null, '["SUPER_ADMIN"]',
        null);

create table if not exists route
(
    id            serial primary key,
    route_coords  JSONB,
    start_address text,
    end_address   text,
    start_date    timestamp,
    cargo_name    text,
    weight        decimal,
    status_id     integer not null
        constraint route_status_fk references route_status,
    driver_id     integer
        constraint route_driver_fk references users,
    manager_id    integer not null
        constraint route_manager_fk references users
);

