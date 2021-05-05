CREATE TABLE IF NOT EXISTS weather (
    id BIGINT NOT NULL PRIMARY KEY,
    date DATE,
    location VARCHAR(255),
    pretty_location VARCHAR(255),
    max_temp DOUBLE PRECISION,
    min_temp DOUBLE PRECISION,
    average_temp DOUBLE PRECISION,
    max_wind DOUBLE PRECISION,
    precipitation DOUBLE PRECISION,
    visibility DOUBLE PRECISION,
    humidity DOUBLE PRECISION,
    uv_index DOUBLE PRECISION,
    weather_condition VARCHAR(1023),
    sunrise VARCHAR(255),
    sunset VARCHAR(255)
);
