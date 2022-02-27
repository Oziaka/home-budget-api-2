INSERT INTO user_role (user_role_id, description, role_name, is_default)
VALUES
    (1, 'Default role', 'USER', true),
    (2, 'God of server role likes root', 'ADMIN', false);

INSERT INTO category (category_id, description, name, type, is_default)
VALUES
    (1, 'Good expense', 'Food', 0, true),
    (2, 'Here comes the money', 'Salary', 1, true),
    (3, 'To get or receive something from someone with the intention of giving it back after a period of time','Borrow', 2, true),
    (4, 'An act of lending something', 'Loan', 3, true),
    (5, 'Return borrowed item', 'Borrow back', 4, true),
    (6, 'Receiving the loan', 'Loan back', 5, true),
    (7, 'Entertainment', 'Entertainment', 0, true),
    (8, 'Everybody like getting presents', 'Present', 1, true);

INSERT INTO user_item_key (user_item_key_id, name)
VALUES (1, 'favoriteWalletId')
