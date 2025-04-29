USE porject_db;
show tables;
describe installés;

ALTER TABLE Ordinateur
Drop CONSTRAINT  ordinateur_ibfk_1;

ALTER TABLE Ordinateur
DROP COLUMN Nom_Salle;

SHOW CREATE TABLE instalés;


DROP table instalés;

CREATE TABLE IF NOT EXISTS installés(
 Nom_Salle varchar(10) NOT NULL , 
 foreign key (Nom_Salle) references Salle_Tp(Nom_Salle) ON DELETE CASCADE ,
 id_logiciel INTEGER auto_increment , 
 foreign key (id_logiciel) references logiciel(id_logiciel) ON DELETE CASCADE  , 
 primary key(id_logiciel , Nom_Salle));
 
 ALTER table Salle_Tp 
 