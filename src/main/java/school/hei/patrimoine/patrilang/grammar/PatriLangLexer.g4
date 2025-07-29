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

ENTETE_INITIALISATION
    :   'Initialisation'
    ;

ENTETE_SUIVI
    :   'Suivi'
    ;

MOT_CORRIGER
    :   'Corriger'
    |   'corriger'
    ;

MOT_DANS
    :   'dans'
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

ENTETE_NOMBRES
    :   'Nombres'
    ;

ENTETE_PERSONNES
    :   'Personnes'
    ;

ENTETE_PERSONNES_MORALES
    :   'Personnes Morales'
    |   'Personnes morales'
    ;

MOT_POUR
    :   'pour'
    ;

MOT_OBJECTIF_DE
    :   'objectif de'
    |   'Objectif de'
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
ENTETE_CONTSTRUCTEUR_D_OPERATIONS
    :   'Constructeurs'
    |   'Constructeurs d\'opérations'
    |   'Constructeurs d\'Opérations'
    ;
ENTETE_OPERATIONS
    :   'Opérations'
    ;
MOT_REMBOURSER
    :   'Rembourser'
    |   'rembourser'
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
MOT_VALEUR_MARCHE
    :   'valeur marché'
    | 'Valeur marché'
    | 'valeur marché de'
    | 'Valeur marché de'
    ;
MOT_ESTIMEE
    : 'estimée'
    | 'estimé'
    ;
MOT_A
    :   'à'
    |   'À'
    ;
MOT_EVALUATION
    :   'évaluation'
    ;
MOT_VENDRE
    :   'vendre'
    |   'Vendre'
    ;
MOT_VENDU
    :   'vendu'
    |   'Vendu'
    ;
MOT_PRIX
    :   'prix'
    |   'Prix'
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

MOIS
    : 'janvier'
    | 'Janvier'
    | 'février'
    | 'Février'
    | 'fevrier'
    | 'Fevrier'
    | 'mars'
    | 'Mars'
    | 'avril'
    | 'Avril'
    | 'mai'
    | 'Mai'
    | 'juin'
    | 'Juin'
    | 'juillet'
    | 'Juillet'
    | 'août'
    | 'Août'
    | 'aout'
    | 'Aout'
    | 'septembre'
    | 'Septembre'
    | 'octobre'
    | 'Octobre'
    | 'novembre'
    | 'Novembre'
    | 'décembre'
    | 'Décembre'
    | 'decembre'
    | 'Decembre'
    ;

MOT_DATE_INDETERMINER
    :   'date indéterminée'
    |   'date indéterminer'
    |   'Date indéterminée'
    |   'Date indéterminer'
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
    :   'Jusqu\'à'
    |   'jusqu\'à'
    |   'jusqu\'a'
    |   'Jusqu\'a'
    ;
MOT_TOUT_LES
    :   'Tous les'
    |   'tous les'
    ;
/* Mots */
MOT_DEVISE_EN
    :   'Devise en'
    |   'devise en'
    ;
MOT_VALANT
    :   'valant'
    ;

/* Opérateurs */
MOT_EVALUER
    :  'évalué'
    ;
UNITE_DATE_DE
    : 'années de'
    | 'année de'
    | 'Année de'
    | 'Années de'
    | 'mois de'
    | 'Mois de'
    | 'jour de'
    | 'Jour de'
    | 'jours de'
    | 'Jours de'
    ;

DUREE_UNITE
    :   'en jours'
    |   'en Jours'
    |   'en mois'
    |   'en Mois'
    |   'en années'
    |   'en Années'
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
