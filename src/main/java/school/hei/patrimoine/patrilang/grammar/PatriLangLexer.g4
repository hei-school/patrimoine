lexer grammar PatriLangLexer;

@header {
package school.hei.patrimoine.patrilang.antlr;
}

fragment DIGIT: [0-9];

/* Général */
ENTETE_GENERAL     : '# Général';
MOT_SPECIFIER      : 'Spécifié';
MOT_DEVISE_EN      : 'Devise en';
MOT_PATRIMOINE_DE  : 'Patrimoine de';

/* Trésorerie */
ENTETE_TRESORERIES : '# Trésoreries';
MOT_CONTIENT       : 'contient';

/* Créance  */
ENTETE_CREANCES    : '# Créances';

/* Dettes */
ENTETE_DETTES      : '# Dettes';

/* Date */
MOT_LE             : 'Le'
                   | 'le'
                   ;
MOT_DU             : 'du';
TIRER              : '-';

/* Commun */
MOT_VALANT         : 'valant';
DEVISE             : 'Ar'
                   | '€'
                   ;
PUCE               : '*'
                   | '-'
                   ;
COMMA              : ',';
DECIMAL            : DIGIT+ '.' DIGIT+;
ENTIER             : DIGIT+;
TEXT               : [\p{L}_]+;

/* Skipped */
WS                 : [ \t]+                              -> skip;
NEWLINE            : [\r\n]+                             -> skip;