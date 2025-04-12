INSERT INTO ObjectType(name)
VALUES ('Thermostat'),
       ('Distributeur de croquette'),
       ('Lampe');

INSERT INTO ObjectAttribute(name, value_type, object_type_id)
VALUES ('Température actuelle', 'TEMPERATURE', 1),
       ('Température cible', 'TEMPERATURE', 1),
       ('Intervalle', 'MINUTES', 2),
       ('Couleur', 'STRING', 3),
       ('Intensité', 'PERCENTAGE', 3);

INSERT INTO Room(name)
VALUES ('Test'),
       ('AutreTest');

INSERT INTO ConnectedObject(name, mode, connectivity, brand, last_interaction, battery_status, power, is_active, id_type, id_room)
VALUES ('Lampe Philips HUE', 'AUTOMATIC', 'BLUETOOTH', 'Philips', '2025-03-24 19:45:00', 85, 9, FALSE, 3, 1),
       ('Thermostat Salon', 'ECO', 'WIFI_STRONG', 'Netatmo', '2025-03-25 14:17:00', 65, 3600, FALSE, 1, 2),
       ('Distributeur pour chat', 'AUTOMATIC', 'WIFI_STRONG', 'Catit Pixi Smart', '2025-03-25 12:05:00', 90, 5, FALSE, 2, 1);

INSERT INTO AttributeValue(int_value, double_value, string_value, connected_object_id, object_attribute_id)
VALUES (null, null, 'Blanc', 1, 4),
       (70, null, null, 1, 5),
       (21, null, null, 2, 1),
       (23, null, null, 2, 2),
       (160, null, null, 3, 3);

INSERT INTO PowerChangeEvent (connected_object_id, power, timestamp)
VALUES (1, 7.5, '2025-04-01 08:00:00'),
       (1, 9, '2025-04-02 09:30:00'),
       (1, 6.5, '2025-04-02 10:15:00'),
       (2, 3600, '2025-04-05 06:00:00'),
       (2, 1800, '2025-04-08 07:45:00'),
       (2, 2000, '2025-04-09 12:30:00');

INSERT INTO UsageEvent (connected_object_id, status, timestamp)
VALUES (1, true, '2025-04-02 08:00:00'),
       (1, false, '2025-04-07 23:00:00'),
       (1, true, '2025-04-08 18:45:00'),
       (1, false, '2025-04-08 19:25:00'),
       (2, true, '2025-04-01 06:00:00'),
       (2, false, '2025-04-02 23:30:00'),
       (2, true, '2025-04-06 07:00:00'),
       (2, false, '2025-04-07 09:00:00'),
       (3, true, '2025-03-28 07:00:00'),
       (3, false, '2025-04-03 23:00:00');




