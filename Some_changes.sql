USE project_db;
SHOW TABLES;

SELECT * FROM Stock;
delete from Stock where id_équipement;
describe utilisateur;

ALTER TABLE utilisateur
MODIFY COLUMN Nom_utilisateur VARCHAR(255);

describe Stock;

SHOW CREATE TABLE stock;

ALTER TABLE stock DROP CHECK Stock_chk_4;

ALTER TABLE stock ADD CONSTRAINT Stock_ch_1 CHECK (
    équipement_Neuf >= 0 AND équipement_Utiliser >= 0
);