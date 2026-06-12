# Général
* Spécifier Dates:ajd
* Fin de simulation Dates:finSimulation
* Cas de RakotoComplet
* Devise en Ar

# Possesseurs
* Personnes:Rakoto 60%
* Personnes:Nirina 40%

# Nombres
* Nombres:tauxAppreciationMaison: 5
* Nombres:tauxDepreciationVoiture: 10
* Nombres:loyer: 300000
* Nombres:salaire: 1500000

# Dates
* Dates:ajd: le 01 du 01-2025
* Dates:finSimulation: le 01 du 01-2028
* Dates:achatMaison: le 01 du 06-2020
* Dates:achatVoiture: le 15 du 03-2022
* Dates:echeanceDette: le 01 du 06-2026

# Trésoreries
* Trésoreries:compteRakoto
* Trésoreries:compteNirina
* Trésoreries:compteSociete

# Créances
* Créances:creanceLocataire
* Créances:creanceFournisseur

# Dettes
* Dettes:detteHypothecaire
* Dettes:detteVoiture

# Initialisation
* `objectifEpargne` Dates:ajd, objectif de 50000000Ar pour Trésoreries:compteRakoto
* `seedCompteRakoto` Dates:ajd, entrer 5000000Ar vers Trésoreries:compteRakoto
* `seedCompteNirina` Dates:ajd, entrer 2000000Ar vers Trésoreries:compteNirina
* `seedCompteSociete` Dates:ajd, entrer 10000000Ar vers Trésoreries:compteSociete

# Opérations
## Patrimoine, Dates:ajd, devise en Ar
* `maisonFamiliale` Dates:ajd, posséder MaisonAmbohimanarina, valant 80000000Ar obtenu Dates:achatMaison se dépréciant annuellement de Nombres:tauxAppreciationMaison%
* `voitureToyota` Dates:ajd, posséder VoitureToyotaCorolla, valant 25000000Ar obtenu Dates:achatVoiture se dépréciant annuellement de Nombres:tauxDepreciationVoiture%

## Acquisitions, Dates:ajd, devise en Ar
* `achatOrdinateur` Dates:ajd, acheter OrdinateurMacBook, valant 4500000Ar, depuis Trésoreries:compteRakoto se dépréciant annuellement de 20%
* `achatMobilier` le 01 du 03-2025, acheter MobilierBureau, valant 1200000Ar, depuis Trésoreries:compteSociete se dépréciant annuellement de 15%

## Revenus, Dates:ajd, devise en Ar
* `salaireMensuel` Dates:ajd, entrer Nombres:salaire Ar vers Trésoreries:compteRakoto, jusqu'à date indéterminée tous les 27 du mois
* `loyerLocataire` Dates:ajd, entrer Nombres:loyer Ar vers Trésoreries:compteRakoto, jusqu'à date indéterminée tous les 5 du mois
* `dividendesSociete` le 01 du 07-2025, entrer 3000000Ar vers Trésoreries:compteRakoto, jusqu'à le 01 du 07-2027 tous les fin du mois

## Charges, Dates:ajd, devise en Ar
* `electricite` Dates:ajd, sortir 150000Ar depuis Trésoreries:compteRakoto, jusqu'à date indéterminée tous les 10 du mois
* `eauJIRAMA` Dates:ajd, sortir 45000Ar depuis Trésoreries:compteRakoto, jusqu'à date indéterminée tous les 10 du mois
* `assuranceMaison` Dates:ajd, sortir 80000Ar depuis Trésoreries:compteRakoto, jusqu'à date indéterminée tous les fin du mois
* `remboursementHypotheque` Dates:ajd, sortir 600000Ar depuis Trésoreries:compteRakoto, jusqu'à Dates:echeanceDette tous les 1 du mois

## Transferts, Dates:ajd, devise en Ar
* `apportSociete` le 01 du 02-2025, transférer 2000000Ar depuis Trésoreries:compteRakoto vers Trésoreries:compteSociete
* `remiseNirina` Dates:ajd, transférer 500000Ar depuis Trésoreries:compteRakoto vers Trésoreries:compteNirina, jusqu'à le 01 du 01-2026 tous les fin du mois

## ValeursMarché, Dates:ajd, devise en Ar
* `reevaluationMaison` le 01 du 01-2026,  Matériel:MaisonAmbohimanarina valant 90000000Ar
* `reevaluationVoiture` le 01 du 01-2026,  Matériel:VoitureToyotaCorolla valant 18000000Ar
* `reevaluationOrdinateur` le 01 du 01-2027, Matériel:OrdinateurMacBook valant 2000000Ar

## Cessions, Dates:ajd, devise en Ar
* `venteVoiture` le 15 du 06-2026, vendre Matériel:voitureToyotaCorolla pour 20000000Ar vers Trésoreries:compteRakoto
* `venteMobilier` le 01 du 12-2026, vendre Matériel:mobilierBureau pour 800000Ar vers Trésoreries:compteSociete

## Remboursements, Dates:ajd, devise en Ar
* `remboursementLocataire` le 01 du 04-2025, rembourser Dettes:detteHypothecaire de Personnes:Rakoto avec Créances:creanceLocataire de Personnes:Nirina valant 1500000Ar
* `apurementFournisseur` le 01 du 09-2025, rembourser Dettes:detteVoiture de Personnes:Rakoto avec Créances:creanceFournisseur de Personnes:Rakoto valant 900000Ar

# Constructeurs d'opérations
## depenseRecurrente(Trésoreries:compte, Argent:montant, Nombre:jour)
* `depRecurrente` Dates:ajd, sortir Argent:montant depuis Trésoreries:compte, jusqu'à date indéterminée tous les Nombre:jour du mois

# Suivi
* `objectifMiParcours` le 01 du 01-2026, objectif de 20000000Ar pour Trésoreries:compteRakoto
* `correctionSolde` le 01 du 06-2025, corriger 5500000Ar dans Trésoreries:compteRakoto
* `objectifFinal2027` le 01 du 01-2027, objectif de 40000000Ar pour Trésoreries:compteRakoto