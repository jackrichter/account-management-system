insert into bank_db.transactions (amount, transaction_date, account_id)
values (100, NOW(), 1);
insert into bank_db.transactions (amount, transaction_date, account_id)
values (500, DATE_ADD(NOW(), INTERVAL 5 MINUTE ), 1);
insert into bank_db.transactions (amount, transaction_date, account_id)
values (-100, DATE_ADD(NOW(), INTERVAL 7 MINUTE ), 1);
insert into bank_db.transactions (amount, transaction_date, account_id)
values (-200, DATE_ADD(NOW(), INTERVAL 10 MINUTE ), 1);
insert into bank_db.transactions (amount, transaction_date, account_id)
values (500, DATE_ADD(NOW(), INTERVAL 15 MINUTE ), 1);
insert into bank_db.transactions (amount, transaction_date, account_id)
values (-100, DATE_ADD(NOW(), INTERVAL 30 MINUTE ), 1);
insert into bank_db.transactions (amount, transaction_date, account_id)
values (100, DATE_ADD(NOW(), INTERVAL 40 MINUTE ), 1);
insert into bank_db.transactions (amount, transaction_date, account_id)
values (-500, DATE_ADD(NOW(), INTERVAL 45 MINUTE ), 1);
insert into bank_db.transactions (amount, transaction_date, account_id)
values (500, DATE_ADD(NOW(), INTERVAL 53 MINUTE ), 1);
insert into bank_db.transactions (amount, transaction_date, account_id)
values (-1000, DATE_ADD(NOW(), INTERVAL 67 MINUTE ), 1);
insert into bank_db.transactions (amount, transaction_date, account_id)
values (1000, DATE_ADD(NOW(), INTERVAL 1 DAY ), 1);
