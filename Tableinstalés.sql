

DESCRIBE Instalés;

RENAME TABLE logiciels_installés TO Logiciel; /*lamarakch mcree Logiciel_instalés hadi wakila rahi fdak lfile lirslthlk lowl renamiha kbl mtexecute b "Logiciel" win kayna create*/

CREATE TABLE IF NOT exists Instalés( id_logiciel INTEGER , 
Code_Pc Varchar(30) ,
foreign key (id_logiciel) references logiciel(id_logiciel) ON DELETE CASCADE ,
foreign key (Code_pc) references Ordinateur(Code_pc) ON DELETE CASCADE ,
primary key (id_logiciel , Code_pc)
);

ALTER TABLE salle_tp
MODIFY COLUMN id_utilisateur INTeger NOT NULL;