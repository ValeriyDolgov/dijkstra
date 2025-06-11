CREATE TABLE road_config
(
    id         SERIAL PRIMARY KEY,
    surface    VARCHAR(50),
    road_type  VARCHAR(50),
    lanes      INTEGER DEFAULT 1,
    maxspeed   INTEGER DEFAULT 50,
    multiplier NUMERIC(10, 5) NOT NULL
);

-- Значения соответствуют логике getRoadMultiplier()

-- Asphalt & paved
INSERT INTO road_config (surface, road_type, lanes, maxspeed, multiplier)
VALUES ('asphalt', 'motorway', 2, 100, 1.0 * (2.0 / 2) * (50.0 / 100) * 0.9),
       ('asphalt', 'primary', 2, 90, 1.0 * (2.0 / 2) * (50.0 / 90) * 1.0),
       ('asphalt', 'secondary', 1, 60, 1.0 * (2.0 / 1) * (50.0 / 60) * 1.1),
       ('asphalt', 'tertiary', 1, 50, 1.0 * (2.0 / 1) * (50.0 / 50) * 1.2),
       ('asphalt', 'residential', 1, 30, 1.0 * (2.0 / 1) * (50.0 / 30) * 1.3);

-- Gravel
INSERT INTO road_config (surface, road_type, lanes, maxspeed, multiplier)
VALUES ('gravel', 'secondary', 1, 40, 1.2 * (2.0 / 1) * (50.0 / 40) * 1.1),
       ('gravel', 'residential', 1, 30, 1.2 * (2.0 / 1) * (50.0 / 30) * 1.3);

-- Dirt
INSERT INTO road_config (surface, road_type, lanes, maxspeed, multiplier)
VALUES ('dirt', 'tertiary', 1, 30, 1.5 * (2.0 / 1) * (50.0 / 30) * 1.2);

-- Unknown cases
INSERT INTO road_config (surface, road_type, lanes, maxspeed, multiplier)
VALUES (null, 'unknown', 1, 50, 1.3 * (2.0 / 1) * (50.0 / 50) * 1.4);
