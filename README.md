# JavaEncryptedChatServer

Le Programme ConnectionsManager fonctionne de la manière suivante :

- Il essaie écoute sur le port 666 avec un thread, et crée un thread par connection
- Il attend de cette connection, qu'elle lui envoie un message chiffré avec la clef publique du serveur
- Il essaie ensuite de lui renvoyer un message "Bien reçu", chiffré avec DH
- La clef DH est générée à partir de la clef publique du client et de la clef privée du serveur.

S'ensuit alors une phase de discussion jusqu'à un "QUIT"

Hélas, un encodage non maitrisé rend la discussion impossible, pour des raisons que nous n'avons pas pu expliquer :
Exception in thread "main" java.lang.IllegalArgumentException: Last unit does not have enough valid bits
