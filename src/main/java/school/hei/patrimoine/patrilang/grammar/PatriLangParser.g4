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
    :   sectionToutCasGeneral sectionCas? sectionPersonnes? sectionDates? sectionTresoreries? sectionCreances? sectionDettes? EOF
    ;

ligneObjectifFinal
    :   PUCE MOT_OBJECTIF_FINAL variable
    //  PUCE MOT_OBJECTIF_FINAL variable[0]=date
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
    :   HASHES ENTETE_DATES ligneNomValeur*
    ;

/* Cas */
cas
    :   sectionCasGeneral sectionPossesseurs sectionTresoreries? sectionCreances? sectionDettes? sectionOperations? EOF
    ;

sectionCasGeneral
    :   HASHES ENTETE_GENERAL ligneDateSpecification ligneDateFinSimulation ligneCasNom ligneDevise
    ;

ligneCasNom
    :   PUCE MOT_CAS_DE variable
    ;

sectionPossesseurs
    :   HASHES ENTETE_POSSESSEURS lignePossesseur+
    ;

lignePossesseur
    :   PUCE variable variable PERCENT
    ;

ligneDateSpecification
    :   PUCE MOT_SPECIFIER variable
    ;

ligneDateFinSimulation
    :   PUCE MOT_FIN_SIMULATION variable
    ;

ligneDevise
    :   PUCE MOT_DEVISE_EN variable
    ;

/* -------------------- Possessions --------------------  */
/* Trésorerie */
sectionTresoreries
    :   HASHES ENTETE_TRESORERIES compte*
    ;

/* Créances */
sectionCreances
    :   HASHES ENTETE_CREANCES compte*
    ;

/* Dettes */
sectionDettes
    :   HASHES ENTETE_DETTES compte*
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
    ;

/* Simple Possessions */
compte
    :   PUCE variable COMMA MOT_VALANT variable variable
    //  PUCE variable[0]=nom COMMA MOT_VALANT variable[1]=valeurComptable variable[2]=date
    ;

fluxArgentTransferer
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_TRANSFERER variable MOT_DEPUIS variable MOT_VERS variable dateFin?
    //  PUCE BACKTICK variable[0]=id BACKTICK variable[1]=date COMMA MOT_TRANSFERER variable[2]=valeurComtable MOT_DEPUIS variable[3]=debiteur MOT_VERS variable[4]=crediteur value?
    ;

fluxArgentEntrer
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_ENTRER variable MOT_VERS variable dateFin?
    //  PUCE BACKTICK variable[0]=id BACKTICK variable[1]=date COMMA MOT_ENTRER variable[2]=valeurComptable MOT_VERS variable[3]=compte value?
    ;

fluxArgentSortir
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_SORTIR variable MOT_DEPUIS variable dateFin?
    //  PUCE BACKTICK variable[0]=id BACKTICK variable[1]=date COMMA MOT_SORTIR variable[2]=valeurComptable MOT_DEPUIS variable[3]=compte value?
    ;

acheterMateriel
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_ACHETER variable COMMA MOT_VALANT variable COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE variable PERCENT COMMA MOT_DEPUIS variable
    //  PUCE BACKTICK variable[0]=id BACKTICK variable[1]=date COMMA MOT_ACHETER variable[2]=materielNom COMMA MOT_VALANT variable[3]=valeurComptable COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE variable[4]=valeurAppreciation PERCENT COMMA MOT_DEPUIS variable[5]=compte
    ;

possedeMateriel
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_POSSEDER variable COMMA MOT_VALANT variable COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE variable PERCENT
    //  PUCE BACKTICK variable[0]=id BACKTICK variable[1]=date COMMA MOT_POSSEDER variable[2]=nom COMMA MOT_VALANT variable[3]=valeurComptable COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE variable[4]=tauxAppreciation PERCENT
    ;

/* -------------------- Commun --------------------  */
sousTitre
    :   HASHES HASHES variable COMMA variable COMMA MOT_DEVISE_EN variable
    //  HASHES HASHES variable[0]=nom COMMA variable[1]=date COMMA MOT_DEVISE_EN variable[2]=devise
    ;

dateFin
    :   COMMA MOT_JUSQUA variable MOT_TOUT_LES ENTIER MOT_DU MOT_MOIS
    //  COMMA MOT_JUSQUA variable[0]=date MOT_TOUT_LES ENTIER MOT_DU MOT_MOIS
    ;

ligneNomValeur
    :   PUCE variable COLON variable
    ;

ligneNom
    :   PUCE variable
    ;

/*  Valeur englobé par variable  */
variable
    :   devise
    |   argent
    |   date
    |   nombre
    |   text
    |   variableValue
    ;

variableValue
    :   BACKTICK VARIABLE BACKTICK
    ;

argent
    :   nombre devise
    ;

devise
    :   DEVISE
    ;

nombre
    :   DECIMAL
    |   ENTIER
    ;

date
    :   MOT_LE ENTIER MOT_DU ENTIER TIRER ENTIER
    |   MOT_DATE_INDETERMINER
    ;

text
    :   TEXT
    ;