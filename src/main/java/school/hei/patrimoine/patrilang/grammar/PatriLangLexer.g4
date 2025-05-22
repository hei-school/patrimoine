lexer grammar PatriLangLexer;

@header {
package school.hei.patrimoine.patrilang.antlr;
}

fragment DIGIT
    :   [0-9]
    ;

fragment STRING
    :   ([\p{L}_]) ([\p{L}\p{N}_])*
    ;

/* -------------------- Base --------------------  */
/* Cas */
ENTETE_GENERAL
    :   'Général'
    ;

MOT_SPECIFIER
    :   'Spécifié'
    |   'Spécifier'
    ;

MOT_CAS_DE
    :   'Cas de'
    |   'cas de'
    ;
MOT_FIN_SIMULATION
    :   'Fin de simulation'
    |   'fin de simulation'
    ;
ENTETE_POSSESSEURS
    :   'Possesseurs'
    ;

/* ToutCas */
MOT_OBJECTIF_FINAL
    :   'Objectif final'
    |   'Objectif Final'
    ;

ENTETE_CAS
    :   'Cas'
    ;

ENTETE_DATES
    :   'Dates'
    ;

ENTETE_PERSONNES
    :   'Personnes'
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
ENTETE_CREANCES
    :   'Créances'
    ;

/* Dettes */
ENTETE_DETTES
    :   'Dettes'
    ;

/* Opérations */
ENTETE_OPERATIONS
    :   'Opérations'
    ;
MOT_POSSEDER
    :   'posséder'
    ;
MOT_ACHETER
    :   'acheter'
    ;
MOT_SORTIR
    :   'sortir'
    ;
MOT_ENTRER
    :   'entrer'
    ;
MOT_TRANSFERER
    :   'transférer'
    ;
MOT_DEPUIS
    :   'depuis'
    ;
MOT_VERS
    :   'vers'
    ;
MATERIEL_APPRECIATION
    :   's\'appréciant'
    |   'se dépréciant'
    ;
MOT_ANNUELLEMENT_DE
    :   'annuellement de'
    ;
PERCENT
    :   '%'
    ;

/* -------------------- Commun --------------------  */
/* Date */
MOT_LE
    :   'Le'
    |   'le'
    ;
MOT_DU
    :   'du'
    ;
TIRER
    :   '-'
    ;

/* Mots */
MOT_DATE_INDETERMINER
    :   'date indéterminée'
    |   'date indéterminer'
    |   'Date indéterminée'
    |   'Date indéterminer'
    ;
MOT_JUSQUA
    :   'Jusqu\'à'
    |   'jusqu\'à'
    |   'jusqu\'a'
    |   'Jusqu\'a'
    ;
MOT_TOUT_LES
    :   'Tous les'
    |   'tous les'
    ;
MOT_MOIS
    :   'mois'
    |   'Mois'
    ;
MOT_DEVISE_EN
    :   'Devise en'
    |   'devise en'
    ;
MOT_VALANT
    :   'valant'
    ;

/* Opérateurs */
DEVISE
    :   'Ar'
    |   '€'
    ;
PUCE
    :   '*'
    ;
HASHES
    :   '#'
    ;
COMMA
    :   ','
    ;
BACKTICK
    :   '`'
    ;

COLON
    :   ':'
    ;

/* Valeurs */
VARIABLE
    :   [\p{L}]+ COLON STRING
    ;
DECIMAL
    :   DIGIT+ '.' DIGIT+
    ;
ENTIER
    :   DIGIT+
    ;
TEXT
    :   STRING
    ;

/* Ignorés */
WS
    : [ \t]+    -> skip
    ;
NEWLINE
    : [\r\n]+   -> skip
    ;