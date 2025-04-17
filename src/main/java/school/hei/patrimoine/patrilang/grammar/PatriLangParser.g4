parser grammar PatriLangParser;

@header {
package school.hei.patrimoine.patrilang.antlr;
}

options { tokenVocab=PatriLangLexer; }

document
  : sectionGeneral
    sectionTresorerie?
    sectionCreance?
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
sectionTresorerie
    : ENTETE_TRESORERIE compte*
    ;

compte
    : PUCE TEXT MOT_CONTIENT argent date
    ;

/* Créances */
sectionCreance
    : ENTETE_CREANCE creance*;

creance
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