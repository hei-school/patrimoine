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

/* Opérations */
ENTETE_OPERATIONS  : '# Opérations';
MOT_JE             : 'Je'
                   | 'J\''
                   | 'J\'ai'
                   ;
MOT_POSSEDER       : 'possède'
                   | 'posseder'
                   ;
MOT_ACHETER        : 'acheter'
                   | 'achète'
                   ;
MOT_SORTIR         : 'sortir'
                   | 'sors'
                   ;
MOT_ENTRER         : 'entrer'
                   | 'entre'
                   ;
MOT_TRANSFERER     : 'transférer'
                   | 'transfère'
                   ;
MOT_DEPUIS         : 'depuis'
                   ;
MOT_VERS           : 'vers'
                   ;
ID_DEBUT           : '`'                                -> pushMode(ID_MODE)
                   ;
MATERIEL_APPRECIATION
                   : 's\'appréciant'
                   | 'dépréciant'
                   ;
MOT_ANNUELLEMENT_DE
                   : 'annuellement de'
                   ;

PERCENT            : '%'
                   ;
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

mode ID_MODE;
ID                 : ~[\r\n`]
                   ;
ID_FIN             : '`'                                 -> popMode
                   ;