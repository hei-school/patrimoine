parser grammar PatriLangParser;

@header {
package school.hei.patrimoine.patrilang.antlr;
}

options { tokenVocab=PatriLangLexer; }

/* -------------------- Base --------------------  */

document
    :   patrimoine
    |   cas
    ;

/* Patri */
patrimoine
    :   sectionPatrimoineGeneral
        sectionTresoreries?
        sectionCreances?
        sectionDettes?
        sectionOperations?
        EOF
    ;

sectionPatrimoineGeneral
    :   HASHES ENTETE_GENERAL
        lignePatrimoineDate
        lignePatrimoineNom
        ligneDevise
    ;

lignePatrimoineDate
    :   PUCE MOT_SPECIFIER variable
    ;

lignePatrimoineNom
    :   PUCE MOT_PATRIMOINE_DE variable
    ;

/* Cas */
cas
    :   sectionCasGeneral
        sectionTresoreries?
        sectionCreances?
        sectionDettes?
        sectionOperations?
        EOF
    ;

sectionCasGeneral
    :   ligneDateSpecification
        ligneDateFinSimulation
        ligneDevise
        ligneCasNom
    ;

ligneCasNom
    :   PUCE MOT_CAS_DE variable
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
    ;

fluxArgentTransferer
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_TRANSFERER variable MOT_DEPUIS variable MOT_VERS variable dateFin?
    ;

fluxArgentEntrer
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_ENTRER variable MOT_VERS variable dateFin?
    ;

fluxArgentSortir
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_SORTIR variable MOT_DEPUIS variable dateFin?
    ;

acheterMateriel
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_ACHETER variable COMMA MOT_VALANT variable COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE variable PERCENT COMMA MOT_DEPUIS variable
    ;

possedeMateriel
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_POSSEDER variable COMMA MOT_VALANT variable COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE variable PERCENT
    ;

/* -------------------- Commun --------------------  */
ligneDateSpecification
    :   PUCE MOT_SPECIFIER variable
    ;

ligneDateFinSimulation
    :   PUCE MOT_FIN_SIMULATION variable
    ;

ligneDevise
    :   PUCE MOT_DEVISE_EN variable
    ;

sousTitre
    :   HASHES HASHES variable COMMA variable COMMA MOT_DEVISE_EN variable
    ;

dateFin
    :   COMMA MOT_JUSQUA variable MOT_TOUT_LES ENTIER MOT_DU MOT_MOIS
    ;

/*  Valeur englobé par variable  */
variable
    :   devise
    |   argent
    |   dateFinValue
    |   date
    |   nombre
    |   text
    |   BACKTICK VARIABLE BACKTICK
    ;

argent
    :   nombre devise
    ;

devise
    :   DEVISE
    ;

dateFinValue
    :   MOT_DATE_INDETERMINER
    |   date
    ;

nombre
    :   DECIMAL
    |   ENTIER
    ;

date
    :   MOT_LE ENTIER MOT_DU ENTIER TIRER ENTIER
    ;

text
    :   TEXT
    ;