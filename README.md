# SmartBudget 📉💸

Bienvenue sur le répertoire officiel de **SmartBudget**, une application Android native conçue spécifiquement pour vous aider à suivre, organiser et optimiser vos dépenses personnelles et votre trésorerie, avec une esthétique minimaliste, élégante et sans compromis ciblée autour des besoins des étudiants.

![SmartBudget Hero](app_pictures/WhatsApp%20Image%202026-04-16%20at%2010.17.06%20PM.jpeg)

## 📌 Objectif de l'Application

SmartBudget n'est pas juste un énième gestionnaire de dépenses. Constatant que beaucoup d'applications financières sont complexes, lentes, remplies de publicités ou exigent des données en ligne, SmartBudget fait le pari de **la simplicité, l'indépendance et du design**. Son architecture **Offline-First** garantit que tous vos calculs de budget demeurent 100% privés, hyper-réactifs, et continuellement disponibles sur votre smartphone. Sa mission : vous épargner des calculs désordonnés et vous offrir une conscience transparente de vos mouvements de devises du mois, en s'appuyant sur un design "Premium" extrêmement visuel.

---

## 🛠️ Technologies & Architecture

Cette application déploie intégralement les principes de la **Modern Android Development (MAD)** érigés par Google. Le code base est résiliant, réactif et robuste pour s'adapter à toutes résolutions d'écran Android modernes.

- **Kotlin** : Le langage de programmation central, concis et ultra-performant.
- **Jetpack Compose** : L'outil UI nativement déclaratif qui propulse le design épuré, soutenu par la logique de `Material Design 3` (Cartes arrondies, effets d'ombrages "Shadow" modérés, animations vectorielles).
- **Room Database** : Le gestionnaire de base de données asynchrone bâti au-dessus de SQLite pour emmagasiner les données en mode *offline*.
- **Coroutines & Kotlin Flow** : Outils de flux d'opérations autorisant des mises à jour réactives strictes : un ajout à la base met directement l'écran à jour, sans latence ni rafraichissements manuels complexes.
- **Hilt (Dagger-Hilt)** : Moteur d'Injection de Dépendances (DI) rendant le code hyper-modulable.
- **Jetpack Navigation Compose** : Configuration transparente des routes graphiques et des passages d'écrans.

---

## 🔥 Fonctionnalités Principales

### 1. 📊 Interface de Dépenses Intelligente (Structure en Accordéons)
![Ecran Principal - Dépenses](app_pictures/WhatsApp%20Image%202026-04-16%20at%2010.17.06%20PM%20(1).jpeg)
- **Totaux Mensuels Fixes** : Gardez perpétuellement un oeil sur le total de consommation du mois en cours via la carte principale, massivement colorée en haut d'interface.
- **L'Accordéon des Catégories** : Vos dépenses ne sont plus un "fouillis indémêlable". Elles figurent sous forme de groupes propres (Catégories). Cliquez sur une carte (ex: 🍔 Nourriture) pour qu'elle s'entrouvre et mette en relief les dépenses précises (ex: "McDo" -> -60 MAD) passées juste en dessous.
- **Moteur Temporel** : Parcourez les dépenses d'un mois / d'une année vers un/une autre sans aucune contrainte via le Header directionnel.

### 2. 📝 Création et Suivi Flexible d'une Dépense
![Ajout Dépense](app_pictures/WhatsApp%20Image%202026-04-16%20at%2010.17.06%20PM%20(2).jpeg)
- Complétion d'un formulaire simple via le bouton central Flottant « **+** ».
- Bloquage des erreurs d'insertion via un pavé numérique validé automatiquement au déclic.
- Sélecteur de date interactif moderne « **DatePicker** » natif interceptant la date du jour, tout en conférant la pleine liberté de stocker ou d'altérer la date d'une facturation ultérieure passée !

### 3. 🤔 Catégories Dynamiques 100% Personnalisables
![Création de Catégorie](app_pictures/WhatsApp%20Image%202026-04-16%20at%2010.17.07%20PM.jpeg)
Aucune base de donnée factice ne vous enchevêtre ; c'est vous le designer de votre budget :
- Créez facilement des classes de dépenses manuellement dans les options.
- Assignez leur un **Nom textuel**, un **Emoji expressif**, ainsi qu'une **Couleur (Sélecteur Code Hex)** pour styliser un look de listes propre et qui vous correspond totalement.

### 4. 📈 Statistiques et Poids de Répartition
![Ecran Statistiques](app_pictures/WhatsApp%20Image%202026-04-16%20at%2010.17.07%20PM%20(1).jpeg)
- L'écran `StatsScreen` est l'allié décisionnel de fin du mois.
- Identifications de "Fuites Budget" ou des dépenses critiques via un calcul algorithmique sur les sommes globales, dessinant de pures **ProgressBars linéaires**. Celles-ci estiment explicitement le pourcentage drainé du portefeuille visuellement, colorées en adéquation avec votre préférence configurée.

### 5. ⚙️ Contrôle Total & Import / Export CSV (Souveraineté)
![Ecran Paramètres](app_pictures/WhatsApp%20Image%202026-04-16%20at%2010.17.07%20PM%20(2).jpeg)
Le tableau des options (`SettingsScreen`) a été refaçonné pour les utilisateurs puissants :
- **Conversion Dynamique de Devise** : Choisissez manuellement la devise financière (MAD, EUR, USD). Instantanément, toutes devises formatées dans vos UI seront métamorphosées au standard choisi.
- **Adaptation Linguistique** : Sélecteur de langage (Français / English) emboîté dans la logique de stockage.
- **Exportation CSV** : Effectuez facilement un Backup total du mois consulté sous un fichier "Tableur" (Excel). Le bouton exploite une mécanique `FileProvider` native et appelle les intents contextuels natifs d'Android pour vous proposer d'enregistrer le fichier / le partager rapidement (WhatsApp, E-mail...).
- **Importation CSV Native** : Une fausse manipulation ? Ouvrez un explorateur d'archives embarqué natif (`rememberLauncherForActivityResult`), chargez votre `.csv` personnel et un flux asynchrone rechargera la data intégralement dans l'application Room !

---

## 🎨 Conception Graphique, UI & UX 

Le design visuel obéit rigoureusement au cahier des charges "Look moderne, clair et taillé pour les étudiants".

- **Fondation Lumineuse** : Couleur de fond de travail extra pur blanc perle (`#F8FAFC`). 
- **Contraste Sécurisant** : Emploi d'un bleu roi profond (`#1D4ED8` / `#0F172A`) sur les zones chaudes et importantes, procurant une distinction hyper hiérarchisée tout en imposant la modernité.
- Des icônes de la librairie **Google Rounded Icon**, adoucissant les polices. Les dimensions ont été paramétrées millimétriquement  pour assurer que toutes interfaces et tous symboles s'accommodent sur d'étroits écrans mobiles (typiquement des dimensions `360dp`).
- **Elevations et Material Card** : Des ombres minimes (`2dp-8dp`) combinées à des angles généreusement incurvées (`16.dp` à `24.dp`) modélisant l'atmosphère tridimensionnelle discrète (subtiles interactions néomorphiques) typique à 2026.

---

*Pensé structurellement par passion pour le bien financier, sans fioritures.*
