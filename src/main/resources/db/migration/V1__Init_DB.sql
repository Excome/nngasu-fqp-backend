create sequence hibernate_sequence start 1 increment 1;

create table hibernate_sequences (
    sequence_name varchar(255) not null,
    next_val int8,
    primary key (sequence_name)
);

create table equipment (
    id int8 not null,
    count int4 not null,
    description varchar(255),
    name varchar(255),
    type varchar(255),
    primary key (id)
);

create table request (
    id int8 not null,
    audience varchar(255),
    description varchar(255),
    status boolean not null,
    author_id int8,
    responsible_id int8,
    primary key (id)
);

create table request_equipment (
    request_id int8 not null,
    equipment_id int8 not null
);

create table user_roles (
    user_id int8 not null,
    roles varchar(255)
);

create table users (
    id int8 not null,
    created_date timestamp,
    email varchar(255),
    first_name varchar(255),
    pass varchar(255),
    sur_name varchar(255),
    user_name varchar(255),
    verification_code varchar(255),
    enabled boolean not null,
    primary key (id)
);

insert into hibernate_sequences(sequence_name, next_val)
    values ('default',0);

alter table if exists request_equipment
    add constraint UK_dartc30y2m56f2sajxltp3etm unique (equipment_id);

alter table if exists request
    add constraint FKroq2b37lskxf9a0tmen9h1g4m
    foreign key (author_id) references users;

alter table if exists request
    add constraint FKs7swgpy8tgqoub4watqyn5cq1
    foreign key (responsible_id) references users;

alter table if exists request_equipment
    add constraint FK3o5eg7r9lvw448x2hs9nv6uu5
    foreign key (equipment_id) references equipment;

alter table if exists request_equipment
    add constraint FK6mseasxhnf05tn9kl0816k5rw
    foreign key (request_id) references request;

alter table if exists user_roles
    add constraint FKhfh9dx7w3ubf1co1vdev94g3f
    foreign key (user_id) references users;