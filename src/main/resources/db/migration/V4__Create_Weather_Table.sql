CREATE TABLE weather (
    id            BIGINT AUTO_INCREMENT  PRIMARY KEY,
    city          VARCHAR(20) NOT NULL,
    country       VARCHAR(20) NOT NULL,
    description   VARCHAR(255) NOT NULL,
    refreshed_at  TIMESTAMP WITH TIME ZONE NOT NULL
);