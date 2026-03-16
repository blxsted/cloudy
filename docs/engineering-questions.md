# Engineering Questions

## Architecture

Why must the domain layer not contain Spring annotations?

Why is the domain framework independent?

What violates the dependency rule?

Who is allowed to depend on whom?

Where does framework coupling begin?

Why is the repository defined as an interface?

## Design

What is the difference between an entity and a DTO?

What belongs in the domain layer?

Where would many developers take shortcuts?

---

Repository
Das Repository ist ein Interface weil:
1. Abstraktion
* Domain kennt keine Implementierung
* Ein Interface definiert ein Verhalten, ohne die Umsetzung festzulegen
2. lose Kopplung
* bessere Testbarkeit
* austauschbare Infrastruktur

Verletzung der Dependency Rule
1. Abhängigkeiten dürfen nur nach innen zeigen.
* Eine Verletzung entsteht, wenn eine innere Schicht von einer äußeren abhängig wird.
  Frameworks ändern sich ständig:
    - Spring
    - Hibernate
    - Datenbank
    - Web Framework
      Aber die Business-Logik sollte stabil bleiben.
      Wenn die Domain unabhängig ist, kannst du z.B.:
    - die Datenbank wechseln
    - Spring entfernen
    - eine CLI statt REST bauen
    - eine andere API hinzufügen
      ohne die Domain zu ändern.
      Erlaubt:
      Controller → UseCase → Domain

Die Infrastruktur implementiert das Interface, aber die Domain kennt sie nicht.
Domain
↑
UseCase
↑
Infrastructure (z.B. JPA Repository)

Wer darf wen kennen ?
Dependency Inversion

Controller  
↓  
Use Case  
↓  
Domain       
Und zusätzlich:  
Infrastructure  
.....↓.........↓(manchmal)  
Domain  Use Cases

Domain definiert  
Use Cases orchestrieren  
Infrastructure implementiert  
Controller verbindet außen mit innen

Warum darf dein Domain Layer keine Spring-Annotationen enthalten?  
Der Domain Layer darf keine Spring-Annotationen enthalten, weil er keine Abhängigkeit zu Frameworks haben darf. Die Domain soll nur Business-Logik enthalten und dadurch unabhängig, stabil und wiederverwendbar bleiben.  
Entity vs. DTO  

Entity:
* Domain-Objekt
* hat Identität
* enthält Business-Logik
* repräsentiert ein fachliches Konzept  

DTO:
* Transportobjekt
* enthält nur Daten
* keine Logik
* wird für Schnittstellen verwendet

Client  
↓  
Controller  
↓  
DTO  
↓  
Use Case  
↓  
Entity (Domain)  
↓  
Repository  
↓  
Datenbank

Domain - was gehört rein?  
Die Domain enthält das fachliche Modell, die Geschäftsregeln und die Kernobjekte der Anwendung – unabhängig von Frameworks, Infrastruktur oder Schnittstellen.  
- Entities  
- Value Objects
- Domain Regeln
- Repository-Interfaces
  Wo würden viele Entwickler Abkürzungen nehmen?
- Service Klasse als Sammelstelle
- Entities direkt im Controller verwenden
- Framework-Annotationen in der Domain
- Repository-Implementierung direkt verwenden
- Business-Logik in Controller oder Repository

Controller  
↓  
Use Case  
↓  
Domain  
↓  
Repository Interface  
↓  
Infrastructure

Wo beginnt Framework-Abhängigkeit?
Framework-Abhängigkeit beginnt dort, wo der Code ohne das Framework,Annotation oder APIs verwendet nicht mehr kompilieren oder laufen kann.
Zum Beispiel durch:
- Annotationen
- Framework-Typen
- Framework-Lifecycle
- Framework-Konfiguration


Die Domain ist framework-unabhängig, weil sie nur fachliche Regeln enthält und keine technischen Details kennt.  
Das wird durch zwei Dinge erreicht:
- keine Framework-Typen oder Annotationen
- Abhängigkeiten nur zu Abstraktionen






