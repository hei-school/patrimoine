parser grammar PatriLangParser;

@header {
package school.hei.patrimoine.patrilang.antlr;
}

options { tokenVocab=PatriLangLexer; }

/* -------------------- Base --------------------  */
document
    :   cas
    |   toutCas
    ;

/* ToutCas */
toutCas
    :   sectionToutCasGeneral sectionCas? sectionDates? sectionPersonnes? sectionTresoreries? sectionCreances? sectionDettes? EOF
    ;

ligneObjectifFinal
    :   PUCE MOT_OBJECTIF_FINAL valeurComptable=argent
    ;

sectionToutCasGeneral
    :   HASHES ENTETE_GENERAL ligneObjectifFinal
    ;

sectionCas
    :   HASHES ENTETE_CAS ligneNom*
    ;

sectionPersonnes
    :   HASHES ENTETE_PERSONNES ligneNom*
    ;

sectionDates
    :   HASHES ENTETE_DATES ligneDate*
    ;

ligneDate
    :   PUCE nom=variable COLON dateValue=variable
    ;

/* Cas */
cas
    :   sectionCasGeneral sectionPossesseurs sectionTresoreries? sectionCreances? sectionDettes? sectionInitialisation? sectionOperations? sectionSuivi? EOF
    ;

sectionCasGeneral
    :   HASHES ENTETE_GENERAL ligneDateSpecification ligneDateFinSimulation ligneCasNom ligneDevise
    ;

ligneCasNom
    :   PUCE MOT_CAS_DE nom=variable
    ;

sectionPossesseurs
    :   HASHES ENTETE_POSSESSEURS lignePossesseur+
    ;

lignePossesseur
    :   PUCE nom=variable pourcentage=nombre PERCENT
    ;

ligneDateSpecification
    :   PUCE MOT_SPECIFIER dateValue=variable
    ;

ligneDateFinSimulation
    :   PUCE MOT_FIN_SIMULATION dateValue=variable
    ;

ligneDevise
    :   PUCE MOT_DEVISE_EN devise
    ;

sectionInitialisation
    :   HASHES ENTETE_INITIALISATION operations*
    ;

sectionSuivi
    :   HASHES ENTETE_SUIVI operations*
    ;

objectif
    :   PUCE BACKTICK id=variable BACKTICK dateValue=variable COMMA MOT_OBJECTIF_DE valeurComptable=argent MOT_POUR compteNom=variable
    ;

correction
    :   PUCE BACKTICK id=variable BACKTICK dateValue=variable COMMA MOT_CORRIGER valeurComptable=argent MOT_DANS compteNom=variable
    ;
/* -------------------- Possessions --------------------  */
/* Trésorerie */
sectionTresoreries
    :   HASHES ENTETE_TRESORERIES compteElement*
    ;

/* Créances */
sectionCreances
    :   HASHES ENTETE_CREANCES compteElement*
    ;

/* Dettes */
sectionDettes
    :   HASHES ENTETE_DETTES compteElement*
    ;

compteElement
    :   compte
    |   PUCE variable
    ;
/* Opérations */
sectionOperations
    : HASHES ENTETE_OPERATIONS operations*
    ;

operations
    :   sousTitre? operation+
    ;

operation
    :   fluxArgentTransferer
    |   fluxArgentEntrer
    |   fluxArgentSortir
    |   possedeMateriel
    |   acheterMateriel
    |   correction
    |   objectif
    ;

/* Simple Possessions */
compte
    :   PUCE nom=variable COMMA MOT_VALANT valeurComptable=argent dateValue=variable
    ;

fluxArgentTransferer
    :   PUCE BACKTICK id=variable BACKTICK dateValue=variable COMMA MOT_TRANSFERER valeurComptable=argent MOT_DEPUIS compteDebiteurNom=variable MOT_VERS compteCrediteurNom=variable dateFin?
    ;

fluxArgentEntrer
    :   PUCE BACKTICK id=variable BACKTICK dateValue=variable COMMA MOT_ENTRER valeurComptable=argent MOT_VERS compteCrediteurNom=variable dateFin?
    ;

fluxArgentSortir
    :   PUCE BACKTICK id=variable BACKTICK dateValue=variable COMMA MOT_SORTIR valeurComptable=argent MOT_DEPUIS compteDebiteurNom=variable dateFin?
    ;

acheterMateriel
    :   PUCE BACKTICK id=variable BACKTICK dateValue=variable COMMA MOT_ACHETER materielNom=variable COMMA MOT_VALANT valeurComptable=argent COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE pourcentageAppreciation=nombre PERCENT COMMA MOT_DEPUIS compteDebiteurNom=variable
    ;

possedeMateriel
    :   PUCE BACKTICK id=variable BACKTICK dateValue=variable COMMA MOT_POSSEDER materielNom=variable COMMA MOT_VALANT valeurComptable=argent COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE pourcentageAppreciation=nombre PERCENT
    ;

/* -------------------- Commun --------------------  */
sousTitre
    :   HASHES HASHES nom=variable COMMA dateValue=variable COMMA MOT_DEVISE_EN devise
    ;

dateFin
    :   COMMA MOT_JUSQUA dateValue=variable MOT_TOUT_LES ENTIER MOT_DU MOT_MOIS
    ;

ligneNom
    :   PUCE nom=variable
    ;

argent
    :   TIRER? nombre devise
    ;

devise
    :   DEVISE
    ;

nombre
    :   DECIMAL
    |   ENTIER
    ;

/*  Valeur englobé par variable  */
variable
    :   date
    |   text
    |   variableValue
    ;

variableValue
    :   BACKTICK VARIABLE BACKTICK
    ;

date
    :   MOT_LE jour=ENTIER MOT_DU mois=ENTIER TIRER annee=ENTIER
    |   MOT_DATE_INDETERMINER
    ;

text
    :   TEXT
    ;