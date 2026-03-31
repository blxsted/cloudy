## Dev Journal – Week 04

### 1. Fortschritt dieser Woche

* Delete-Funktionalität implementiert (inkl. Tests)
* Lifecycle vollständig durch alle Layer umgesetzt (Domain → UseCase → API)
* Statusänderungen (start, complete, reopen) integriert
* Integrationtests für Statusänderungen erweitert
* Logging in UseCases eingeführt

---

### 2. Konkrete Ergebnisse / Output

* Vollständiges Backend mit Lifecycle-Management
* REST Endpoints für Statusänderungen vorhanden
* Erweiterte Integrationtest-Suite (inkl. Status-Flows und Delete)
* Konsistente Domain-Logik mit State Machine
* Erste Observability durch Logging im Application Layer

---

### 3. Offene Punkte / Probleme

* Delete-Regeln noch nicht klar als Domain-Entscheidung modelliert (z. B. wann Löschen erlaubt ist)
* Logging noch nicht strukturiert (nur basic, kein Konzept)
* Exception Handling evtl. noch nicht vollständig vereinheitlicht
* API könnte noch klarer dokumentiert werden

---

### 4. Momentum

hoch

Begründung:
Ich habe mehrere Layer miteinander verbunden und ein konsistentes Systemverhalten umgesetzt, statt isolierter Features zu bauen.

---

### 5. Hebelwirkung für Gesamtstrategie

sehr hoch

Begründung:
Die Umsetzung von Lifecycle, UseCases und Integrationtests entspricht realen Backend-Systemen und zahlt direkt auf mein Zielprofil (Backend + Architektur + DevOps) ein.

---

### 6. Wichtigste Learnings

* Domain Behaviour ist zentral für Systemkonsistenz
* Integrationtests sollten reale Nutzerflüsse abbilden
* Architektur entsteht durch klare Trennung von Verantwortlichkeiten
* Logging gehört in den Application Layer, nicht in die Domain

---

### 7. Wichtigste Aktionen für nächste Woche

* Logging strukturieren (Levels, Inhalte, ggf. Request-Kontext)
* Exception Handling zentralisieren
* API dokumentieren (README oder OpenAPI)
* Erste Vorbereitung auf CI/CD und Docker
