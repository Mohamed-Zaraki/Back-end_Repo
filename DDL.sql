CREATE TABLE IF NOT EXISTS Utilisateur(id_utilisateur INTEGER auto_increment,

Nom_utilisateur VARCHAR(50) NOT NULL ,
Mot_de_passe VARCHAR(20) NOT NULL ,
Email VARCHAR(40) ,
PRIMARY KEY (id_utilisateur));

ALTER TABLE Utilisateur
ADD CONSTRAINT unique_nom_utilisateur UNIQUE (Nom_utilisateur);


CREATE TABLE IF NOT EXISTS Emploi_du_temps(id_emploi INTEGER auto_increment,
jour ENUM('Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi', 'Dimanche') NOT NULL,
Heure_Debut TIME NOT NULL ,
Heure_fin TIME NOT NULL ,
Nom_Enseignant VARCHAR(40),
Groupe VARCHAR(60) NOT NULL ,
Module varchar(30) NOT NULL,
PRIMARY KEY (id_emploi));

CREATE TABLE IF NOT EXISTS Stock(id_équipement INTEGER AUTO_INCREMENT ,
Nom_équipement varchar(40) NOT NULL ,
Quantité INTEGER NOT NULL ,
Etat ENUM('Neufs', 'Utilisés') NOT NULL ,
caractéristique VARCHAR(80) ,
id_utilisateur INTEGER ,
PRIMARY KEY (id_équipement)  ,
FOREIGN KEY (id_utilisateur) references Utilisateur(id_utilisateur) ON DELETE SET NULL);


CREATE TABLE IF NOT EXISTS Salle_Tp(Numero_Salle VARCHAR(10), 
Nombre_Poste INTEGER NOT NULL,
Capacité INTEGER NOT NULL,
Nombre_tables INTEGER ,
internet BOOLEAN NOT NULL DEFAULT FALSE ,
id_utilisateur INTEGER ,
PRIMARY KEY (Numero_Salle) ,
foreign key (id_utilisateur) references Utilisateur(id_utilisateur) ON DELETE SET NULL);

CREATE TABLE Concerne( id_emploi INTEGER auto_increment ,
Numero_Salle varchar(10) ,
primary key (id_emploi),
foreign key (id_emploi) references Emploi_du_temps(id_emploi) ,
foreign key (Numero_Salle) references Salle_Tp(Numero_Salle));

CREATE TABLE IF NOT EXISTS Ordinateur(Code_Pc VARCHAR(15) ,

Marque VARCHAR(20) NOT NULL,
Type_SE varchar(30) NOT NULL,
Processeur VARCHAR(30) NOT NULL,
Disque_Dur VARCHAR(20) NOT NULL,
Ram VARCHAR(30) ,
Numero_Salle VARCHAR(10),
PRIMARY KEY (Code_Pc) ,
foreign key (Numero_Salle) references Salle_Tp(Numero_Salle) ON DELETE SET NULL);


CREATE TABLE IF NOT EXISTS Logiciels_installés( id_logiciel INTEGER auto_increment ,

Nom_Logiciel VARCHAR(30) not null ,
primary key (id_logiciel));

ALTER TABLE Ordinateur
ADD column id_logiciel integer not NULL,
ADD constraint foreign key (id_logiciel) references Logiciels_installés( id_logiciel) ON DELETE SET NULL;

show tables;