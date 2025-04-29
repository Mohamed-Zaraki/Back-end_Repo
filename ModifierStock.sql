DESCRIBE Stock;

ALTER TABLE Stock
DROP COLUMN Etat;

SHOW CREATE TABLE Stock;

ALTER TABLE STOCK 
DROP foreign key stock_ibfk_1 ;
ALTER TABLE Stock
MODIFY COLUMN id_utilisateur INT NOT NULL; /* hneya ghil ki gbila bghir nrod foreign key not null*/
ALTER table Stock 
ADD CONSTRAINT fk1_id_utilisateur 
FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur(id_utilisateur);

ALTER TABLE Stock
CHANGE COLUMN Quantité équipement_Neuf INTEGER DEFAULT 0, /*renommit quantité*/
ADD COLUMN équipement_Utiliser INTEGER DEFAULT 0; /* lezouj etithom par default 0*/