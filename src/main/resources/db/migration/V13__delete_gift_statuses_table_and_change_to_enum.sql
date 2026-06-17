alter table tab_gifts
add column status text;

alter table tab_shopping_items
add column status text;

update tab_gifts
set status = case status_id
    when 1 then 'IDEA'
    when 2 then 'PURCHASED'
    when 3 then 'DELIVERED'
end;

update tab_shopping_items
set status = case status_id
    when 1 then 'IDEA'
    when 2 then 'PLANNED'
    when 3 then 'TO_BUY'
    when 4 then 'BOUGHT'
    when 5 then 'CANCELED'
end;

alter table tab_gifts
alter column status set not null;

alter table tab_shopping_items
alter column status set not null;

alter table tab_gifts
drop constraint fk_gifts_status_id;

drop index if exists idx_gifts_person_status;
drop index if exists idx_gifts_status_id;

alter table tab_gifts
drop column status_id;

alter table tab_shopping_items
drop constraint fk_shopping_item_status;

drop index if exists idx_shopping_item_status;
drop index if exists idx_shopping_item_category_status;

alter table tab_shopping_items
drop column status_id;

create index idx_gifts_status_id on tab_gifts(status);
create index idx_gifts_person_status on tab_gifts(person_id, status);
create index idx_shopping_item_status on tab_shopping_items(status);
create index idx_shopping_item_category_status on tab_shopping_items(category_id, status);

drop table tab_gift_statuses;
drop table tab_shopping_item_statuses;