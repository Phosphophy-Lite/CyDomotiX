DROP TABLE IF EXISTS House;
DROP TABLE IF EXISTS `User`;
DROP TABLE IF EXISTS Room;
DROP TABLE IF EXISTS Tool;
DROP TABLE IF EXISTS ConnectedObject;

CREATE TABLE House (
                       id_house INT PRIMARY KEY,
                       name VARCHAR(20)
);

CREATE TABLE `User` (
                      id_user INT PRIMARY KEY,
                      username VARCHAR(20) UNIQUE NOT NULL,
                      age INT,
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
                      id_room INT,
                      name VARCHAR(50),
                      type VARCHAR(50),
                      id_house INT NOT NULL,
                      PRIMARY KEY(id_room),
                      FOREIGN KEY(id_house) REFERENCES House(id_house)
);

CREATE TABLE Tool (
                      id_tool INT,
                      name VARCHAR(50),
                      id_room INT,
                      PRIMARY KEY(id_tool),
                      FOREIGN KEY(id_room) REFERENCES Room(id_room)
);

CREATE TABLE ConnectedObject (
                        id_object INT,
                        mode VARCHAR(20),
                        connectivity VARCHAR(20),
                        brand VARCHAR(20),
                        last_interaction VARCHAR(50),
                        battery_status INT,
                        is_active BOOLEAN,
                        object_type VARCHAR(50),
                        name VARCHAR(20),
                        id_room INT,
                        PRIMARY KEY(id_object),
                        FOREIGN KEY(id_room) REFERENCES Room(id_room)
);
