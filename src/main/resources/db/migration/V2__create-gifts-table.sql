create table tab_gifts (
    id uuid primary key,
    title varchar(255) not null,
    link text,
    person_id uuid not null,
    event text not null,
    status text not null,
    created_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone not null default current_timestamp,
    constraint fk_person_id foreign key (person_id) references tab_persons(id) on delete cascade
);
