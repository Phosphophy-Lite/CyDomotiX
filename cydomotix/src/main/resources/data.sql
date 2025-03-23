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