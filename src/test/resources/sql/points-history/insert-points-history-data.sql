INSERT INTO customer_loyalty.customer_points(id, customer_id, total_points, created_at, updated_at)
VALUES ('40ef642b-8a0b-4737-a84f-f4deb551ed4e', '419d5f27-758e-4aaa-81ee-ecf27074eaf3', 40, now() - INTERVAL '1 DAY', now() - INTERVAL '1 DAY');

INSERT INTO customer_loyalty.points_history(id, points_id, customer_id, points, transaction_type, loyalty_type, reason,
                                            created_at)
VALUES ('ac98b028-a523-4aa3-b8cc-2d0ef3a1223d', '40ef642b-8a0b-4737-a84f-f4deb551ed4e',
        '419d5f27-758e-4aaa-81ee-ecf27074eaf3', 30, 'ADD', 'ORDER', 'Order with number #436272986', now() - INTERVAL '1 DAY'),
       ('ea10e9cc-eab8-434a-a756-56e4b6368d1f', '40ef642b-8a0b-4737-a84f-f4deb551ed4e',
        '419d5f27-758e-4aaa-81ee-ecf27074eaf3', 10, 'ADD', 'ORDER', 'Order with number #436272361', now() - INTERVAL '2 DAY');


