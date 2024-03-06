CREATE TABLE location (
                          id SERIAL PRIMARY KEY,
                          name TEXT NOT NULL,
                          latitude NUMERIC NOT NULL,
                          longitude NUMERIC NOT NULL
);

CREATE TABLE type (
                      id SERIAL PRIMARY KEY,
                      name TEXT NOT NULL,
                      icon TEXT NOT NULL
);

CREATE TABLE weather (
                         id SERIAL PRIMARY KEY,
                         location_id INT NOT NULL REFERENCES location(id),
                         type_id INT NOT NULL REFERENCES type(id),
                         timestamp TEXT NOT NULL,
                         temperature NUMERIC NOT NULL,
                         humidity SMALLINT NOT NULL,
                         wind_speed NUMERIC NOT NULL,
                         wind_direction SMALLINT NOT NULL,
                         title TEXT NOT NULL,
                         description TEXT NOT NULL
);