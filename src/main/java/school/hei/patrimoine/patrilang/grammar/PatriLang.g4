grammar PatriLang;

@header{
package school.hei.patrimoine.patrilang.antlr;
}

patrimoine  : SEPARATEUR date nom mainDevise comptes possessions SEPARATEUR;
date        : NUMBER 'du' NUMBER '-' NUMBER;
nom         : 'Nom' ':' MOT TERMINATEUR;
mainDevise  : 'Devise' ':' DEVISE TERMINATEUR;
comptes     : 'Mes comptes' ':' compte+;
compte      : MOT ':' date 'contient' argent TERMINATEUR;
argent      : NUMBER DEVISE;
possessions : possession+;
possession  : materiel
            | fluxArgent;
materiel    : date ',' 'je possède' MOT ',' 'valant' argent ',' appreciation TERMINATEUR;
fluxArgent  : fluxEntrer
            | fluxSortir;
fluxSortir  : date ',' 'sortir' argent 'depuis' MOT TERMINATEUR;
fluxEntrer  : date ',' 'entrer' argent 'vers' MOT TERMINATEUR;
appreciation: APPRECIATION_TYPE 'annuellement de' NUMBER '%' ;

APPRECIATION_TYPE
            : 'se dépréciant'
            | 's\'appréciant';
TERMINATEUR : '.' ;
NUMBER      : [0-9]+ ;
SEPARATEUR  : '====' ;
DEVISE      : 'Ar' | '€' ;
MOT         : [a-zA-ZÀ-ÿ_-]+ ;
ESPACE      : [ \t\r\n]+ -> skip ;