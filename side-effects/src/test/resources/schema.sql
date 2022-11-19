create table customers
(
    id         uuid primary key,
    first_name character varying(255)   not null,
    last_name  character varying(255)   not null,
    version    bigint not null
);

create table accounts
(
    number        character varying(17) primary key,
    customer_id   uuid   not null references customers (id),
    currency_code character(3),
    balance       numeric(2),
    version       bigint not null
);

create sequence account_number_sequence;