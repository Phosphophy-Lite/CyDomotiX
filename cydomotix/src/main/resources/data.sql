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
VALUES ('Lampe Philips HUE', 'AUTOMATIC', 'BLUETOOTH', 'Philips', '2025-03-24 19:45:00', 85, 5, FALSE, 3, 1),
       ('Thermostat Salon', 'ECO', 'WIFI_STRONG', 'Netatmo', '2025-03-25 14:17:00', 65, FALSE, 10, 1, 2),
       ('Distributeur pour chat', 'AUTOMATIC', 'WIFI_STRONG', 'Catit Pixi Smart', '2025-03-25 12:05:00', 90, 3, FALSE, 2, 1);

INSERT INTO AttributeValue(int_value, double_value, string_value, connected_object_id, object_attribute_id)
VALUES (null, null, 'Blanc', 1, 4),
       (70, null, null, 1, 5),
       (21, null, null, 2, 1),
       (23, null, null, 2, 2),
       (160, null, null, 3, 3);

