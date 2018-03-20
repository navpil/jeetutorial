create schema myschema authorization sa;

create table myschema.person (
    guid varchar(4096),
    name varchar(4096),
    age integer,
);