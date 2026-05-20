create table tab_persons (
    id              uuid primary key,
    name            varchar(100) not null,
    birthday_month  smallint not null check(birthday_month between 1 and 12),
    birthday_day    smallint not null check(birthday_day between 1 and 31),
    created_at      timestamptz not null default current_timestamp,
    updated_at      timestamptz not null default current_timestamp,

    constraint date_validate check (
        (birthday_month, birthday_day) not in (
            (2, 30), (2, 31),
            (4, 31), (6, 31),
            (9, 31), (11, 31)
        )
        and not (birthday_month = 2 and birthday_day > 29)
        )
);

create index idx_persons_birthday on tab_persons(birthday_month, birthday_day);
create index idx_persons_name on tab_persons(name);
