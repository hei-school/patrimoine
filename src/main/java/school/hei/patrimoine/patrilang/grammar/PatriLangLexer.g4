lexer grammar PatriLangLexer;

@header {
package school.hei.patrimoine.patrilang.antlr;
}

// Typography tokens
DU                : 'du';
TIRET             : '-';
NOM               : 'Nom';
DEVISE_MOT        : 'Devise';
MES_COMPTES       : 'Mes comptes';
DEUX_POINTS       : ':' ;
VIRGULE           : ',' ;
JE_POSSEDE        : 'je possède';
VALANT            : 'valant';
CONTIENT          : 'contient';
SORTIR            : 'sortir';
DEPUIS            : 'depuis';
ENTRER            : 'entrer';
VERS              : 'vers';
ANNUELLEMENT_DE   : 'annuellement de';
POURCENT          : '%';

// Symbol tokens
APPRECIATION_TYPE : 'se dépréciant' | 's\'appréciant';
TERMINATEUR       : '.' ;
NUMBER            : [0-9]+ ;
SEPARATEUR        : '====' ;
DEVISE            : 'Ar' | '€' ;
MOT               : [a-zA-ZÀ-ÿ_-]+ ;

ESPACE            : [ \t\r\n]+ -> skip ;
