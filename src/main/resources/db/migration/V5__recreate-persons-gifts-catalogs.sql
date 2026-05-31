drop table if exists tab_gifts cascade;
drop table if exists tab_gift_status cascade;
drop table if exists tab_gift_statuses cascade;
drop table if exists tab_events cascade;
drop table if exists tab_persons cascade;

create table tab_persons (
    id bigint generated always as identity primary key,
    name varchar(100) not null,
    birthday_month smallint,
    birthday_day smallint,
    created_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone not null default current_timestamp,

    constraint ck_persons_birthday_pair check (
        (birthday_month is null and birthday_day is null)
        or (birthday_month is not null and birthday_day is not null)
    ),
    constraint ck_persons_birthday_month check (
        birthday_month is null or birthday_month between 1 and 12
    ),
    constraint ck_persons_birthday_day check (
        birthday_day is null or birthday_day between 1 and 31
    ),
    constraint ck_persons_birthday_valid check (
        birthday_month is null
        or (
            not (birthday_month = 2 and birthday_day > 29)
            and not (birthday_month in (4, 6, 9, 11) and birthday_day = 31)
        )
    )
);

create index idx_persons_name on tab_persons(name);
create index idx_persons_birthday on tab_persons(birthday_month, birthday_day);

create table tab_events (
    id bigint generated always as identity primary key,
    name varchar(100) not null unique,
    color varchar(9) not null,
    created_at timestamp with time zone not null default current_timestamp
);

insert into tab_events (name, color)
values
    ('NONE', '#6B7280'),
    ('BIRTHDAY', '#EC4899'),
    ('CHRISTMAS', '#22C55E'),
    ('MARRIAGE', '#A855F7');

create table tab_gift_statuses (
    id bigint generated always as identity primary key,
    name varchar(100) not null unique,
    color varchar(9) not null,
    created_at timestamp with time zone not null default current_timestamp
);

insert into tab_gift_statuses (name, color)
values
    ('IDEA', '#A855F7'),
    ('PURCHASED', '#3B82F6'),
    ('DELIVERED', '#22C55E');

create table tab_gifts (
    id bigint generated always as identity primary key,
    title varchar(255) not null,
    link text,
    person_id bigint not null,
    event_id bigint not null,
    status_id bigint not null,
    created_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone not null default current_timestamp,

    constraint fk_gifts_person_id foreign key (person_id)
        references tab_persons(id) on delete cascade,
    constraint fk_gifts_event_id foreign key (event_id)
        references tab_events(id),
    constraint fk_gifts_status_id foreign key (status_id)
        references tab_gift_statuses(id)
);

create index idx_gifts_person_id on tab_gifts(person_id);
create index idx_gifts_event_id on tab_gifts(event_id);
create index idx_gifts_status_id on tab_gifts(status_id);
create index idx_gifts_person_status on tab_gifts(person_id, status_id);
create index idx_gifts_person_event on tab_gifts(person_id, event_id);
