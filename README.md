# movie-mediator
 Conception et réalisation d’un médiateur simple, exploitant différentes sources de données dans le domaine du cinéma.
 Réalisé par Alice Mabille et Cylia Belkacemi dans le cadre du Master Systèmes Intelligents Communicants de Cergy-Paris Université.

## Installation
> Cette application requiert [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) ou une version supérieure.

Librairies nécessaires :
* [Apache Jena 5.0.0](https://dlcdn.apache.org/jena/binaries/apache-jena-5.0.0.zip)
* [JSoup 1.17.2](https://jsoup.org/download)
* [JDBC](https://www.oracle.com/fr/database/technologies/appdev/jdbc-downloads.html)

## Utilisation
Ce programme s'utilise en ligne de commande. Relancer le programme pour chaque nouvelle requête.
Il répond à trois types de requêtes :
* Recherche d'informations extensives sur un film en se basant sur un titre donné.
* Recherche d'une liste d'informations moindres sur les films dans lesquels a joué un acteur donné.
* Création de fichiers CSV avec les données du site [www.the-numbers.com](www.the-numbers.com)