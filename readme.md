# Home Audio Distribution

## Überblick
Das Home-Audio-Distribution System besteht aus zwei oder mehr Knoten (Home-Audio-Distribution-Units, HADU), die untereinander Audio über das lokale Netzwerk übertragen. Das System folgt dabei folgenden Regeln:

- Jeder HADU hat einen "Masterknopf"
	- Ein Druck auf diesen Knopf veranlasst diesen Knoten, das auf Line-IN liegende Signal als SRTP Stream bereit zu stellen
    - Alle anderen Knoten bekommen von dem neuen Master Knoten das "Mastersignal" und beginnen, den Stream von diesem Knoten zu lesen und auf Line-OUT auszugeben.
- Jeder Knoten hat intern eine Liste aller Im Netzwerk vorhanden HADUs. Diese Liste wird periodisch aktualisiert, indem jeder Knoten seinerseits periodisch ein "Lebenszeichen" sendet. In diesem Lebenszeichen ist auch die Information enthalten, ob der Knoten Master ist oder Client, so können neu in das Netzwerk aufgenommene Knoten ebenfalls ihre Arbeit aufnehmen.
- Die Knoten verfügen über eine Webschnittstelle über die Statusinformationen ausgegeben und für einen Knoten das Matersignal ausgelöst werden kann.
- Über eine Smartphone App kann ebenfalls (über die Webschnittstelle) das Mastersignal ausgelöst werden und so das System gesteuert werden.


## Für nächste Ausbaustufe geplant

- Aufbau mehrerer Paralleler Zonen
- Streaming über Internet
- WLAN 

## Technische Beschreibung

##### Begriffe

- NodeDirectory: Jeder Knoten hat ein internes Verzeichnis jedes Knotens


##### Start eines HADU

- Selbst im eigenen NodeDirectory eintragen
- FFServer starten
- Thread für das Empfangen von Broadcasts starten (ReceiverThread)
- Thread für das Senden von Broadcasts starten (DiscoveryThread)

##### ReceiverThread

- Wartet auf Datagram
	- Wenn MasterSignal kommt
    	- FFMpeg stoppen
        - FFPlay stoppen
        - FFPlay mit neuer IP starten
    	- NodeDirectory aktualisieren
    - Wenn ClientSignal kommt
    	- NodeDirectory aktualisieren
    	
##### DiscoveryThread

- Periodisches Senden einer Datagram Nachricht
	- Wenn HADU Master ist: `I_AM_HAD_MASTER` (MasterSignal)
    - Wenn HADU Client ist: `I_AM_HAD_CLIENT` (ClientSignal)
