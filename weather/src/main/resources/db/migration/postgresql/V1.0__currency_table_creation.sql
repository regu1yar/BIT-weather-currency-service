CREATE TABLE IF NOT EXISTS currency (
    id BIGINT NOT NULL PRIMARY KEY,
    date DATE,
    currency_value VARCHAR(255),
    currency_name VARCHAR(255)
);
