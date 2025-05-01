
use project_db;

show tables;

select * from est_réserver;



/*bla chat gpt sahbi*/
SELECT Nom_Salle 
FROM salle_tp
WHERE Nom_Salle NOT IN (
    SELECT salle_tp.Nom_Salle
    FROM salle_tp
    JOIN emploi_du_temps e ON salle_tp.Nom_Salle = e.Nom_Salle
    WHERE e.jour = 'Dimanche'
      AND e.Heure_Debut = '12:30:00'
      AND e.Heure_fin = '14:00:00'
    GROUP BY salle_tp.Nom_Salle
) AND  Nom_Salle NOT IN (

select Nom_Salle
from est_réserver);





