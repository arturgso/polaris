alter table tab_gift_statuses
    rename column name to tag;

alter table tab_gift_statuses
    add column name varchar(100);

update tab_gift_statuses
set name = case tag
    when 'IDEA' then 'ideia'
    when 'PURCHASED' then 'comprado'
    when 'DELIVERED' then 'entregue'
end;

alter table tab_gift_statuses
    alter column name set not null;

alter table tab_events
    rename column name to tag;

alter table tab_events
    add column name varchar(100);

update tab_events
set name = case tag
    when 'NONE' then 'outro'
    when 'BIRTHDAY' then 'aniversario'
    when 'CHRISTMAS' then 'natal'
    when 'MARRIAGE' then 'casamento'
end;

alter table tab_events
    alter column name set not null;

alter table tab_shopping_item_statuses
    rename column name to tag;

alter table tab_shopping_item_statuses
    add column name varchar(20);

update tab_shopping_item_statuses
set name = case tag
    when 'IDEA' then 'ideia'
    when 'PLANNED' then 'planejado'
    when 'TO_BUY' then 'a comprar'
    when 'BOUGHT' then 'comprado'
    when 'CANCELED' then 'cancelado'
end;

alter table tab_shopping_item_statuses
    alter column name set not null;

alter table tab_shopping_item_categories
    rename column name to tag;

alter table tab_shopping_item_categories
    add column name varchar(20);

update tab_shopping_item_categories
set name = case tag
    when 'TECH' then 'tecnologia'
    when 'HEALTH' then 'saude'
    when 'MAKEUP' then 'maquiagem'
    when 'OTHER' then 'outro'
end;

alter table tab_shopping_item_categories
    alter column name set not null;
