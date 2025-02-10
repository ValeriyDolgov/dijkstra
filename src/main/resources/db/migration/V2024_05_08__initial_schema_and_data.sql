CREATE TABLE city
(
    id        serial primary key ,
    name      VARCHAR(255),
    latitude  DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL
);