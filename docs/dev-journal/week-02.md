Was habe ich gebaut?
- Unit Tests für Task Domain und UseCases
- Erste Integrationstests für Controller → DB
- Validation für DTO Requests (@NotBlank, etc.)
- Globales Exception Handling (@ControllerAdvice)
- Refactoring / DTO Mapper verbessert

Architektur-Entscheidungen
- Domain Tests starten kein Spring, volle Isolation
- Integrationstests testen Systemverhalten mit Spring und DB
- DTO ↔ Domain Mapping bleibt sauber getrennt
- Repository Interface im Domain Layer, Implementation im Infrastructure Layer

Was war schwieriger als gedacht?
- Testcontainers Setup für DB Integration
- Mapping zwischen Domain ↔ JPA Entity korrekt zu testen
- Validation und Exception Handling in Controller konsistent anzuwenden
- Unsicherheit: Wie sollen Unit vs. Integration Tests sein

Was habe ich über Systeme gelernt?
- Trennung von Domain ↔ Persistence erleichtert Testbarkeit
- Testpyramide: kleine schnelle Unit Tests + weniger Integration Tests
- Clean Architecture zwingt dazu, Verantwortung klar zu definieren

Was würde ich nächste Woche besser machen?
- Vor Implementierung genau definieren, welcher Layer verantwortlich ist
- UseCases und Mapper vorher skizzieren
- Integration Tests kleiner, gezielter, ggf. Mock nutzen, wenn DB unnötig

Marktwert-Check
Testbarkeit, Layer-Verständnis, API-Qualität → hohe Hebelwirkung für Backend + DevOps