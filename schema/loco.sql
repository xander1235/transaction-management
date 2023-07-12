create database loco;

use loco;

create table transactions
(
    transaction_id bigint primary key,
    type           varchar(255)                        not null,
    amount         double                              not null,
    parent_id      bigint,
    created_at     timestamp default CURRENT_TIMESTAMP not null,
    modified_at    timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create index parent_id_idx on transactions (parent_id);

create index type_idx on transactions (type);