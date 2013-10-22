use user;

create table user(
	email varchar PRIMARY KEY,
	login varchar,
	password blob,
	first_name varchar,
	last_name varchar,
	gender varchar,
	birth_date timestamp,
	favorites set
);

CREATE INDEX tokens_login_idx ON tokens(login);
CREATE INDEX tokens_password_idx ON tokens(password);
CREATE INDEX tokens_first_name_idx ON tokens(first_name);
CREATE INDEX tokens_last_name_idx ON tokens(last_name);
CREATE INDEX tokens_gender_idx ON tokens(gender);
CREATE INDEX tokens_birth_date_idx ON tokens(birth_date);