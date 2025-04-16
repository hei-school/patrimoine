parser grammar PatriLangParser;

@header {
package school.hei.patrimoine.patrilang.antlr;
}

options { tokenVocab=PatriLangLexer; }

patrimoine  : SEPARATEUR date nom mainDevise comptes possessions SEPARATEUR;
date        : NUMBER DU NUMBER TIRET NUMBER;
nom         : NOM DEUX_POINTS MOT TERMINATEUR;
mainDevise  : DEVISE_MOT DEUX_POINTS DEVISE TERMINATEUR;
comptes     : MES_COMPTES DEUX_POINTS compte+;
compte      : MOT DEUX_POINTS date CONTIENT argent TERMINATEUR;
argent      : NUMBER DEVISE;
possessions : possession+;
possession  : materiel
            | fluxArgent;
materiel    : date VIRGULE JE_POSSEDE MOT VIRGULE VALANT argent VIRGULE appreciation TERMINATEUR;
fluxArgent  : fluxEntrer
            | fluxSortir;
fluxSortir  : date VIRGULE SORTIR argent DEPUIS MOT TERMINATEUR;
fluxEntrer  : date VIRGULE ENTRER argent VERS MOT TERMINATEUR;
appreciation: APPRECIATION_TYPE ANNUELLEMENT_DE NUMBER POURCENT ;