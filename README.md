# Benutzerhandbuch

# Schachspiel

## Softwarepraktikum 2019

### von: Adrian Samoticha, Simon Trapp, Mohamad Halloway


In diesem Handbuch wird die Installation und Benutzung der Schachspielsoftware erläutert.

## 1 Installation

Dieses Kapitel erklärt die Ausführungsvoraussetzungen, die Einrichtung und den Erhalt des
Programms.

### 1.1 Voraussetzungen

Um das Programm ausführen zu können, muss Java in der Version 8 (Update 180 oder höher)
installiert sein.

### 1.2 Einrichtung und Deployment

Die ausführbare Datei „chess_swp_2019.jar“ kann aus dem git-Repository des Projekts heruntergeladen
werden. Diese kann beliebig auf der Festplatte des Rechners platziert und dann im Explorer per
Doppelklick auf die Datei ausgeführt werden.

## 2 Benutzung

Dieser Abschnitt erklärt, wie das Programm zu handhaben ist, um erfolgreich Schach zu spielen.

Das heißt:

1. Wie starte ich ein Spiel?
2. Wie bewege ich eine Figur?
3. Wie interpretiere ich die Zuggeschichte?
4. Wie wähle ich eine Figur aus, um einen Bauern am Ende vom Spielfeld zu ersetzen?
5. Wie speichere ich einen Spielstand?
6. Wie lade ich einen Spielstand?


### 2.1 Wie starte ich ein Spiel?

Nachdem die Software gestartet wurde, erscheint zuerst das Hauptfenster mit einem leeren
Schachbrett:

Daraufhin erscheint das Dialog-Fenster, dass sie zum Starten eines neuen Spiels aufruft.

Falls das Fenster über "Start" verlassen wird, wird ein neues Spiel gestartet. Falls das Verlassen über
"Abbrechen" oder den Schließen-Button erfolgt, wird am aktuellen Zustand des Spielfeldes nichts
geändert. Sie können das Dialogfeld einfach wieder über den "New game"-Menüpunkt im
Hauptfenster öffnen.


### 2.2 Wie bewege ich eine Figur?

Im Hauptfenster wird der menschliche Spieler, der gerade am Zug ist, durch das Hervorheben der
Figuren seiner Farbe signalisiert. Der Spieler kann nun auf eine seiner hervorgehobenen Figuren
klicken. Daraufhin werden alle Felder markiert, auf die die gewählte Figur bewegt werden kann.
Wollen sie die Auswahl der Figur rückgängig machen und eine andere Figur auswählen, klicken sie
nochmal auf die ausgewählte Figur. Wollen sie mit der ausgewählten Figur einen Zug ausführen,
klicken sie auf eines der anderen hervorgehobenen Felder.

### 2.3 Wie interpretiere ich die Zuggeschichte?

```
Die Zuggeschichte befindet sich auf der rechten Seite des Hauptfensters.
Kommt ein neuer Zug hinzu, wird dieser unten an die Liste angefügt.
```
```
Für jeden Zug werden links die Koordinaten in der Form 'Startkoordinate'
-> 'Zielkoordinate' gespeichert. Außerdem werden rechts zusätzlich
Bilder angezeigt, die spezielle Auswirkungen des Zuges signalisieren:
```
```
Dieses Symbol signalisiert, dass durch den Zug eine Rochade durchgeführt wurde.
```
```
Dieses Symbol signalisiert, dass durch den Zug ein 'En Passant' durchgeführt wurde.
```
```
Dieses Symbol signalisiert, dass durch den Zug eine Figur geschmissen wurde.
```

### 2.4 Wie wähle ich eine Figur aus, um einen Bauern am Ende vom Spielfeld

### zu ersetzen?

Falls sie mit einem Bauern das andere Ende vom Spielfeld erreichen, dürfen sie diesen Bauern durch
eine andere Figur ihrer Wahl (außer eines Königs) ersetzen. Hierzu wird automatisch ein Dialog-
Fenster geöffnet:

Sobald sie das Fenster schließen (egal, ob durch "OK"-Button oder rotes Kreuz), wird der Bauer durch
die ausgewählte Figur ersetzt.

### 2.5 Wie speichere ich einen Spielstand?

Zum Speichern eines Spielstandes drücken sie den Menüpunkt "Save" im Hauptfenster. Daraufhin
öffnet sich ein Standard-Dialogfenster.

Speichern sie die cgf-Datei am gewünschten Ort. Bei Problemen erscheint ein Infofenster, dass sie
auf den gescheiterten Speicherversuch hinweist.


### 2.6 Wie lade ich einen Spielstand?

Zum Laden eines Spielstandes drücken sie den Menüpunkt "Load" im Hauptfenster. Daraufhin öffnet
sich ein Standard-Dialogfenster.

Laden sie die cgf-Datei vom gewünschten Ort. Bei Problemen erscheint ein Info-Fenster, dass sie auf
den gescheiterten Ladeversuch hinweist.


