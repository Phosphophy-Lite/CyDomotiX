# CyDomotiX
Le site web pour gérer, automatiser et superviser sa maison connectée.

## Prérequis

- Git
- JDK 23+ (OpenJDK 23 recommandé)
- Maven

Assurez-vous d'avoir Git installé sur votre machine.<br/><br/>
Vous aurez besoin d'un JDK Java version 23+. Vous pouvez notamment utiliser OpenJDK 23 pour exécuter le projet. Si vous ne l'avez pas encore installé, voici comment le faire :
- Linux (Debian/Ubuntu) :
```
sudo apt update
sudo apt install openjdk-23-jdk
```
- Windows : <br/>
Téléchargez OpenJDK 23 depuis l'archive de <a href="https://jdk.java.net/archive/">Oracle</a>.<br/><br/>

Maven doit être installé pour gérer les dépendances et exécuter les commandes de construction du projet. Si vous ne l'avez pas encore installé, voici comment le faire :
- Linux (Debian/Ubuntu) :
```
sudo apt-get install maven
```
- Windows : <br/>
Téléchargez Maven depuis <a href="https://maven.apache.org/download.cgi">Apache Maven</a>, décompressez le fichier téléchargé et ajoutez le chemin de Maven dans les variables d'environnement PATH.<br/><br/>

Pour s'assurer que Java version 23 et Maven sont correctement installés : <br/>
```
java -version
mvn -version
```

## Configuration du projet

1. Clonez le dépôt dans un dossier au choix et se rendre à l'intérieur de sous-dossier Cydomotix/cydomotix via le terminal. <br/>
```
git clone https://github.com/Phosphophy-Lite/CyDomotiX.git
cd nom-du-dossier-cloné/cydomotix
```

2. Une fois à l'intérieur de ce dossier, vous pouvez lancer la commande : <br/>
```
mvn clean install -DskipTests
mvn spring-boot:run
```

3. Ouvrez un navigateur et rendez vous sur `http://localhost:8080`. <br/>
4. Des comptes par défaut sont initialisés au premier démarrage, pour le test de l'application. Vous pouvez utiliser les authentifiants fournis dans la console pour tester le site. <br/>
Si besoin, par défaut : <br/>
- Compte administrateur :
  - login : `admin`
  - mot de passe : `adminpassword`
- Compte utilisateur complexe (gestion) :
  - login : `gestion`
  - mot de passe : `gestionpassword`
- Compte utilisateur simple :
  - login : `crewmate`
  - mot de passe : `userpassword`

## Configurations supplémentaires (optionelles)

1. Au premier lancement du site, un dossier "cydomotix" sera créé sur votre répertoire "Home" (Linux) / "C:\Users\[votre_nom_d'utilisateur]" (Windows).<br/>
Il contient les enregistrements de la base de donnée ainsi que les images uploadées.<br/>
Si vous voulez utiliser un autre dossier, il faudra modifier la ligne suivante dans `cydomotix/src/main/resources/application.properties` par le chemin souhaité :
```
# Dossier général
app.storage.base-dir=${user.home}/cydomotix
```

2. Par défaut, l'application tourne en mode développeur, ce qui permet l'accès à la base de données directement par la requête `localhost:8080/h2-console`. <br/>
Pour désactiver ceci : modifier dans `cydomotix/src/main/resources/application.properties` :
```
# CHANGER CETTE LIGNE EN spring.profiles.active=prod POUR DEPLOIEMENT
# Activer le Dev Mode : utiliser un profil de lancement dev quand on lance l'application (utilisé pour exécuter certaines parties du code pour des tests)
spring.profiles.active=dev
```

3. Pour accéder à la base de données, se rendre sur : `localhost:8080/h2-console`. Laisser tous les champs tels quels sauf le champ "JDBC" où il faut entrer :<br/>
`jdbc:h2:file:/[chemin vers votre dossier cydomotix]/cydomotix/data/db`
Par défaut, le chemin est votre dossier Home où sera créé automatiquement le dossier cydomotix au premier démarrage.<br/><br/>

4. L'inscription sera impossible sans un serveur SMTP configuré. Si vous n'en avez pas, vous pouver en créer un gratuitement pour le test sur Mailtrap, par exemple.<br/>
Si vous voulez configurer un serveur : modifier dans `cydomotix/src/main/resources/application.properties` :
```
## CONFIGURATION SERVEUR MAIL SMTP ##
spring.config.import=optional:classpath:application-secret.properties
spring.mail.host=smtp.mailtrap.io # à changer par le lien du serveur utilisé
spring.mail.port=587
spring.mail.protocol=smtp
```
Notez la ligne `spring.config.import=optional:classpath:application-secret.properties`. <br/>
Ce fichier n'étant pas importé dans le Git (pour des raisons évidentes de sécurité), vous pourrez créer un fichier `application-secret.properties` dans `cydomotix/src/main/resources/` 
et y mettre vos identifiants et mot de passe de votre serveur SMTP avec les lignes suivantes :
```
spring.mail.username=...        # l'authentifiant du serveur SMTP
spring.mail.password=...        # le mot de passe du serveur SMTP
```
