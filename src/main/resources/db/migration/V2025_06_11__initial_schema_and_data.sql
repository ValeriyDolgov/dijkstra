CREATE TABLE road_surface_config
(
    id         SERIAL PRIMARY KEY,
    surface    VARCHAR(50),
    multiplier NUMERIC(10, 5) NOT NULL
);

CREATE TABLE road_type_config
(
    id         SERIAL PRIMARY KEY,
    type       VARCHAR(50),
    multiplier NUMERIC(10, 5) NOT NULL
);

INSERT INTO road_surface_config (surface, multiplier)
VALUES ('asphalt', 1.0),
       ('gravel', 1.2),
       ('dirt', 1.5),
       ('paved', 1.0),
       ('unpaved', 3.0),
       ('concrete', 1.0),
       ('sand', 6.0),
       ('unknown', 1.5);

INSERT INTO road_type_config (type, multiplier)
VALUES ('motorway', 0.9),
       ('trunk', 1.0),
       ('primary', 1.2),
       ('secondary', 1.5),
       ('tertiary', 1.7),
       ('residential', 2.0),
       ('service', 2.5),
       ('unclassified', 2.2),
       ('track', 3.5),
       ('unknown', 1.5);
