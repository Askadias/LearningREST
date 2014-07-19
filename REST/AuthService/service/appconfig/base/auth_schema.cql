DROP KEYSPACE IF EXISTS forxy;
CREATE KEYSPACE forxy WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

USE forxy;

CREATE TABLE client (
    clientID TEXT,
    applicationName TEXT,
    applicationDescription TEXT,
    first_name TEXT,
    last_name TEXT,
    gender TEXT,
    birth_date TIMESTAMP,
    create_date timestamp,
    favorites SET<TEXT>,
    PRIMARY KEY (tokenKey)
);
//WITH CLUSTERING ORDER BY (create_date DESC);

CREATE INDEX create_date_idx ON token(create_date);
CREATE INDEX login_idx ON token(login);
CREATE INDEX password_idx ON token(password);
CREATE INDEX first_name_idx ON token(first_name);
CREATE INDEX last_name_idx ON token(last_name);
CREATE INDEX gender_idx ON token(gender);
CREATE INDEX birth_date_idx ON token(birth_date);