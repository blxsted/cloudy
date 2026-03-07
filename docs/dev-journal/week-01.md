# Dev Journal – Week 01

## 1. Was habe ich gebaut?

- Domain Model für Task implementiert
- Repository Interface im Domain Layer erstellt
- Persistence Adapter mit Spring Data JPA gebaut
- REST API mit POST /tasks und GET /tasks/{id}
- DTO Layer eingeführt

---

## 2. Architektur-Entscheidung

Ich habe das Repository als Interface im Domain Layer definiert und die Implementierung im Infrastructure Layer platziert.

Begründung:
Die Domain soll nicht von Frameworks wie Spring Data abhängen. Dadurch bleibt die Businesslogik unabhängig von der Datenbanktechnologie.

---

## 3. Was war schwieriger als gedacht?

Die Dependency Injection funktionierte zunächst nicht, weil meine UseCases nicht als Spring Beans registriert waren.

Das hat gezeigt, dass Spring nur Klassen instanziiert, die im Application Context registriert sind.

Ich denke ich verstehe Konstruktoren auch immer noch nciht so richtig. Auch die Anforderungen der "Clean architecture" zu erfüllen war auf anhieb nicht leicht. Da ich durch Annotations vieles nicht kannte.

---

## 4. Was habe ich über Systeme gelernt?

Layer-Trennung zwingt zu klaren Verantwortlichkeiten.

Wenn Domain, Persistence und Web getrennt sind, wird der Datenfluss eines Systems deutlich verständlicher.

---

## 5. Was würde ich nächste Woche besser machen?

Vor dem Implementieren zuerst die Architekturstruktur definieren, um spätere Refactorings zu vermeiden.

---

## 6. Marktwert-Check

Diese Woche habe ich gelernt, Clean Architecture mit Spring Boot praktisch umzusetzen und Layer-Abhängigkeiten sauber zu definieren.

---

## 7. Wo liegt deine größte Unsicherheit?

Zu schnell aufzugeben und mich nicht intensiv genug den Antworten und Anforderungen zu widmen. Lerne ich hierdurch echt etwas?
