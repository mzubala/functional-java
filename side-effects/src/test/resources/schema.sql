create table if not exists customers
(
    id         uuid primary key,
    first_name text   not null,
    last_name  text   not null,
    version    bigint not null
);

create table if not exists accounts
(
    number        character varying(17) primary key,
    customer_id   uuid   not null references customers (id),
    currency_code character(3),
    balance       numeric(2),
    version       bigint not null
);

create sequence if not exists account_number_sequence;
