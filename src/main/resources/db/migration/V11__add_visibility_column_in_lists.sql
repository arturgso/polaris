alter table tab_gift_list
    add column in_vault bool default false;

update tab_gift_list
set in_vault = false where in_vault is null;

alter table tab_gift_list
    alter column in_vault set not null;

alter table tab_shopping_list
    add column in_vault bool default false;

update tab_shopping_list
set in_vault = false where in_vault is null;

alter table tab_shopping_list
    alter column in_vault set not null;
