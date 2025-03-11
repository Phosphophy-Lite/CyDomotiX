DROP TABLE IF EXISTS Maison;
DROP TABLE IF EXISTS Utilisateur;
DROP TABLE IF EXISTS Piece;
DROP TABLE IF EXISTS Outil;
DROP TABLE IF EXISTS Objet;

CREATE TABLE Maison(
                       id_Maison INT PRIMARY KEY,
                       Nom VARCHAR(20),
);

CREATE TABLE Utilisateur(
                            id_Utilisateur INT,
                            Pseudonyme VARCHAR(20),
                            Age INT,
                            Sexe VARCHAR(20),
                            DateNaissance DATE,
                            TypeDeMembre VARCHAR(20),
                            Photo VARCHAR(20),
                            Prenom VARCHAR(20),
                            Nom VARCHAR(20),
                            TypeAcces VARCHAR(20),
                            NiveauExp VARCHAR(20),
                            NbPts INT,
                            MotDePasse VARCHAR(20),
                            estConnecte BOOLEAN,
                            id_Maison INT NOT NULL,
                            PRIMARY KEY(id_Utilisateur),
                            FOREIGN KEY(id_Maison) REFERENCES Maison(id_Maison)
);

CREATE TABLE Pieces(
                       id_Piece INT,
                       Nom VARCHAR(50),
                       Type VARCHAR(50),
                       id_Maison INT NOT NULL,
                       PRIMARY KEY(id_Piece),
                       FOREIGN KEY(id_Maison) REFERENCES Maison(id_Maison)
);

CREATE TABLE Outils(
                       id_Outils INT,
                       Nom VARCHAR(50),
                       id_Piece INT,
                       PRIMARY KEY(id_Outils),
                       FOREIGN KEY(id_Piece) REFERENCES Pieces(id_Piece)
);

CREATE TABLE Objet(
                      id_Objets INT,
                      Mode VARCHAR(20),
                      Connectivité VARCHAR(20),
                      Marque VARCHAR(20),
                      DerniereInterraction VARCHAR(50),
                      EtatBatterie INT,
                      EstActiver LOGICAL,
                      TypeObjet VARCHAR(50),
                      Nom VARCHAR(20),
                      id_Piece INT,
                      PRIMARY KEY(id_Objets),
                      FOREIGN KEY(id_Piece) REFERENCES Pieces(id_Piece)
);
