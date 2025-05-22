# Général
* Spécifier `Dates:ajd`
* Fin de simulation `Dates:finSimulation`
* Cas de A2
* Devise en Ar

# Possesseurs
* `Personnes:Zety` 10%
* `Personnes:Lita` 90%

# Trésoreries
* `Trésoreries:litaVente`
* `Trésoreries:zetyPersonnel`

# Opérations
## ProjectMaison, `Dates:ajd`, devise en Ar
* `invOrdinateur1` `Dates:ajd`, posséder ordinateurBureau, valant 350000Ar, se dépréciant annuellement de 20%
* `invTerrainNord` Le 18 du 04-2025, posséder terrainNord, valant 700000Ar, s'appréciant annuellement de 8%
* `achatVillaAmb` Le 18 du 04-2025, acheter villaAmbatobe, valant 1250000Ar, s'appréciant annuellement de 5%, depuis `Trésoreries:litaVente` 

## DépensesMensuelles, `Dates:ajd`, devise en Ar
* `sortieLoyer` Le 18 du 04-2025, sortir 250000Ar depuis `Trésoreries:zetyPersonnel`
* `salaireMensuel` Le 18 du 04-2025, entrer 800000Ar vers `Trésoreries:litaVente`
* `transfertMensuel` Le 18 du 04-2025, transférer 300000Ar depuis `Trésoreries:zetyPersonnel` vers `Trésoreries:litaVente`
* `abonnementNet` Le 18 du 04-2025, sortir 9500Ar depuis `Trésoreries:zetyPersonnel`, jusqu'à date indéterminée tous les 2 du mois
* `revenuProjetWeb` Le 18 du 04-2025, entrer 150000Ar vers `Trésoreries:litaVente`, jusqu'à le 25 du 12-2025 tous les 25 du mois
* `épargneAutomatique` Le 18 du 04-2025, transférer 200000Ar depuis `Trésoreries:litaVente` vers `Trésoreries:zetyPersonnel`, jusqu'à le 31 du 12-2025 tous les 01 du mois