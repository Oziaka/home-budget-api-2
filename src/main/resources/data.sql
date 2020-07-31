INSERT INTO user_role (user_role_id, description, role_name, is_default)
VALUES (1, ' Default role', 'ROLE_USER', true);
VALUES (2, 'God of server role likes root', 'ROLE_ADMIN', false);

INSERT INTO category (category_id, description, name, type, is_default)
VALUES (1, 'Good expense', 'Food', 0, true);
VALUES (2, 'Here comes the money', 'Salary', 1, true);
VALUES (3, 'To get or receive something from someone with the intention of giving it back after a period of time',
        'Borrow', 2, true);
VALUES (4, 'An act of lending something', 'Loan', 3, true);
VALUES (5, 'Return borrowed item', 'Borrow back', 4, true);
VALUES (6, 'Receiving the loan', 'Loan back', 5, true);
VALUES (7, null, 'Entertainment', 0, true);
VALUES (8, 'Everybody like getting presents', 'Present', 1, true);

INSERT INTO user_item_key (user_item_key_id, name)
VALUES (1, 'favoriteWalletId')