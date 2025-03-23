Si vous avez un build réussi avec Maven mais que les imports sont toujours soulignés en rouge dans VSCode, c'est probablement un problème lié à la configuration de l'IDE plutôt qu'à votre code ou à la structure de vos POM.

Voici quelques suggestions pour résoudre ce problème dans VSCode :

1. **Mettez à jour l'extension Java pour VSCode** :
   - Vérifiez que vous avez les dernières versions des extensions Java pour VSCode
   - Les extensions importantes incluent "Extension Pack for Java", "Spring Boot Extension Pack" et "Maven for Java"

2. **Rafraîchissez votre projet Java dans VSCode** :
   - Ouvrez la palette de commandes (Ctrl+Shift+P ou Cmd+Shift+P sur Mac)
   - Tapez et sélectionnez "Java: Clean Java Language Server Workspace"
   - Sélectionnez "Restart and delete" lorsque vous y êtes invité

3. **Vérifiez la configuration de votre projet** :
   - VSCode peut parfois avoir du mal avec les projets multi-modules Maven
   - Assurez-vous que VSCode a bien reconnu la structure du projet parent/enfant

4. **Configurez le classpath explicitement** :
   - VSCode pourrait ne pas reconnaître automatiquement toutes les dépendances dans les projets multi-modules
   - Vous pouvez essayer de créer ou modifier le fichier `.classpath` si nécessaire

5. **Importez explicitement le projet dans VSCode** :
   - Fermez puis rouvrez le projet
   - Assurez-vous d'ouvrir le dossier racine du projet (qui contient le pom.xml parent)

6. **Vérifiez la version de Java utilisée par VSCode** :
   - VSCode pourrait utiliser une version de Java différente de celle de votre projet
   - Vérifiez la version de Java dans les paramètres VSCode (settings.json)

7. **Essayez une reconstruction complète du projet** :
   ```bash
   cd /Users/hachimhassani/projects/tutos/quizapi
   mvn clean install -U
   ```
   Le flag `-U` force une mise à jour des dépendances.

Le fait que la compilation fonctionne avec Maven confirme que votre structure de projet et vos dépendances sont correctes. Le problème est donc très probablement lié à la façon dont VSCode interprète votre projet.

Si les suggestions ci-dessus ne fonctionnent pas, vous pourriez également essayer de redémarrer complètement VSCode, ou même de considérer l'utilisation d'un IDE comme IntelliJ IDEA qui gère généralement mieux les projets Java multi-modules.