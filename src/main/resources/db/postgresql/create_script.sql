CREATE USER weatheruser WITH PASSWORD 'db';

CREATE DATABASE weatherdb;

CREATE DATABASE weatherdb OWNER weatheruser;

CREATE TABLE location (
                          id SERIAL PRIMARY KEY,
                          name TEXT NOT NULL,
                          latitude NUMERIC NOT NULL,
                          longitude NUMERIC NOT NULL
);

CREATE TABLE type (
                          id SERIAL PRIMARY KEY,
                          name TEXT NOT NULL,
                          api_id INT NOT NULL,
                          icon_code TEXT NOT NULL
);

CREATE TABLE weather (
                         id SERIAL PRIMARY KEY,
                         location_id INT NOT NULL REFERENCES location(id),
                         type_id INT NOT NULL REFERENCES type(id),
                         date TEXT NOT NULL,
                         time TEXT NOT NULL,
                         temperature NUMERIC NOT NULL,
                         humidity SMALLINT NOT NULL,
                         wind_speed NUMERIC NOT NULL,
                         wind_direction SMALLINT NOT NULL,
                         description TEXT NOT NULL
);