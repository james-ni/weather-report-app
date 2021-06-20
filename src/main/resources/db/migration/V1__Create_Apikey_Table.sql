CREATE TABLE apikey (
    id         BIGINT AUTO_INCREMENT  PRIMARY KEY,
    apikey     VARCHAR(255) NOT NULL,
    rate_limit INT NOT NULL
);