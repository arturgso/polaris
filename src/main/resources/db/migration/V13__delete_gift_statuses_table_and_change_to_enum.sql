alter table tab_gifts
add column status text;

-- alter table tab_shopping_items
-- add column status text;

update tab_gifts
set status = case status_id
    when 1 then 'IDEA'
    when 2 then 'PURCHASED'
    when 3 then 'DELIVERED'
end;

-- update tab_shopping_items
-- set status = case status_id
--     when 1 then 'IDEA'
--     when 2 then 'PLANNED'
--     when 3 then 'TO_BUY'
--     when 4 then 'BOUGHT'
--     when 5 then 'CANCELED'
-- end;

alter table tab_gifts
alter column status set not null;

-- alter table tab_shopping_items
-- alter column status set not null;

alter table tab_gifts
drop column status_id;

-- add drop in shopping_items


