create table tab_users (
    id uuid primary key,
    username varchar(100) not null unique,
    password varchar(255) not null,
    role varchar(50) not null default 'ADMIN',
    created_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone not null default current_timestamp
);
