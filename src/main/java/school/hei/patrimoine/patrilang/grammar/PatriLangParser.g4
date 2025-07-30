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
    :   MUL MOT_OBJECTIF_FINAL valeurComptable=variable
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
    :   HASHES ENTETE_DATES operations*
    ;

sectionNombresDeclarations
    :   HASHES ENTETE_NOMBRES operations*
    ;
/* Cas */
cas
    :   sectionCasGeneral sectionPossesseurs sectionNombresDeclarations? sectionDatesDeclarations? sectionTresoreries? sectionCreances? sectionDettes? sectionInitialisation? sectionOperations? sectionOperationTemplateDeclaration? sectionSuivi? EOF
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
    :   MUL nom=variable pourcentage=variable PERCENT
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
    :   MUL id COMMA? dateValue=variable COMMA? MOT_OBJECTIF_DE valeurComptable=variable MOT_POUR compteNom=variable
    ;

correction
    :   MUL id COMMA? dateValue=variable COMMA? MOT_CORRIGER valeurComptable=variable MOT_DANS compteNom=variable
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

valeurMarche
    :   MUL id COMMA? dateValue=variable COMMA? possession=variable MOT_VALANT valeur=variable
    ;

vente
    :   MUL id COMMA? dateValue=variable COMMA? MOT_VENDRE possession=variable MOT_POUR prixVente=variable MOT_VERS compteBeneficiaire=variable
    ;

/* Opérations */
sectionOperationTemplateDeclaration
    :   HASHES ENTETE_CONTSTRUCTEUR_D_OPERATIONS operationTemplate*
    ;

operationTemplate
    :   HASHES HASHES name=text LPAREN operationTemplateParam? RPAREN operations*
    ;

operationTemplateParam
    :   operationTemplateParamValue (COMMA? operationTemplateParamValue)*
    ;

operationTemplateParamValue
    :   argName=variable
    ;

operationTemplateCall
    :   MUL BACKTICK templateName=text LPAREN operationTemplateCallArg? RPAREN BACKTICK
    ;

operationTemplateCallArg
    :   operationTemplateCallArgValue (COMMA? operationTemplateCallArgValue)*
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
    |   rembourserDette
    |   correction
    |   objectif
    |   operationTemplateCall
    |   ligneVariableDeclaration
    |   ligneCasOperations
    |   valeurMarche
    |   vente
    ;

ligneCasOperations
    :   MUL ENTETE_OPERATIONS MOT_DE variable
    ;

ligneVariableDeclaration
    :   MUL nomEtType=variable COLON valeur=variable
    ;

/* Simple Possessions */
compte
    :   MUL nom=text COMMA? MOT_VALANT valeurComptable=variable dateValue=variable
    ;

fluxArgentTransferer
    :   MUL id COMMA? dateValue=variable COMMA? MOT_TRANSFERER valeurComptable=variable MOT_DEPUIS compteDebiteurNom=variable MOT_VERS compteCrediteurNom=variable dateFin?
    ;

fluxArgentEntrer
    :   MUL id COMMA? dateValue=variable COMMA? MOT_ENTRER valeurComptable=variable MOT_VERS compteCrediteurNom=variable dateFin?
    ;

fluxArgentSortir
    :   MUL id COMMA? dateValue=variable COMMA? MOT_SORTIR valeurComptable=variable MOT_DEPUIS compteDebiteurNom=variable dateFin?
    ;

acheterMateriel
    :   MUL id COMMA? dateValue=variable COMMA? MOT_ACHETER materielNom=text COMMA? MOT_VALANT valeurComptable=variable COMMA? MOT_DEPUIS compteDebiteurNom=variable COMMA? MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE pourcentageAppreciation=variable PERCENT
    ;

possedeMateriel
    :   MUL id COMMA? dateValue=variable COMMA? MOT_POSSEDER materielNom=text COMMA? MOT_VALANT valeurComptable=variable (MOT_OBTENU dateObtention=variable)? COMMA? MATERIEL_APPRECIATION MOT_ANNUELLEMENT_DE pourcentageAppreciation=variable PERCENT
    ;

rembourserDette
    :   MUL id COMMA? dateValue=variable COMMA? MOT_REMBOURSER dette=variable MOT_DE rembourseur=variable MOT_AVEC creance=variable MOT_DE rembourse=variable MOT_VALANT valeurComptable=variable
    ;

/* -------------------- Commun --------------------  */
sousTitre
    :   HASHES HASHES HASHES? nom=text COMMA? dateValue=variable COMMA? MOT_DEVISE_EN devise
    ;

dateFin
    :   COMMA? MOT_JUSQUA dateValue=variable MOT_TOUT_LES jourOperation=variable MOT_DU MOT_MOIS
    ;

ligneNom
    :   MUL nom=text
    ;

devise
    :   DEVISE
    ;

variable
    :   date
    |   argent
    |   expression
    |   VARIABLE
    ;

argent
    :   lhs=argentValue  ((PLUS | MOINS) rhs=argentValue MOT_EVALUER date)?
    ;

argentValue
    :   expression devise
    |   ARGENTS_VARIABLE
    ;

dateDelta
    :   (PLUS | MOINS) anneePart moisPart jourPart
    |   (PLUS | MOINS) anneePart moisPart
    |   (PLUS | MOINS) anneePart
    |   (PLUS | MOINS) anneePart jourPart
    |   (PLUS | MOINS) moisPart jourPart
    |   (PLUS | MOINS) moisPart
    |   (PLUS | MOINS) jourPart
    ;

anneePart
    :   variable (MOT_ANNEE | MOT_ANNEES) MOT_ET?
    ;

moisPart
    :   variable MOT_MOIS MOT_ET?
    ;

jourPart
    :   variable (MOT_JOUR | MOT_JOURS)
    ;

id
    : BACKTICK text (PLUS variable)? BACKTICK
    ;

date
    :    dateAtom dateDelta?
    ;

dateAtom
    :   MOT_LE jour=variable MOT_DU moisEntier=variable MOINS annee=variable
    |   MOT_LE jour=variable moisTextuel=MOIS annee=variable
    |   MOT_DATE_INDETERMINER
    |   MOT_DATE_MINIMUM
    |   MOT_DATE_MAXIMUM
    |   DATE_VARIABLE
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
    :   MOINS atom                                  # NegateExpr
    |   LPAREN expression RPAREN                    # ParenExpr
    |   duration                                    # DurationExpr
    |   uniteDateDe                                 # UniteDateDeExpr
    |   nombre                                      # NombreExpr
    |   NOMBRE_VARIABLE                             # NombreVariableExpr
    ;

duration
    :   lhs=date MOINS rhs=date DUREE_UNITE
    ;

uniteDateDe
    :   UNITE_DATE_DE date
    ;

nombre
    :   DECIMAL
    |   ENTIER
    ;

text
    :   TEXT
    ;
