parser grammar PatriLangParser;

@header {
package school.hei.patrimoine.patrilang.antlr;
}

options { tokenVocab=PatriLangLexer; }

document
  : sectionGeneral EOF
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

// Commun
date
    : MOT_LE NOMBRE MOT_DU NOMBRE TIRER NOMBRE
    ;