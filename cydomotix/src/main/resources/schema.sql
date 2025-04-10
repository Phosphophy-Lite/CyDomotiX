DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Room;
DROP TABLE IF EXISTS ConnectedObject;
DROP TABLE IF EXISTS ObjectAttribute;
DROP TABLE IF EXISTS AttributeValue;
DROP TABLE IF EXISTS ObjectType;
DROP TABLE IF EXISTS VerificationToken;
DROP TABLE IF EXISTS DeletionRequest;
DROP TABLE IF EXISTS UserAction;

CREATE TABLE Users (
                        id_user INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(255) UNIQUE NOT NULL,
                        email VARCHAR(255) UNIQUE,
                        gender ENUM('MALE', 'FEMALE', 'OTHER'),
                        access_type ENUM('USER', 'GESTION', 'ADMIN', 'DEV') NOT NULL,
                        birth_date DATE,
                        member_type ENUM('FATHER', 'MOTHER', 'CHILD'),
                        photo VARCHAR(255),
                        first_name VARCHAR(255),
                        last_name VARCHAR(255),
                        experience_level VARCHAR(20),
                        points INT,
                        password VARCHAR(255) NOT NULL,
                        enabled BOOLEAN NOT NULL,
                        approved_by_admin BOOLEAN NOT NULL
);

CREATE TABLE Room (
                      id_room INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) UNIQUE NOT NULL
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
                                 power DOUBLE,
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
);

CREATE TABLE DeletionRequest (
                                   id_del_request INT AUTO_INCREMENT PRIMARY KEY,
                                   reason VARCHAR(255),
                                   request_date TIMESTAMP,
                                   connected_object_id INT,
                                   object_type_id INT,
                                   target_type ENUM('CONNECTED_OBJECT', 'OBJECT_TYPE'),
                                   requester_user_id INT NOT NULL,
                                   FOREIGN KEY (connected_object_id) REFERENCES ConnectedObject(id_object) ON DELETE CASCADE,
                                   FOREIGN KEY (object_type_id) REFERENCES ObjectType(id_object_type) ON DELETE CASCADE,
                                   FOREIGN KEY (requester_user_id) REFERENCES Users(id_user) ON DELETE CASCADE
);

CREATE TABLE UserAction (
                            id_user_action INT PRIMARY KEY AUTO_INCREMENT,
                            timestamp TIMESTAMP,
                            action_type ENUM('LOGIN', 'ADD_OBJECT', 'UPDATE_OBJECT', 'DELETE_OBJECT', 'ON_OBJECT', 'OFF_OBJECT', 'UPDATE_USER', 'DELETE_USER', 'ADD_TYPE', 'DELETE_TYPE'),
                            author VARCHAR(255),
                            related_entity VARCHAR(255)
);

CREATE TABLE PowerChangeEvent (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  connected_object_id INT NOT NULL,
                                  power DOUBLE NOT NULL,
                                  timestamp TIMESTAMP NOT NULL,
                                  FOREIGN KEY (connected_object_id) REFERENCES ConnectedObject(id_object) ON DELETE CASCADE
);

CREATE TABLE UsageEvent (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            connected_object_id INT NOT NULL,
                            status BOOLEAN NOT NULL,
                            timestamp TIMESTAMP NOT NULL,
                            FOREIGN KEY (connected_object_id) REFERENCES ConnectedObject(id_object) ON DELETE CASCADE
);
