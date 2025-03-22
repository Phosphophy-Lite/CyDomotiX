INSERT INTO ObjectType(name)
VALUES ('Thermostat'),
       ('Distributeur de croquette'),
       ('Lampe');

INSERT INTO ObjectAttribute(name, value_type, object_type_id)
VALUES ('Température actuelle', 'INTEGER', 1),
       ('Température cible', 'INTEGER', 1),
       ('Intervalle', 'INTEGER', 2),
       ('Couleur', 'STRING', 3),
       ('Intensité', 'INTEGER', 3);