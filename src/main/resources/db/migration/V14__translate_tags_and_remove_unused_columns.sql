update tab_events
set tag = case tag
    when 'NONE' then 'SEM_EVENTO'
    when 'BIRTHDAY' then 'ANIVERSARIO'
    when 'CHRISTMAS' then 'NATAL'
    when 'MARRIAGE' then 'CASAMENTO'
    else tag
end;

alter table tab_events
drop column color;

alter table tab_events
drop column name;

update tab_shopping_item_categories
set tag = case tag
    when 'HEALTH' then 'SAUDE'
    when 'MAKEUP' then 'MAQUIAGEM'
    when 'OTHER' then 'SEM_CATEGORIA'
    else tag
end;

alter table tab_shopping_item_categories
drop column color;

alter table tab_shopping_item_categories
drop column name;