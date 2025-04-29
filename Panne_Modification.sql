USE project_db;

show tables;

describe panne;

ALTER TABLE panne 
MODIFY column Détails varchar(255) NULL , 
MODIFY column Type_Panne enum('Logicielle' , 'Matérielle') NULL;

