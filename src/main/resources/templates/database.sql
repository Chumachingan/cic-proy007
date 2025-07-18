drop table if exists coche cascade
drop sequence if exists coche_seq
create sequence coche_seq start with 1 increment by 50
create table coche (
        encendido boolean not null,
        potencia float(53),
        id bigint not null,
        marca varchar(255),
        primary key (id)
    )