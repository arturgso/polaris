alter table tab_gifts
    add column in_vault bool default false;

update tab_gifts
set in_vault = false where in_vault is null;

alter table tab_gifts
    alter column in_vault set not null;

alter table tab_shopping_items
    add column in_vault bool default false;

update tab_shopping_items
set in_vault = false where in_vault is null;

alter table tab_shopping_items
    alter column in_vault set not null;
