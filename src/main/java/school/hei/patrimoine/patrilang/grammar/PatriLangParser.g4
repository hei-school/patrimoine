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
  : ENTETE_GENERAL
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
    : ENTETE_TRESORERIES compte*
    ;

/* Créances */
sectionCreances
    : ENTETE_CREANCES compte*
    ;

/* Dettes */
sectionDettes
    : ENTETE_DETTES compte*
    ;

/* Opérations */
sectionOperations
    : ENTETE_OPERATIONS operation*
    ;

operation
    : possedeMateriel
    | acheterMateriel
    | fluxArgentEntrer
    | fluxArgentSortir
    | fluxArgentTransferer
    ;

/* Possessions */
compte
    : PUCE TEXT COMMA MOT_VALANT argent date
    ;
possedeMateriel
    : ID date COMMA MOT_JE? MOT_POSSEDE TEXT MOT_VALANT argent COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE ENTIER PERCENT
    ;
acheterMateriel
    : ID date COMMA MOT_JE? MOT_ACHETER TEXT MOT_VALANT argent MOT_DEPUIS TEXT COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE ENTIER PERCENT
    ;
fluxArgentEntrer
    : ID date COMMA MOT_JE? MOT_ENTRER argent MOT_VERS TEXT
    ;
fluxArgentSortir
    : ID date COMMA MOT_JE? MOT_SORTIR argent MOT_DEPUIS TEXT
    ;
fluxArgentTransferer
    : ID date COMMA MOT_JE? MOT_TRANSFERER argent MOT_DEPUIS TEXT MOT_VERS TEXT
    ;

/* Commun */
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