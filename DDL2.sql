CREATE TABLE IF NOT EXISTS Subit (
  Numero_Salle VARCHAR(10) NOT NULL,
  id_Panne INTEGER NOT NULL,
  FOREIGN KEY (Numero_Salle) REFERENCES Salle_tp(Numero_Salle) ON DELETE CASCADE,
  FOREIGN KEY (id_Panne) REFERENCES Panne(id_Panne) ON DELETE CASCADE,
  PRIMARY KEY (Numero_Salle, id_Panne)
);


CREATE table IF NOT EXISTS Panne(id_Panne INTEGER auto_increment ,
Détails VARCHAR(255) NOT NULL ,
Degré_Criticité TINYINT CHECK (Degré_Criticité BETWEEN 1 AND 3) ,
Type_Panne ENUM('Logicielle', 'Matérielle') NOT NULL ,
Date_Déclaration DATE ,
primary key(id_Panne))
;

CREATE TABLE IF NOT EXISTS Réservation( id_réservation INTEGER auto_increment ,
date_réservation DATE NOT NULL ,
jour ENUM('Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi', 'Dimanche') NOT NULL ,
Heure_Debut TIME NOT NULL ,
Heure_fin TIME NOT NULL ,
Nom_Enseignant VARCHAR(60) NOT NULL , 
PRIMARY KEY (id_réservation));

CREATE TABLE IF NOT EXISTS est_réserver (
id_réservation INTEGER NOT NULL ,
Numero_Salle VARCHAR(10) NOT NULL ,
foreign  key (id_réservation) references Réservation(id_réservation) ON DELETE CASCADE ,
foreign key (Numero_Salle) references Salle_tp(Numero_Salle) ON DELETE CASCADE, 
	PRIMARY KEY (id_réservation));

describe Stock;
/*henya bghit nzid constraint ftype bch maydich negatif*/
ALTER TABLE Stock
modify Column équipement_Neuf INTEGER CHECK(équipement_Neuf >0) ,
modify Column équipement_Utiliser INTEGER CHECK(équipement_Utiliser >0) ;


show tables;

describe emploi_du_temps;
select * from stock;

DROP TABLE concerne;

ALTER TABLE emploi_du_temps
ADD COLUMN Nom_Salle VARCHAR(10) NOT NULL,
ADD CONSTRAINT fk_nom_salle
FOREIGN KEY (Nom_Salle) REFERENCES salle_tp(Nom_Salle) ON DELETE CASCADE;

ALTER TABLE emploi_du_temps
MODIFY COLUMN Nom_Salle VARCHAR(10) NOT NULL;
ALTER TABLE subit
RENAME column Numero_Salle TO Nom_Salle;

