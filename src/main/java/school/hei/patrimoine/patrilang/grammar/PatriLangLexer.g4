lexer grammar PatriLangLexer;

@header {
package school.hei.patrimoine.patrilang.antlr;
}

fragment DIGIT
    :   [0-9]
    ;

fragment STRING_RAW_VALUE
    :   '[' | ']' | '>' | '<' | '&'
    ;

fragment STRING
    :   ([\p{L}] | STRING_RAW_VALUE) (([\p{L}\p{N}_]) | STRING_RAW_VALUE)*
    ;

URL_START : '"' -> pushMode(URL);

/* -------------------- Base --------------------  */
/* Cas */
ENTETE_GENERAL
    :   'Général'
    ;

MOT_SPECIFIER
    :   'Spécifi'[é|er]
    ;

MOT_CAS_DE
    :   [c|C]'as de'
    ;
MOT_FIN_SIMULATION
    :   [f|F]'in de simulation'
    ;
ENTETE_POSSESSEURS
    :   'Possesseurs'
    ;

ENTETE_INITIALISATION
    :   'Initialisation'
    ;

ENTETE_SUIVI
    :   'Suivi'
    ;

MOT_CORRIGER
    :   [c|C]'orriger'
    ;

MOT_DANS
    :   'dans'
    ;
/* ToutCas */
MOT_OBJECTIF_FINAL
    :   [o|O]'Objectif '[f|F]'inal'
    ;

ENTETE_CAS
    :   'Cas'
    ;

ENTETE_DATES
    :   'Dates'
    ;

ENTETE_NOMBRES
    :   'Nombres'
    ;

ENTETE_PERSONNES
    :   'Personnes'
    ;

ENTETE_PERSONNES_MORALES
    :   [j|J]'ersonnes '[m|M]'orales'
    ;

MOT_POUR
    :   'pour'
    ;

MOT_OBJECTIF_DE
    :   [o|O]'bjectif de'
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

/* Pièces justificative  */
ENTETE_PIECES_JUSTIFICATIVES
    :   'Pièces Justificatives'
    ;

LIEN_PIECE_JUSTIFICATIVE
    :   'Opérations lier'
    ;

/* Opérations */
ENTETE_CONTSTRUCTEUR_D_OPERATIONS
    :   'Constructeurs'
    |   'Constructeurs d\''[o|O]'pérations'
    ;
ENTETE_OPERATIONS
    :   'Opérations'
    ;
MOT_REMBOURSER
    :   [r|R]'Rembourser'
    ;
MOT_POSSEDER
    :   'posséder'
    ;
MOT_OBTENU
    :   'obtenu'
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
    :  [L|l]'e'
    ;
MOT_DU
    :   'du'
    ;

MOIS
    : [j|J]'anvier'
    | [f|F][e|é]'vrier'
    | [m|M]'ars'
    | [a|A]'vril'
    | [m|M]'ai'
    | [j|J]'uin'
    | [j|J]'uillet'
    | [a|A]'o'[û|u]'t'
    | [s|S]'eptembre'
    | [o|O]'ctobre'
    | [n|N]'ovembre'
    | [d|D][e|é]'cembre'
    ;

MOT_DATE_INDETERMINER
    :   [d|D]'date indétermin'[e|é][e|r]
    ;
MOT_DATE_MINIMUM
    :   'DATE_MIN'
    |   'DATE_MINIMUM'
    |   'LE_DEBUT_DU_TEMPS'
    |   'DEBUT_DU_TEMPS'
    ;
MOT_DATE_MAXIMUM
    :   'DATE_MAX'
    |   'DATE_MAXIMUM'
    |   'FIN_DES_TEMPS'
    |   'FIN_DU_TEMPS'
    ;
MOT_JUSQUA
    :   [j|J]'usqu\''[a|à]
    ;
MOT_TOUS_LES
     :   [t|T]'ous les'
     ;
MOT_FIN_DU_MOIS
    :   'fin du mois'
    ;
/* Mots */
MOT_DEVISE_EN
    :   [d|D]'evise en'
    ;
MOT_VALANT
    :   'valant'
    ;

/* Opérateurs */
MOT_EVALUER
    :  'évalué'
    ;
UNITE_DATE_DE
    : [a|A]'nnée'[|s]' de'
    | [m|M]'ois de'
    | [j|J]'our'[|s]' de'
    ;

DUREE_UNITE
    :   'en '[j|J]'ours'
    |   'en '[m|M]'ois'
    |   'en '[a|A]'nnées'
    ;
DEVISE
    :   'Ar'
    |   '€'
    |   '$'
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

LPAREN
    :   '('
    ;

RPAREN
    :   ')'
    ;

COLON
    :   ':'
    ;

/* Valeurs */
ARGENTS_VARIABLE
    :   'Argents' COLON STRING
    ;
NOMBRE_VARIABLE
    :   'Nombres' COLON STRING
    ;
DATE_VARIABLE
    :   'Dates' COLON STRING
    ;
VARIABLE
    :   [\p{L}]+ COLON STRING
    ;
DECIMAL
    :   DIGIT+ ('_' DIGIT+)* '.' DIGIT+
    ;
ENTIER
    :   DIGIT+ ('_' DIGIT+)*
    ;
POINT
    :   '.'
    ;
PLUS
    :   '+'
    ;
MUL
    :   '*'
    ;
DIV
    :   '/'
    ;
MOINS
    :   '-'
    ;
MOT_SEMAINE
    :   'semaine'
    ;
MOT_SEMAINES
    :   'semaines'
    ;
MOT_JOUR
    :   'jour'
    ;
MOT_JOURS
    :   'jours'
    ;
MOT_MOIS
    :   'mois'
    ;
MOT_ANNEE
    :   'année'
    ;
MOT_ANNEES
    :   'années'
    ;
MOT_ET
    :   'et'
    ;
MOT_AVEC
    :   'avec'
    ;
MOT_DE
    :   'de'
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

COMMENTAIRE
    :   BACKTICK DIV MUL .*? MUL DIV BACKTICK -> skip
    ;

//
mode URL;
URL_END: '"' -> popMode;
URL_CONTENT
    :   ~[^" \t\r\n]+
    ;