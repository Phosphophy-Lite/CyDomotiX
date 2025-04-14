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
```
Si vous voulez lancer l'application avec la vérification des inscriptions par email, veuillez configurer un fichier de propriétés pour votre serveur SMTP (voir section "Configurations supplémentaires") et indiquer son chemin dans la commande suivante (à compléter avec le bon chemin) : 
```
mvn spring-boot:run -Dspring-boot.run.arguments="--smtp.credentials.path=/chemin/du/fichier/de/config.properties"
```
Vous pouvez exécuter le serveur sans les vérification des inscriptions par email avec cette commande :
```
mvn spring-boot:run -Dspring-boot.run.arguments="--app.email.enabled=false"
```

2.bis. Alternative : Vous pouvez aussi récupérer `cydomotix.jar` dans "Releases" et l'exécuter avec la commande suivante :
```
# Lancement avec un serveur SMTP configuré dans un fichier personnel :
java -jar cydomotix.jar --smtp.credentials.path=/chemin/du/fichier/de/config.properties

# Lancement avec vérification par email des nouveaux comptes désactivée :
java -jar cydomotix.jar --app.email.enabled=false
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

1. Si vous lancez le serveur avec l'email activé, il faut configurer un serveur SMTP. Si vous n'en avez pas, vous pouver en créer un gratuitement pour le test sur Mailtrap, par exemple.<br/>
Si vous voulez configurer un serveur : créer votre propre fichier `[nom_au_choix].properties` où vous voulez. Y renseigner dedans :
```
spring.mail.host=smtp.mailtrap.io     # à changer avec le lien de votre serveur smtp
spring.mail.port=587   # à changer (ou non) avec le port voulu (587 est sécurisé)
spring.mail.username=...    # renseigner l'authentifiant du serveur SMTP
spring.mail.password=...    # renseigner le mot de passe du serveur SMTP
```
Puis, se référer à la section "Configuration du projet" pour lancer l'application avec ce fichier de configuration du serveur SMTP. <br/>

2. Au premier lancement du site, un dossier "cydomotix" sera créé sur votre répertoire "Home" (Linux) / "C:\Users\[votre_nom_d'utilisateur]" (Windows).<br/>
Il contient les enregistrements de la base de donnée ainsi que les images uploadées.<br/>
Si vous voulez utiliser un autre dossier, vous pouvez spécifier un autre chemin en lançant le serveur avec cette commande :
```
# Pour lancer le serveur sans vérification par email
mvn spring-boot:run -Dspring-boot.run.arguments="--app.email.enabled=false --app.storage.base-dir=/chemin/perso/cydomotix"

# Pour lancer le serveur avec un fichier de configuration pour un serveur SMTP
mvn spring-boot:run -Dspring-boot.run.arguments="--smtp.credentials.path=/chemin/config.properties --app.storage.base-dir=/chemin/perso/cydomotix"
```
Alternative pour le .jar :
```
# Pour lancer le serveur sans vérification par email
java -jar cydomotix.jar --app.email.enabled=false --app.storage.base-dir=/chemin/perso/cydomotix

# Pour lancer le serveur avec un fichier de configuration pour un serveur SMTP
java -jar cydomotix.jar --smtp.credentials.path=/chemin/config.properties --app.storage.base-dir=/chemin/perso/cydomotix
```

3. Par défaut, l'application tourne en mode développeur, ce qui permet l'accès à la base de données directement par la requête `localhost:8080/h2-console`. <br/>
Pour désactiver ceci au lancement du serveur :
```
# Pour lancer le serveur sans vérification par email
mvn spring-boot:run -Dspring-boot.run.arguments="--app.email.enabled=false --spring.profiles.active=prod"

# Pour lancer le serveur avec un fichier de configuration pour un serveur SMTP
mvn spring-boot:run -Dspring-boot.run.arguments="--smtp.credentials.path=/chemin/config.properties --spring.profiles.active=prod"
```
Alternative pour le .jar :
```
# Pour lancer le serveur sans vérification par email
java -jar cydomotix.jar --app.email.enabled=false --spring.profiles.active=prod

# Pour lancer le serveur avec un fichier de configuration pour un serveur SMTP
java -jar cydomotix.jar --smtp.credentials.path=/chemin/config.properties --spring.profiles.active=prod
```

4. Pour accéder à la base de données, se rendre sur : `localhost:8080/h2-console`. Laisser tous les champs tels quels sauf le champ "JDBC" où il faut entrer :<br/>
`jdbc:h2:file:/[chemin vers votre dossier cydomotix]/cydomotix/data/db`
Par défaut, le chemin est votre dossier Home où sera créé automatiquement le dossier cydomotix au premier démarrage.<br/><br/>
