parser grammar PatriLangParser;

@header {
package school.hei.patrimoine.patrilang.antlr;
}

options { tokenVocab=PatriLangLexer; }

document
  : sectionGeneral
    sectionTresoreries?
    sectionCreances?
    sectionDettes?
    sectionOperations?
    EOF
  ;

/* Général */
sectionGeneral
  : HASHES ENTETE_GENERAL
    lignePatrimoineDate
    lignePatrimoineNom
    lignePatrimoineDevise
  ;

lignePatrimoineDate
    : PUCE MOT_SPECIFIER date
    ;

lignePatrimoineNom
    : PUCE MOT_PATRIMOINE_DE TEXT
    ;

lignePatrimoineDevise
    : PUCE MOT_DEVISE_EN DEVISE
    ;

/* Trésorerie */
sectionTresoreries
    : HASHES ENTETE_TRESORERIES compte*
    ;

/* Créances */
sectionCreances
    : HASHES ENTETE_CREANCES compte*
    ;

/* Dettes */
sectionDettes
    : HASHES ENTETE_DETTES compte*
    ;

/* Opérations */
sectionOperations
    : HASHES ENTETE_OPERATIONS operations*
    ;

operations
    : sousTitre? operation+
    ;

operation
    : fluxArgentTransferer
    | fluxArgentEntrer
    | fluxArgentSortir
    | possedeMateriel
    | acheterMateriel
    ;

/* Possessions */
compte
    : PUCE TEXT COMMA MOT_VALANT argent date
    ;
fluxArgentTransferer
    : PUCE BACKTICK TEXT BACKTICK date COMMA MOT_JE? MOT_TRANSFERER argent MOT_DEPUIS TEXT MOT_VERS TEXT
    ;
fluxArgentEntrer
    : PUCE BACKTICK TEXT BACKTICK date COMMA MOT_JE? MOT_ENTRER argent MOT_VERS TEXT
    ;
fluxArgentSortir
    : PUCE BACKTICK TEXT BACKTICK date COMMA MOT_JE? MOT_SORTIR argent MOT_DEPUIS TEXT
    ;
acheterMateriel
    : PUCE BACKTICK TEXT BACKTICK date COMMA MOT_JE? MOT_ACHETER TEXT COMMA MOT_VALANT argent COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE ENTIER PERCENT COMMA MOT_DEPUIS TEXT
    ;
possedeMateriel
    : PUCE BACKTICK TEXT BACKTICK date COMMA MOT_JE? MOT_POSSEDER TEXT COMMA MOT_VALANT argent COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE ENTIER PERCENT
    ;

/* Commun */
sousTitre
    : HASHES HASHES TEXT COMMA date COMMA MOT_DEVISE_EN DEVISE
    ;

argent
    : nombre DEVISE
    ;

date
    : MOT_LE ENTIER MOT_DU ENTIER TIRER ENTIER
    ;
nombre
    : DECIMAL
    | ENTIER
    ;