

SELECT Salle_Tp.Nom_Salle
FROM Salle_Tp
JOIN installés ON Salle_Tp.Nom_Salle = installés.Nom_Salle
JOIN logiciel ON logiciel.id_logiciel = installés.id_logiciel
JOIN Ordinateur ON Ordinateur.Nom_Salle = Salle_Tp.Nom_Salle
WHERE Ordinateur.ram = '16 GB'
  AND logiciel.Nom_logiciel LIKE 'MATLAB'
  AND Salle_Tp.Capacité = (SELECT max(Capacité) from Salle_Tp) 
ORDER BY ABS(Salle_Tp.Capacité - 34) 
LIMIT 3;
