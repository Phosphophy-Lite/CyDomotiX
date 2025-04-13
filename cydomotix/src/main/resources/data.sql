INSERT INTO ObjectType(name)
VALUES ('Thermostat'),
       ('Distributeur de croquette'),
       ('Lampe'),
       ('Caméra de sécurité'),
       ('Réfrigérateur'),
       ('Aspirateur robot'),
       ('Assistant vocal'),
       ('Machine à café');


INSERT INTO ObjectAttribute(name, value_type, object_type_id)
VALUES ('Température actuelle', 'TEMPERATURE', 1),
       ('Température cible', 'TEMPERATURE', 1),
       ('Intervalle', 'MINUTES', 2),
       ('Couleur', 'STRING', 3),
       ('Intensité', 'PERCENTAGE', 3),
       ('Qualité vidéo', 'STRING', 4),
       ('Détection de mouvement', 'BOOLEAN', 4),
       ('Température intérieure', 'TEMPERATURE', 5),
       ('Mode congélation', 'STRING', 5),
       ('Temps de nettoyage', 'MINUTES', 6),
       ('Surface de nettoyage', 'SURFACE', 6),
       ('Langue', 'STRING', 7),
       ('Volume', 'PERCENTAGE', 7),
       ('Type de café', 'STRING', 8),
       ('Quantité eau', 'VOLUME', 8);


INSERT INTO Room(name)
VALUES ('Salon'),
       ('Cuisine'),
       ('Chambre'),
       ('Salle de bain'),
       ('Garage');


INSERT INTO ConnectedObject(name, mode, connectivity, brand, last_interaction, battery_status, power, is_active, id_type, id_room)
VALUES ('Lampe Philips HUE', 'AUTOMATIC', 'BLUETOOTH', 'Philips', '2025-03-24 19:45:00', 85, 9, FALSE, 3, 1),
       ('Thermostat Salon', 'ECO', 'WIFI_STRONG', 'Netatmo', '2025-03-25 14:17:00', 65, 3600, FALSE, 1, 2),
       ('Distributeur pour chat', 'AUTOMATIC', 'WIFI_STRONG', 'Catit Pixi Smart', '2025-03-25 12:05:00', 90, 5, FALSE, 2, 1),
       ('Caméra Arlo Extérieure', 'PERFORMANCE', 'WIFI_STRONG', 'Arlo', '2025-04-10 09:30:00', 95, 7, FALSE, 4, 5),
       ('Samsung Family Hub', 'AUTOMATIC', 'ETHERNET', 'Samsung', '2025-04-12 08:00:00', 100, 120, FALSE, 5, 2),
       ('Roomba i7+', 'SCHEDULED', 'WIFI_STRONG', 'iRobot', '2025-04-11 16:45:00', 75, 50, FALSE, 6, 1),
       ('Google Nest Audio', 'MANUAL', 'WIFI_WEAK', 'Google', '2025-04-13 07:50:00', 100, 15, FALSE, 7, 3),
       ('Nespresso Vertuo Next', 'ECO', 'BLUETOOTH', 'Nespresso', '2025-04-13 06:15:00', 80, 1350, FALSE, 8, 2);


INSERT INTO AttributeValue(int_value, double_value, string_value, connected_object_id, object_attribute_id)
VALUES (null, null, 'Blanc', 1, 4),
       (70, null, null, 1, 5),
       (21, null, null, 2, 1),
       (23, null, null, 2, 2),
       (160, null, null, 3, 3);

-- Caméra
INSERT INTO AttributeValue(int_value, double_value, string_value, connected_object_id, object_attribute_id)
VALUES
    (null, null, '1080p', 4, 6),
    (1, null, null, 4, 7);

-- Réfrigérateur
INSERT INTO AttributeValue(int_value, double_value, string_value, connected_object_id, object_attribute_id)
VALUES
    (4, null, null, 5, 8),
    (null, null, 'Rapide', 5, 9);

-- Aspirateur
INSERT INTO AttributeValue(int_value, double_value, string_value, connected_object_id, object_attribute_id)
VALUES
    (45, null, null, 6, 10),
    (null, 12.0, null, 6, 11);

-- Assistant vocal
INSERT INTO AttributeValue(int_value, double_value, string_value, connected_object_id, object_attribute_id)
VALUES
    (null, null, 'Français', 7, 12),
    (60, null, null, 7, 13);

-- Machine à café
INSERT INTO AttributeValue(int_value, double_value, string_value, connected_object_id, object_attribute_id)
VALUES
    (null, null, 'Espresso', 8, 14),
    (null, 120.0, null, 8, 15);


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




