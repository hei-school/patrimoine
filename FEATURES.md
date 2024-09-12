# Spécifier

Un [patrimoine](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/Patrimoine.java) (économique,
c'est-à-dire évaluable dans [une devise](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/Devise.java)),
est composé de plusieurs types de [possessions](https://github.com/hei-school/patrimoine/tree/main/src/main/java/school/hei/patrimoine/modele/possession).
Ces possessions peuvent être groupées en 3 grands [agrégats](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/possession/TypeAgregat.java) :
- 💵 **La trésorerie**. C'est l'[argent](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/possession/Argent.java) disponible immédiatement.
  Ici vont l'épargne, les comptes courants, les espèces.
- 🏠 **Les immobilisations**. Ce sont les possessions [matérielles](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/possession/Materiel.java)
  comme une maison, ou immatérielles comme un logiciel, qui ont bien une valeur économique mais qui ne sont pas convertibles facilement en argent.
  De fait, elles ne sont pas aussi facile à exploiter que la trésorie. Par exemple, il faut encore les vendre, les louer ou les mettre en hypothèque pour en tirer profit.
- 🗞️ **Les obligations**. C'est ce que vous devez aux autres ou [dettes](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/possession/Dette.java),
  ainsi que ce que les autres vous doivent ou [créances](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/modele/possession/Creance.java).

[Voici](https://owncloud.hei.school/s/VzAvh2EEr34BAJC) le sujet d'exercice initial pour lequel la librairie a été créée chez [HEI](https://hei.school),
et sur lequel vous pouvez vous entraîner à spécifier.
[Voici](https://owncloud.hei.school/s/SiVASYtItCESdRp) l'examen qui a suivi, ainsi que sa spécification [corrigée](https://github.com/hei-school/patrimoine/blob/main/src/main/java/school/hei/patrimoine/cas/zety/PatrimoineZetyAu3Juillet2024.java).

Des interfaces visuelles sont en cours de développement afin de permettre de spécifier sans coder en Java.

# Projeter
# Recouper
# Alerter
