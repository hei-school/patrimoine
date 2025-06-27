parser grammar PatriLangParser;

@header {
package school.hei.patrimoine.patrilang.antlr;
}

options { tokenVocab=PatriLangLexer; }

/* -------------------- Base --------------------  */
document
    :   cas
    |   toutCas
    ;

/* ToutCas */
toutCas
    :   sectionToutCasGeneral sectionCas? sectionDatesDeclarations? sectionPersonnesMoralesDeclarations? sectionPersonnesDeclarations? sectionTresoreries? sectionCreances? sectionDettes? EOF
    ;

ligneObjectifFinal
    :   MUL MOT_OBJECTIF_FINAL valeurComptable=argent
    ;

sectionToutCasGeneral
    :   HASHES ENTETE_GENERAL ligneObjectifFinal
    ;

sectionCas
    :   HASHES ENTETE_CAS ligneNom*
    ;

sectionPersonnesMoralesDeclarations
    :   HASHES ENTETE_PERSONNES_MORALES ligneNom*
    ;

sectionPersonnesDeclarations
    :   HASHES ENTETE_PERSONNES ligneNom*
    ;

sectionDatesDeclarations
    :   HASHES ENTETE_DATES ligneDateDeclaration*
    ;

ligneDateDeclaration
    :   MUL nom=text COLON dateValue=variable
    ;

/* Cas */
cas
    :   sectionCasGeneral sectionPossesseurs  sectionDatesDeclarations? sectionTresoreries? sectionCreances? sectionDettes? sectionInitialisation? sectionOperations? sectionSuivi?  sectionOperationTemplateDeclaration? EOF
    ;

sectionCasGeneral
    :   HASHES ENTETE_GENERAL ligneDateSpecification ligneDateFinSimulation ligneCasNom ligneDevise
    ;

ligneCasNom
    :   MUL MOT_CAS_DE nom=text
    ;

sectionPossesseurs
    :   HASHES ENTETE_POSSESSEURS lignePossesseur+
    ;

lignePossesseur
    :   MUL nom=variable pourcentage=nombre PERCENT
    ;

ligneDateSpecification
    :   MUL MOT_SPECIFIER dateValue=variable
    ;

ligneDateFinSimulation
    :   MUL MOT_FIN_SIMULATION dateValue=variable
    ;

ligneDevise
    :   MUL MOT_DEVISE_EN devise
    ;

sectionInitialisation
    :   HASHES ENTETE_INITIALISATION operations*
    ;

sectionSuivi
    :   HASHES ENTETE_SUIVI operations*
    ;

objectif
    :   MUL id dateValue=variable COMMA MOT_OBJECTIF_DE valeurComptable=argent MOT_POUR compteNom=variable
    ;

correction
    :   MUL id dateValue=variable COMMA MOT_CORRIGER valeurComptable=argent MOT_DANS compteNom=variable
    ;
/* -------------------- Possessions --------------------  */
/* Trésorerie */
sectionTresoreries
    :   HASHES ENTETE_TRESORERIES compteElement*
    ;

/* Créances */
sectionCreances
    :   HASHES ENTETE_CREANCES compteElement*
    ;

/* Dettes */
sectionDettes
    :   HASHES ENTETE_DETTES compteElement*
    ;

compteElement
    :   compte
    |   MUL variable
    ;
/* Opérations */
sectionOperationTemplateDeclaration
    :   HASHES ENTETE_TEMPLATES operationTemplate*
    ;

operationTemplate
    :   HASHES HASHES name=text LPAREN operationTemplateParam? RPAREN operations*
    ;

operationTemplateParam
    :   operationTemplateParamValue (COMMA operationTemplateParamValue)*
    ;

operationTemplateParamValue
    :   argName=variable
    ;

operationTemplateCall
    :   MUL BACKTICK templateName=text LPAREN operationTemplateCallArg? RPAREN BACKTICK
    ;

operationTemplateCallArg
    :   operationTemplateCallArgValue (COMMA operationTemplateCallArgValue)*
    ;

operationTemplateCallArgValue
    :   variable
    ;

sectionOperations
    :   HASHES ENTETE_OPERATIONS operations*
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
    |   correction
    |   objectif
    |   operationTemplateCall
    ;

/* Simple Possessions */
compte
    :   MUL nom=text COMMA MOT_VALANT valeurComptable=argent dateValue=variable
    ;

fluxArgentTransferer
    :   MUL id dateValue=variable COMMA MOT_TRANSFERER valeurComptable=argent MOT_DEPUIS compteDebiteurNom=variable MOT_VERS compteCrediteurNom=variable dateFin?
    ;

fluxArgentEntrer
    :   MUL id dateValue=variable COMMA MOT_ENTRER valeurComptable=argent MOT_VERS compteCrediteurNom=variable dateFin?
    ;

fluxArgentSortir
    :   MUL id dateValue=variable COMMA MOT_SORTIR valeurComptable=argent MOT_DEPUIS compteDebiteurNom=variable dateFin?
    ;

acheterMateriel
    :   MUL id dateValue=variable COMMA MOT_ACHETER materielNom=text COMMA MOT_VALANT valeurComptable=argent COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE pourcentageAppreciation=nombre PERCENT COMMA MOT_DEPUIS compteDebiteurNom=variable
    ;

possedeMateriel
    :   MUL id dateValue=variable COMMA MOT_POSSEDER materielNom=text COMMA MOT_VALANT valeurComptable=argent COMMA MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE pourcentageAppreciation=nombre PERCENT
    ;

/* -------------------- Commun --------------------  */
sousTitre
    :   HASHES HASHES nom=text COMMA dateValue=variable COMMA MOT_DEVISE_EN devise
    ;

dateFin
    :   COMMA MOT_JUSQUA dateValue=variable MOT_TOUT_LES ENTIER MOT_DU MOT_MOIS
    ;

ligneNom
    :   MUL nom=text
    ;

argent
    :   expression devise
    ;

expression
    :   additionExpr
    ;

additionExpr
    :   multiplicationExpr ( (PLUS | MOINS) multiplicationExpr )*
    ;

multiplicationExpr
    :   atom ( (MUL | DIV) atom )*
    ;

atom
    :   MOINS atom                    # NegateExpr
    |   LPAREN expression RPAREN      # ParenExpr
    |   nombre                        # NombreExpr
    ;

devise
    :   DEVISE
    ;

nombre
    :   DECIMAL
    |   ENTIER
    ;
anneePart
    :   ENTIER (MOT_ANNEE | MOT_ANNEES) MOT_ET?
    ;

moisPart
    :   ENTIER MOT_MOIS MOT_ET?
    ;

jourPart
    :   ENTIER (MOT_JOUR | MOT_JOURS)
    ;

id
    : BACKTICK text (PLUS variable)? BACKTICK
    ;

variable
    :   date dateDelta?
    |   VARIABLE dateDelta?
    ;

date
    :   MOT_LE jour=ENTIER MOT_DU mois=ENTIER MOINS annee=ENTIER
    |   MOT_DATE_INDETERMINER
    |   MOT_DATE_MINIMUM
    |   MOT_DATE_MAXIMUM
    ;

dateDelta
    :   (PLUS | MOINS) anneePart? moisPart? jourPart?
    ;

text
    :   TEXT
    ;