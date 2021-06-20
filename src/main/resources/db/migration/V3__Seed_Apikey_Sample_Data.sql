INSERT INTO apikey (apikey, rate_limit) VALUES ('apikey_1', 5);

INSERT INTO apikey_usage (apikey_id, request_time) VALUES(1, now());
INSERT INTO apikey_usage (apikey_id, request_time) VALUES(1, now());
INSERT INTO apikey_usage (apikey_id, request_time) VALUES(1, now());


INSERT INTO apikey (apikey, rate_limit) VALUES ('apikey_2', 5);

INSERT INTO apikey_usage (apikey_id, request_time) VALUES(2, now());
INSERT INTO apikey_usage (apikey_id, request_time) VALUES(2, now());
INSERT INTO apikey_usage (apikey_id, request_time) VALUES(2, now());