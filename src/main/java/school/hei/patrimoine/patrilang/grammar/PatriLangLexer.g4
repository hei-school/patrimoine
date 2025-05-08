lexer grammar PatriLangLexer;

@header {
package school.hei.patrimoine.patrilang.antlr;
}

fragment DIGIT
    :   [0-9]
    ;

/* -------------------- Base --------------------  */
ENTETE_GENERAL
    :   'Général'
    ;
MOT_SPECIFIER
    :   'Spécifié'
    ;

/* Patrimoine */
MOT_PATRIMOINE_DE
    :   'Patrimoine de'
    ;

/* Cas */
MOT_CAS_DE
    :   'Cas de'
    |   'cas de'
    ;
MOT_FIN_SIMULATION
    :   'Fin de simulation'
    |   'fin de simulation'
    ;

/* --------------------  Possessions --------------------  */
/* Trésorerie */
ENTETE_TRESORERIES
    :   'Trésoreries'
    ;
MOT_CONTIENT
    :   'contient'
    ;

/* Créance  */
ENTETE_CREANCES    : 'Créances';

/* Dettes */
ENTETE_DETTES      : 'Dettes';

/* Opérations */
ENTETE_OPERATIONS  : 'Opérations';
MOT_POSSEDER       : 'posséder';
MOT_ACHETER        : 'acheter';
MOT_SORTIR         : 'sortir';
MOT_ENTRER         : 'entrer';
MOT_TRANSFERER     : 'transférer';
MOT_DEPUIS         : 'depuis';
MOT_VERS           : 'vers';
MATERIEL_APPRECIATION
                   : 's\'appréciant'
                   | 'se dépréciant'
                   ;
MOT_ANNUELLEMENT_DE
                   : 'annuellement de'
                   ;
PERCENT            : '%';

/* -------------------- Commun --------------------  */
/* Date */
MOT_LE             : 'Le'
                   | 'le'
                   ;
MOT_DU             : 'du';
TIRER              : '-';

/* Autres */
BACKTICK           : '`';
MOT_DATE_INDETERMINER
                   : 'date indéterminée'
                   | 'date indéterminer'
                   | 'Date indéterminée'
                   | 'Date indéterminer'
                   ;
MOT_JUSQUA         : 'Jusqu\'à'
                   | 'jusqu\'à'
                   | 'jusqu\'a'
                   | 'Jusqu\'a'
                   ;
MOT_TOUT_LES       : 'Tous les'
                   | 'tous les'
                   ;
MOT_MOIS           : 'mois'
                   | 'Mois'
                   ;
MOT_DEVISE_EN      : 'Devise en'
                   | 'devise en'
                   ;
MOT_VALANT         : 'valant';
DEVISE             : 'Ar'
                   | '€'
                   ;
PUCE               : '*'
                   | '-'
                   ;
HASHES             : '#';
COMMA              : ',';
DECIMAL            : DIGIT+ '.' DIGIT+;
ENTIER             : DIGIT+;
TEXT               : ([\p{L}_]) ([\p{L}\p{N}_])* ;

/* Skipped */
WS                 : [ \t]+                              -> skip;
NEWLINE            : [\r\n]+                             -> skip;