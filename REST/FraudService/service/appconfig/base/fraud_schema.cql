CREATE KEYSPACE IF NOT EXISTS fraud
 WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

USE fraud;

CREATE TABLE IF NOT EXISTS blacklist (
    value TEXT,
    type TEXT,
    is_active BOOLEAN,
    create_date TIMESTAMP,
    created_by TEXT,
    update_date TIMESTAMP,
    updated_by TEXT,
    PRIMARY KEY ((type, value))
);

CREATE TABLE IF NOT EXISTS whitelist (
    value TEXT,
    type TEXT,
    is_active BOOLEAN,
    create_date TIMESTAMP,
    created_by TEXT,
    update_date TIMESTAMP,
    updated_by TEXT,
    PRIMARY KEY ((type, value))
);
