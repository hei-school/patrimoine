# Spécifier

Un [patrimoine](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/Patrimoine.java) (économique,
c'est-à-dire évaluable dans [une devise](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/Devise.java)),
est composé de plusieurs types de [possessions](https://github.com/hei-school/patrimoine/tree/main/src/main/java/school/hei/patrimoine/modele/possession).
Ces possessions peuvent être groupées en 3 grands [agrégats](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/possession/TypeAgregat.java) :
- 💵 **La trésorerie**. C'est l'[argent](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/possession/Argent.java) disponible immédiatement.
  Ici vont l'épargne, les comptes courants, les espèces.
- 🏠 **Les immobilisations**. Ce sont les possessions [matérielles](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/possession/Materiel.java)
  comme une maison, ou immatérielles comme un logiciel, qui ont bien une valeur économique mais qui ne sont pas convertibles facilement en argent.
  De fait, elles ne sont pas aussi facile à exploiter que la trésorerie. Par exemple, il faut encore les vendre, les louer ou les mettre en hypothèque pour en tirer profit.
- 🗞️ **Les obligations**. C'est ce que vous devez aux autres ou [dettes](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/possession/Dette.java),
  ainsi que ce que les autres vous doivent ou [créances](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/possession/Creance.java).

[Voici](https://owncloud.hei.school/s/VzAvh2EEr34BAJC) le sujet d'exercice initial pour lequel la librairie a été créée chez [HEI](https://hei.school),
et sur lequel vous pouvez vous entraîner à spécifier.
[Voici](https://owncloud.hei.school/s/SiVASYtItCESdRp) l'examen qui a suivi, ainsi que sa spécification [corrigée](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/cas/zety/PatrimoineZetyAu3Juillet2024.java).

👷‍♂️⚒️ Des travaux sont en cours afin de permettre de spécifier plus facilement, idéalement sans coder en Java.

# Projeter

[Un grapheur](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/visualisation/xchart/GrapheurEvolutionPatrimoine.java) permet de générer sous forme de graphe l'évolution du patrimoine sur une période de temps donnée.
Le graphe est configurable :
- il peut afficher [la trésorerie uniquement](https://github.com/hei-school/patrimoine/blob/main/src/test/resources/patrimoine-cresus-sur-quelques-annees_treso.png),
- ou [les immobilisations uniquement](https://github.com/hei-school/patrimoine/blob/main/src/test/resources/patrimoine-cresus-sur-quelques-annees_immo.png),
- ou [les obligations uniquement](https://github.com/hei-school/patrimoine/blob/main/src/test/resources/patrimoine-cresus-sur-quelques-annees_obli.png),
- ou [n'importe quelle combinaison](https://github.com/hei-school/patrimoine/blob/main/src/test/resources/patrimoine-cresus-sur-quelques-annees.png) d'entre eux.

Un [visualiseur Swing](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/visualisation/swing/ihm/VisualiseurCas.java) permet de visualiser facilement les projections d'une spécification donnée.

![](https://github.com/hei-school/patrimoine/blob/main/doc/ihm-swing.png)

# Recouper

Les [flux journaliers](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/evolution/FluxJournalier.java)
sont la liste des opérations qui se sont déroulées depuis la date de la spécification jusqu'à la fin choisie de la projection.

Il faut suivre régulièrement cette liste et mettre à jour la spécification en fonction de quelles opérations **passées** se sont réellement réalisées,
et quelles opérations ne se sont jamais réalisées.

Idéalement, dérouler **une politique Zéro Flux Journaliers jusqu'à Aujourd'hui (ZFJA)** :
c'est-à-dire mettre à jour la spécification jusqu'à une date dont la fraîcheur permet d'éliminer tous les flux journaliers listés.
Si ZFJA est atteint, alors votre specification représente fidèlement la réalité à date d'aujourd'hui.

Le visualiseur Swing permet de lister facilement les flux journaliers.

# Alerter

Les [flux impossibles](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/evolution/EvolutionPatrimoine.java#L68)
arrivent quand vous essayer de faire déplacer de l'argent depuis une source qui n'en contient pas suffisamment.

Idéalement, dérouler **une politique Zéro Flux Impossibles (ZFI)** :
c'est-à-dire bien plannifier l'évolution de votre patrimoine de sorte à ce que votre trésorerie puisse toujours couvrir vos différentes opérations.
Si ZFI est atteint, alors vous devrez pouvoir réaliser toutes vos opérations.

Le visualiseur Swing permet de lister facilement les flux impossibles.
