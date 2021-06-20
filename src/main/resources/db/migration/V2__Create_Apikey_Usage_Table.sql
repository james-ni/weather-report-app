CREATE TABLE apikey_usage (
    id            BIGINT AUTO_INCREMENT  PRIMARY KEY,
    apikey_id     BIGINT REFERENCES apikey(id),
    request_time  TIMESTAMP WITH TIME ZONE NOT NULL
);