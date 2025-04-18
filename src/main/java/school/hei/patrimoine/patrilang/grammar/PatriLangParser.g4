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
    EOF
  ;

/* Séction */
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

/* Possessions */
compte
    : PUCE TEXT COMMA MOT_VALANT argent date
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