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
        EOF
    ;

sectionPatrimoineGeneral
    :   HASHES ENTETE_GENERAL
        lignePatrimoineDate
        lignePatrimoineNom
        ligneDevise
    ;

lignePatrimoineDate
    :   PUCE MOT_SPECIFIER date
    ;

lignePatrimoineNom
    :   PUCE MOT_PATRIMOINE_DE TEXT
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
    :   PUCE MOT_CAS_DE TEXT
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
    :   PUCE TEXT COMMA MOT_VALANT argent date
    ;
fluxArgentTransferer
    :   PUCE BACKTICK TEXT BACKTICK date COMMA MOT_TRANSFERER argent MOT_DEPUIS TEXT MOT_VERS TEXT dateFin?
    ;
fluxArgentEntrer
    :   PUCE BACKTICK TEXT BACKTICK date COMMA MOT_ENTRER argent MOT_VERS TEXT dateFin?
    ;
fluxArgentSortir
    :   PUCE BACKTICK TEXT BACKTICK date COMMA MOT_SORTIR argent MOT_DEPUIS TEXT dateFin?
    ;
acheterMateriel
    :   PUCE BACKTICK TEXT BACKTICK date COMMA MOT_ACHETER TEXT COMMA MOT_VALANT argent COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE ENTIER PERCENT COMMA MOT_DEPUIS TEXT
    ;
possedeMateriel
    :   PUCE BACKTICK TEXT BACKTICK date COMMA MOT_POSSEDER TEXT COMMA MOT_VALANT argent COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE ENTIER PERCENT
    ;

/* -------------------- Commun --------------------  */
ligneDateSpecification
    :   PUCE MOT_SPECIFIER date
    ;

ligneDateFinSimulation
    :   PUCE MOT_FIN_SIMULATION date
    ;

ligneDevise
    :   PUCE MOT_DEVISE_EN DEVISE
    ;

sousTitre
    :   HASHES HASHES TEXT COMMA date COMMA MOT_DEVISE_EN DEVISE
    ;

argent
    :   nombre DEVISE
    ;

nombre
    :   DECIMAL
    |   ENTIER
    ;

dateFin
    :   COMMA MOT_JUSQUA dateFinValue MOT_TOUT_LES ENTIER MOT_DU MOT_MOIS
    ;

dateFinValue
    :   MOT_DATE_INDETERMINER
    |   date;

date
    :   MOT_LE ENTIER MOT_DU ENTIER TIRER ENTIER
    ;
