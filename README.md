# Lancemennt projet

```bash
# 1. Lancer tests
mvn test

# 2. Init BDD dev
docker-compose up -d

# 3. Lancer projet
mvn spring-boot:run
```

# Résumé `UserServiceTest.java`

- Teste toutes les opérations REST (CRUD) sur les utilisateurs.
- Vérifie les règles métiers : email unique, gestion erreurs (exceptions personnalisées).
- Utilise DTO sans mot de passe.
- Dépendances injectées, repo mocké (Mockito), pas de vraie BDD.
- Couvre tous les cas : succès, erreurs, doublons, non trouvés.

**Ce fichier garantit la conformité métier et technique de l’API utilisateur.**
