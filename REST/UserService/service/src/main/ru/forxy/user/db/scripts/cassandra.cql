CREATE KEYSPACE Forxy
           WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

use Forxy;

CREATE TABLE user (
    email text PRIMARY KEY,
    password blob,
    login text,
    last_name text,
    first_name text,
    gender varchar,
    birth_day timestamp
) WITH comment='Main user data'
   AND read_repair_chance = 1.0;