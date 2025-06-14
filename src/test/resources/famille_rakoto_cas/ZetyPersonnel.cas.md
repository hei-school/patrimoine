# Général
* Spécifier `Dates:ajd` + 1 mois et 5 jours
* Fin de simulation `Dates:finSimulation` - 1 année 
* Cas de ZetyPersonnel 
* Devise en Ar

# Possesseurs
* `Personnes:Zety` 100%

# Trésoreries
* `Trésoreries:zetyPersonnel`
* `Trésoreries:zetyLoyerMaison`

# Créances
* `Créances:zetyCreance`

# Dettes
* `Dettes:zetyDette`

# Initialisation
* `objectifInitZetyPersonnel` `Dates:ajd`, objectif de 1000000Ar pour `Trésoreries:zetyPersonnel`
* `initComptePersonnel` `Dates:ajd`, entrer 1000000Ar vers `Trésoreries:zetyPersonnel`
 
# Opérations
## TrainDeVie, `Dates:ajd`, devise en Ar
* `abonnementWifi` `Dates:ajd`, sortir 40000Ar depuis `Trésoreries:zetyPersonnel`, jusqu'à date indéterminée tous les 15 du mois
* `nourriture` `Dates:ajd`, sortir 12000Ar depuis `Trésoreries:zetyLoyerMaison`, jusqu'à date indéterminée tous les 1 du mois
* `JIRAMA` `Dates:ajd`, sortir 100000Ar depuis `Trésoreries:zetyLoyerMaison`, jusqu'à date indéterminée tous les 5 du mois
 
## SalaireMensuel, `Dates:ajd`, devise en Ar
* `salaireMensuel` `Dates:ajd`, entrer 500000Ar vers `Trésoreries:zetyPersonnel`, jusqu'à date indéterminée tous les 31 du mois

# Suivi
* `nouvelObjectif` Le 02 du 02-2025, objectif de 2000000Ar pour `Trésoreries:zetyPersonnel`
* `correction1` le 02 du 02-2025, corriger 540000Ar dans `Trésoreries:zetyPersonnel`