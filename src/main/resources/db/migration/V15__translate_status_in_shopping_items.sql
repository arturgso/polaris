update tab_shopping_items
set status = case status
    when 'PLANNED' then 'PLANEJADO'
    when 'TO_BUY' then 'COMPRAR'
    when 'BOUGHT' then 'COMPRADO'
    when 'IDEA' then 'IDEIA'
    when 'CANCELED' then 'CANCELADO'
    else status
end;