parser grammar PatriLangParser;

@header {
package school.hei.patrimoine.patrilang.antlr;
}

options { tokenVocab=PatriLangLexer; }

/* -------------------- Base --------------------  */

document
    :   cas
    |   patrimoine
    ;

/* Patri */
patrimoine
    :   sectionPatrimoineGeneral
        sectionTresoreries?
        sectionCreances?
        sectionDettes?
        sectionOperations?
        EOF
    ;

sectionPatrimoineGeneral
    :   HASHES ENTETE_GENERAL
        lignePatrimoineDate
        lignePatrimoineNom
        ligneDevise
    ;

lignePatrimoineDate
    :   PUCE MOT_SPECIFIER variable
    ;

lignePatrimoineNom
    :   PUCE MOT_PATRIMOINE_DE variable
    ;

/* Cas */
cas
    :   HASHES ENTETE_GENERAL
        sectionCasGeneral
        sectionTresoreries?
        sectionCreances?
        sectionDettes?
        sectionOperations?
        EOF
    ;

sectionCasGeneral
    :   ligneDateSpecification
        ligneDateFinSimulation
        ligneCasNom
        ligneDevise
    ;

ligneCasNom
    :   PUCE MOT_CAS_DE variable
    ;

/* -------------------- Possessions --------------------  */
/* Trésorerie */
sectionTresoreries
    :   HASHES ENTETE_TRESORERIES compte*
    ;

/* Créances */
sectionCreances
    :   HASHES ENTETE_CREANCES compte*
    ;

/* Dettes */
sectionDettes
    :   HASHES ENTETE_DETTES compte*
    ;

/* Opérations */
sectionOperations
    : HASHES ENTETE_OPERATIONS operations*
    ;

operations
    :   sousTitre? operation+
    ;

operation
    :   fluxArgentTransferer
    |   fluxArgentEntrer
    |   fluxArgentSortir
    |   possedeMateriel
    |   acheterMateriel
    ;

/* Simple Possessions */
compte
    :   PUCE variable COMMA MOT_VALANT variable variable
    //  PUCE variable[0]=nom COMMA MOT_VALANT variable[1]=valeurComptable variable[2]=date
    ;

fluxArgentTransferer
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_TRANSFERER variable MOT_DEPUIS variable MOT_VERS variable dateFin?
    //  PUCE BACKTICK variable[0]=id BACKTICK variable[1]=date COMMA MOT_TRANSFERER variable[2]=valeurComtable MOT_DEPUIS variable[3]=debiteur MOT_VERS variable[4]=crediteur dateFin?
    ;

fluxArgentEntrer
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_ENTRER variable MOT_VERS variable dateFin?
    //  PUCE BACKTICK variable[0]=id BACKTICK variable[1]=date COMMA MOT_ENTRER variable[2]=valeurComptable MOT_VERS variable[3]=compte dateFin?
    ;

fluxArgentSortir
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_SORTIR variable MOT_DEPUIS variable dateFin?
    //  PUCE BACKTICK variable[0]=id BACKTICK variable[1]=date COMMA MOT_SORTIR variable[2]=valeurComptable MOT_DEPUIS variable[3]=compte dateFin?
    ;

acheterMateriel
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_ACHETER variable COMMA MOT_VALANT variable COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE variable PERCENT COMMA MOT_DEPUIS variable
    //  PUCE BACKTICK variable[0]=id BACKTICK variable[1]=date COMMA MOT_ACHETER variable[2]=materielNom COMMA MOT_VALANT variable[3]=valeurComptable COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE variable[4]=valeurAppreciation PERCENT COMMA MOT_DEPUIS variable[5]=compte
    ;

possedeMateriel
    :   PUCE BACKTICK variable BACKTICK variable COMMA MOT_POSSEDER variable COMMA MOT_VALANT variable COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE variable PERCENT
    //  PUCE BACKTICK variable[0]=id BACKTICK variable[1]=date COMMA MOT_POSSEDER variable[2]=nom COMMA MOT_VALANT variable[3]=valeurComptable COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE variable[4]=tauxAppreciation PERCENT
    ;

/* -------------------- Commun --------------------  */
ligneDateSpecification
    :   PUCE MOT_SPECIFIER variable
    ;

ligneDateFinSimulation
    :   PUCE MOT_FIN_SIMULATION variable
    ;

ligneDevise
    :   PUCE MOT_DEVISE_EN variable
    ;

sousTitre
    :   HASHES HASHES variable COMMA variable COMMA MOT_DEVISE_EN variable
    //  HASHES HASHES variable[0]=nom COMMA variable[1]=date COMMA MOT_DEVISE_EN variable[2]=devise
    ;

dateFin
    :   COMMA MOT_JUSQUA variable MOT_TOUT_LES ENTIER MOT_DU MOT_MOIS
    //  COMMA MOT_JUSQUA variable[0]=dateFinValue MOT_TOUT_LES ENTIER MOT_DU MOT_MOIS
    ;

/*  Valeur englobé par variable  */
variable
    :   devise
    |   argent
    |   date
    |   dateFinValue
    |   nombre
    |   text
    |   variableValue
    ;

variableValue
    :   BACKTICK VARIABLE BACKTICK
    ;

argent
    :   nombre devise
    ;

devise
    :   DEVISE
    ;

dateFinValue
    :   MOT_DATE_INDETERMINER
    |   date
    ;

nombre
    :   DECIMAL
    |   ENTIER
    ;

date
    :   MOT_LE ENTIER MOT_DU ENTIER TIRER ENTIER
    ;

text
    :   TEXT
    ;