create table tab_shopping_item_categories (
    id bigint generated always as identity primary key,
    name varchar(20) not null unique,
    color varchar(9) not null,
    created_at timestamp with time zone not null default current_timestamp
);

insert into tab_shopping_item_categories (name, color)
values
    ('TECH', '#06B6D4'),
    ('HEALTH', '#10B981'),
    ('MAKEUP', '#EC4899'),
    ('OTHER', '#6B7280');

create table tab_shopping_item_statuses (
    id bigint generated always as identity primary key,
    name varchar(20) not null unique,
    color varchar(9) not null,
    created_at timestamp with time zone not null default current_timestamp
);

insert into tab_shopping_item_statuses (name, color)
values
    ('IDEA', '#A855F7'),
    ('PLANNED', '#3B82F6'),
    ('TO_BUY', '#F59E0B'),
    ('BOUGHT', '#22C55E'),
    ('CANCELED', '#EF4444');

create table tab_shopping_items (
    id bigint generated always as identity primary key,
    title varchar(128) not null,
    link text,
    category_id bigint not null,
    price decimal(10,2) not null,
    status_id bigint not null,
    created_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone not null default current_timestamp,

    constraint fk_shopping_item_category
        foreign key (category_id)
            references tab_shopping_item_categories(id),

    constraint fk_shopping_item_status
        foreign key (status_id)
            references tab_shopping_item_statuses(id)
);

create index idx_shopping_item_category
    on tab_shopping_items(category_id);

create index idx_shopping_item_status
    on tab_shopping_items(status_id);

create index idx_shopping_item_category_status
    on tab_shopping_items(category_id, status_id);
