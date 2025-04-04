DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Room;
DROP TABLE IF EXISTS Tool;
DROP TABLE IF EXISTS ConnectedObject;
DROP TABLE IF EXISTS ObjectAttribute;
DROP TABLE IF EXISTS AttributeValue;
DROP TABLE IF EXISTS ObjectType;

CREATE TABLE Users (
                        id_user INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(255) UNIQUE NOT NULL,
                        email VARCHAR(255) UNIQUE,
                        gender ENUM('MALE', 'FEMALE', 'OTHER'),
                        access_type ENUM('USER', 'ADMIN', 'DEV') NOT NULL,
                        birth_date DATE,
                        member_type ENUM('FATHER', 'MOTHER', 'CHILD'),
                        photo VARCHAR(255),
                        first_name VARCHAR(255),
                        last_name VARCHAR(255),
                        experience_level VARCHAR(20),
                        points INT,
                        password VARCHAR(255) NOT NULL,
                        enabled BOOLEAN NOT NULL
);

CREATE TABLE Room (
                      id_room INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE Tool (
                      id_tool INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50),
                      id_room INT,
                      FOREIGN KEY(id_room) REFERENCES Room(id_room)
);

-- Table des types d'objets connectés
CREATE TABLE ObjectType (
                            id_object_type INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) UNIQUE NOT NULL -- Ex : "Thermostat"
);

-- Table des attributs d'objets
CREATE TABLE ObjectAttribute (
                                 id_object_attribute INT AUTO_INCREMENT PRIMARY KEY,
                                 name VARCHAR(255) NOT NULL, -- Ex: "Température", "Mode"
                                 value_type ENUM('STRING', 'INTEGER', 'DOUBLE', 'TEMPERATURE', 'HOURS', 'MINUTES', 'SECONDS', 'PERCENTAGE'),
                                 object_type_id INT NOT NULL, -- Relie l'attribut à un type spécifique d'objet connecté
                                 FOREIGN KEY (object_type_id) REFERENCES ObjectType(id_object_type) ON DELETE CASCADE,
                                 CONSTRAINT unique_attribute_per_type UNIQUE (name, object_type_id) -- On peut avoir un champ avec le même nom pour deux types différents mais pas deux fois le même champs pour 1 seul type
);

-- Table des objects connectés
CREATE TABLE ConnectedObject (
                                 id_object INT AUTO_INCREMENT PRIMARY KEY,
                                 name VARCHAR(255) NOT NULL,
                                 mode ENUM('AUTOMATIC', 'SCHEDULED', 'ECO', 'PERFORMANCE', 'NIGHT', 'AWAY', 'MANUAL'),
                                 connectivity ENUM('WIFI_WEAK', 'WIFI_STRONG', 'BLUETOOTH', 'ZIGBEE', 'Z_WAVE', 'ETHERNET', 'CELLULAR', 'NFC', 'INFRARED', 'NONE'),
                                 brand VARCHAR(50),
                                 last_interaction TIMESTAMP,
                                 battery_status INT,
                                 is_active BOOLEAN,
                                 id_room INT NOT NULL, -- Relie l'objet à une pièce de la maison
                                 id_type INT NOT NULL, -- Relie l'objet à un type spécifique ("Thermostat", "TV" ..)
                                 FOREIGN KEY(id_room) REFERENCES Room(id_room) ON DELETE CASCADE,
                                 FOREIGN KEY (id_type) REFERENCES ObjectType(id_object_type) ON DELETE CASCADE
);

-- Table des valeurs des attributs d'objets
CREATE TABLE AttributeValue (
                                id_attribute_value INT AUTO_INCREMENT PRIMARY KEY,
                                int_value INT,
                                double_value DOUBLE,
                                string_value VARCHAR(255),
                                connected_object_id INT NOT NULL, -- Relie la valeur à un objet spécifique
                                object_attribute_id INT NOT NULL, -- Relie la valeur à un attribut spécifique
                                FOREIGN KEY (connected_object_id) REFERENCES ConnectedObject(id_object) ON DELETE CASCADE,
                                FOREIGN KEY (object_attribute_id) REFERENCES ObjectAttribute(id_object_attribute) ON DELETE CASCADE
);

CREATE TABLE VerificationToken (
                                    id_token INT AUTO_INCREMENT PRIMARY KEY,
                                    token VARCHAR(255),
                                    expiry_date DATE,
                                    user_id INT NOT NULL,
                                    FOREIGN KEY (user_id) REFERENCES Users(id_user)
)