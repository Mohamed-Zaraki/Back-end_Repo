USE project_db;

ALTER TABLE salle_tp
ADD CONSTRAINT CHECK  (Nombre_poste >=0 AND Nombre_tables >=0);



SELECT * from Salle_tp;
ALTER TABLE Salle_tp DROP COLUMN Capacité; /* dir hadi bach ywli yahsb automatiquement capacite nidyrh ynktb*/
ALTER TABLE Salle_tp 
ADD COLUMN Capacité INT GENERATED ALWAYS AS ((Nombre_Poste * 2) + Nombre_tables * 6) STORED; 

INSERT INTO Salle_tp (Nom_Salle ,Nombre_Poste , Nombre_tables , internet , id_utilisateur) VALUES('A21' ,15 , 1 , 0 ,1);
INSERT INTO Salle_tp (Nom_Salle ,Nombre_Poste , Nombre_tables , internet , id_utilisateur) VALUES('A22' ,15 , 1 , 0 ,1);
INSERT INTO Salle_tp (Nom_Salle ,Nombre_Poste , Nombre_tables , internet , id_utilisateur) VALUES('A23' ,13 , 0 , 0 ,1);
INSERT INTO Salle_tp (Nom_Salle ,Nombre_Poste , Nombre_tables , internet , id_utilisateur) VALUES('A24' ,13 , 0 , 0 ,1);
INSERT INTO Salle_tp (Nom_Salle ,Nombre_Poste , Nombre_tables , internet , id_utilisateur) VALUES('A25' ,15 , 1 , 0 ,1);
INSERT INTO Salle_tp (Nom_Salle ,Nombre_Poste , Nombre_tables , internet , id_utilisateur) VALUES('A31' ,12 , 0 , 1 ,1);
INSERT INTO Salle_tp (Nom_Salle ,Nombre_Poste , Nombre_tables , internet , id_utilisateur) VALUES('A32' ,13 , 0 , 1 ,1);
INSERT INTO Salle_tp (Nom_Salle ,Nombre_Poste , Nombre_tables , internet , id_utilisateur) VALUES('A33' ,15 , 0 , 1 ,1);
INSERT INTO Salle_tp (Nom_Salle ,Nombre_Poste , Nombre_tables , internet , id_utilisateur) VALUES('UNIX' ,16 , 0 , 1 ,1);
INSERT INTO Salle_tp (Nom_Salle ,Nombre_Poste , Nombre_tables , internet , id_utilisateur) VALUES('A34' ,13 , 1 , 1 ,1);
INSERT INTO Salle_tp (Nom_Salle ,Nombre_Poste , Nombre_tables , internet , id_utilisateur) VALUES('A41' ,13 , 0 , 1 ,1);
INSERT INTO Salle_tp (Nom_Salle ,Nombre_Poste , Nombre_tables , internet , id_utilisateur) VALUES('A42' ,13 , 0 , 1 ,1);
INSERT INTO Salle_tp (Nom_Salle ,Nombre_Poste , Nombre_tables , internet , id_utilisateur) VALUES('A43' ,15 , 1 , 1 ,1);





