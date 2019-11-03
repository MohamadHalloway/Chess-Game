[img_board_empty]: board_view_empty.PNG "Fenster mit leerem Schachbrett"
[img_new_game]: new_game_screen.PNG "Dialog-Fenster für neues Spiel"
[img_board_used]: board_view_used.PNG "Fenster mit genutztem Schachbrett"
[img_rochade]: rochade.png "Rochade Symbol"
[img_enpassant]: enpassant.png "En Passant Symbol"
[img_beat]: beatfigure.png "Figure Beat Symbol"
[img_promotion]: pawn_promotion_screen.PNG "Pawn Promotion Screen"
[img_open]: open_dialog.PNG "Opening Dialog"
[img_save]: save_dialog.PNG "Save Dialog"
# Benutzerhandbuch Schachspiel SWP 2019
In diesem Handbuch werden alle Möglichkeiten, die Schachspielsoftware zu nutzen, erläutert.
## Benutzung
Dieser Abschnitt erklärt, wie das Programm zu handhaben ist, um erfolgreich Schach zu spielen.  
Das heißt: 
- Wie starte ich ein Spiel?
- Wie bewege ich eine Figur?
- Wie interpretiere ich die Zuggeschichte?
- Wie wähle ich eine Figur aus, um einen Bauern am Ende vom Spielfeld zu ersetzen?
- Wie speichere ich einen Spielstand?
- Wie lade ich einen Spielstand?   
### Wie starte ich ein Spiel?
Nachdem die Software gestartet wurde, erscheint zuerst das Hauptfenster mit einem leeren Schachbrett:  
![Alt-text][img_board_empty]  
Daraufhin erscheint das Dialog-Fenster, dass sie zum starten eines neuen Spiels aufruft.
![Alt-text][img_new_game]  
Falls das Fenster über "Start" verlassen wird, wird ein neues Spiel gestartet. Falls das Verlassen über "Abbrechen" oder den Schließen-Button erfolgt, wird am aktuellen Zustand des Spiel(-feldes) nichts geändert. Sie können das Dialogfeld einfach wieder über den "Neues Spiel"-Button im Hauptfenster öffnen. Achtung: es muss immer mindestens einer der beiden Spieler menschlich sein!
### Wie bewege ich eine Figur?
Im Hauptfenster wird der menschliche Spieler, der gerade am Zug ist, durch das Hervorheben der Figuren seiner Farbe signalisiert. Der Spieler kann nun auf eine seiner hervorgehobenen Figuren klicken. Dauraufhin werden alle Felder markiert, auf die die gewählte Figur bewegt werden kann. Wollen sie die Auswahl der Figur rückgängig machen und eine andere Figur auswählen, klicken sie nochmal auf die ausgewählte Figur. Wollen sie mit der ausgewählten Figur einen Zug ausführen, klicken sie auf eines der anderen hervorgehobenen Felder.  
![Alt-text][img_board_used]  
### Wie interpretiere ich die Zuggeschichte?
Die Zuggeschichte befindet sich auf der rechten Seite des Hauptfensters unterhalb der Buttons. Kommt ein neuer Zug hinzu, wird dieser unten an die Liste angefügt.  
![Alt-text][img_board_used]  
Für jeden Zug werden links die Koordinaten in der Form  
> 'Startkoordinate' -> 'Zielkoordinate'  

gespeichert. Außerdem werden rechts zusätzlich Bilder angezeigt, die spezielle Auswirkungen des Zuges signalisieren:
- ![Alt-text][img_rochade] Dieses Symbol signalisiert, dass durch den Zug eine Rochade durchgeführt wurde.  
- ![Alt-text][img_enpassant] Dieses Symbol signalisiert, dass durch den Zug ein 'En Passant' durchgeführt wurde.  
- ![Alt-text][img_beat] Dieses Symbol signalisiert, dass durch den Zug eine gegnerische Figur geschmissen wurde.  
### Wie wähle ich eine Figur aus, um einen Bauern am Ende vom Spielfeld zu ersetzen?
Falls sie mit einem Bauern das andere Ende vom Spielfeld erreichen, dürfen sie diesen Bauern durch eine andere Figur ihrer Wahl (außer dem König) ersetzen. Hierzu wird automatisch ein Dialog-Fenster geöffnet:  
![Alt-text][img_promotion]  
Sobald sie das Fenster schließen (egal, ob durch "OK"-Button oder rotes Kreuz), wird der Bauer durch die ausgewählte Figur ersetzt.  
### Wie speichere ich einen Spielstand?
Zum Speichern eines Spielstandes drücken sie den Button "Speichere Spiel" im Hauptfenster. Daraufhin öffnet sich ein Standard-Dialogfenster.
![Alt-text][img_save]  
Speichern sie die .cgf Datei am gewünschten Ort. Bei Problemen erscheint ein Info-Fenster, dass sie auf den gescheiterten Speicherversuch hinweist.
### Wie lade ich einen Spielstand?
Zum Laden eines Spielstandes drücken sie den Button "Öffne Spiel" im Hauptfenster. Daraufhin öffnet sich ein Standard-Dialogfenster.
![Alt-text][img_open]  
Laden sie die .cgf Datei vom gewünschten Ort. Bei Problemen erscheint ein Info-Fenster, dass sie auf den gescheiterten Ladeversuch hinweist.
## Installation