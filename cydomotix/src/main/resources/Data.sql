DROP TABLE IF EXISTS House;
DROP TABLE IF EXISTS `User`;
DROP TABLE IF EXISTS Room;
DROP TABLE IF EXISTS Tool;
DROP TABLE IF EXISTS ConnectedObject;
DROP TABLE IF EXISTS ObjectAttribute;
DROP TABLE IF EXISTS AttributeValue;
DROP TABLE IF EXISTS ObjectType;

CREATE TABLE House (
                       id_house INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(20)
);

CREATE TABLE `User` (
                      id_user INT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(20) UNIQUE NOT NULL,
                      gender VARCHAR(20),
                      birth_date DATE,
                      member_type VARCHAR(20),
                      photo VARCHAR(255),
                      first_name VARCHAR(20),
                      last_name VARCHAR(20),
                      access_type VARCHAR(20) NOT NULL,
                      experience_level VARCHAR(20),
                      points INT,
                      password VARCHAR(255) NOT NULL,
                      is_connected BOOLEAN,
                      FOREIGN KEY(id_house) REFERENCES House(id_house)
);

CREATE TABLE Room (
                      id_room INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50),
                      type VARCHAR(50),
                      id_house INT NOT NULL,
                      FOREIGN KEY(id_house) REFERENCES House(id_house)
);

CREATE TABLE Tool (
                      id_tool INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50),
                      id_room INT,
                      FOREIGN KEY(id_room) REFERENCES Room(id_room)
);

-- Table des objects connectés
CREATE TABLE ConnectedObject (
                        id_object INT AUTO_INCREMENT PRIMARY KEY,
                        mode VARCHAR(20),
                        connectivity VARCHAR(20),
                        brand VARCHAR(20),
                        last_interaction DATE,
                        battery_status INT,
                        is_active BOOLEAN,
                        object_type VARCHAR(50),
                        name VARCHAR(20) NOT NULL,
                        id_room INT, -- Relie l'objet à une pièce de la maison
                        id_type INT, -- Relie l'objet à un type spécifique ("Thermostat", "TV" ..)
                        FOREIGN KEY(id_room) REFERENCES Room(id_room),
                        FOREIGN KEY (id_type) REFERENCES ObjectType(id_object_type)
);

-- Table des types d'objets connectés
CREATE TABLE ObjectType (
                            id_object_type INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(20) UNIQUE NOT NULL -- Ex : "Thermostat"
);

-- Table des attributs d'objets
CREATE TABLE ObjectAttribute (
                                id_object_attribute INT AUTO_INCREMENT PRIMARY KEY,
                                name VARCHAR(20) UNIQUE NOT NULL, -- Ex: "Température", "Mode"
                                value_type VARCHAR(50), -- "STRING", "INT" etc
                                object_type_id INT, -- Relie l'attribut à un type spécifique d'objet connecté
                                FOREIGN KEY (object_type_id) REFERENCES ObjectType(id_object_type)
);

-- Table des valeurs des attributs d'objets
CREATE TABLE AttributeValue (
                                 id_attribute_value INT AUTO_INCREMENT PRIMARY KEY,
                                 value VARCHAR(50),
                                 connected_object_id INT, -- Relie la valeur à un objet spécifique
                                 object_attribute_id INT, -- Relie la valeur à un attribut spécifique
                                 FOREIGN KEY (connected_object_id) REFERENCES ConnectedObject(id_object),
                                 FOREIGN KEY (object_attribute_id) REFERENCES ObjectAttribute(id_object_attribute)
);

