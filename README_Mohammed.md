# LANCER LE PROJET
**Prérequis :**
- [Java 21](https://www.oracle.com/fr/java/technologies/downloads/#java21)
- [Maven](https://maven.apache.org/download.cgi)

**Commandes pour le lancement :**
```bash
# Dans le terminal à la racine du projet
mvn spring-boot:run
```

# Résumé [`UserServiceTest.java`](./src/test/java/fr/ynov/testdevweb/services/UserServiceTest.java)

- Teste toutes les opérations REST (CRUD) sur les utilisateurs.
- Vérifie les règles métiers : email unique, gestion erreurs (exceptions personnalisées).
- Utilise DTO sans mot de passe.
- Dépendances injectées, repo mocké (Mockito), pas de vraie BDD.
- Couvre tous les cas : succès, erreurs, doublons, non trouvés.

**Ce fichier garantit la conformité métier et technique de l’API User.**
