lexer grammar PatriLangLexer;

@header {
package school.hei.patrimoine.patrilang.antlr;
}

fragment DIGIT: [0-9];

// Général
ENTETE_GENERAL: '# Général';
MOT_SPECIFIER: 'Spécifié';
MOT_DEVISE_EN: 'Devise en';
MOT_PATRIMOINE_DE: 'Patrimoine de' -> pushMode(TEXT_MODE);

// Date
MOT_LE
     : 'Le'
     | 'le'
     ;
MOT_DU
     : 'du'
     ;
TIRER
     : '-'
     ;

// Commun
DEVISE
    : 'Ar'
    | '€'
    ;
PUCE
    : '*'
    | '-'
    ;
NOMBRE: DIGIT+ ('.' DIGIT+)?;

// Skipped
WS: [ \t]+ -> skip;
NEWLINE: [\r\n]+ -> skip;

mode    TEXT_MODE;
        TEXT: ~[\r\n,.]+;
        TEXT_NEW_LINE: [\n\r,.] -> popMode;