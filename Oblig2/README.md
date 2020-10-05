# Obligatorisk oppgave 2 i Algoritmer og Datastrukturer

Denne oppgaven er en innlevering i Algoritmer og Datastrukturer. 

# Krav til innlevering

Se oblig-tekst for alle krav. Oppgaver som ikke oppfyller følgende vil ikke få godkjent:

* Git er brukt til å dokumentere arbeid (minst 2 commits per oppgave, beskrivende commit-meldinger)	
* Ingen debug-utskrifter
* Alle testene som kreves fungerer (også spesialtilfeller)
* Readme-filen her er fyllt ut som beskrevet

# Arbeidsfordeling

Oppgaven er levert av følgende studenter:
* Donni Riel Garvida, S344215, s344215@oslomet.no
* ...

Jeg har brukt git til å dokumentere arbeidet mitt. Jeg har 38 commits totalt, og hver logg-melding beskriver det jeg har gjort av endringer.

I oppgaven har vi hatt følgende arbeidsfordeling:
* Donni har hatt hovedansvar for oppgave 1, 2, 3, 4, 5, 6, og 8. 


# Beskrivelse av oppgaveløsning (maks 5 linjer per oppgave)

* Oppgave 1: Løste ved å lage en Object tabell med verdiene fra tabell a. 
	Loop gjennom object tabell og opprette nye noder for hvert element som ikke er null.

* Oppgave 2: toString() ble løst ved å bruke en hjelpe variabel. Pekeren starter fra hode og går til hale.
	omvendtString() ble løst på samme måte men går i motsatt retning. Pekeren starter fra hale.
	leggInn(T) ble løst ved å oppdatere noden hale.
	
* Oppgave 3: finnNode(int) ble løst ved hjelp av en peker node. En for loop som kjører til gitt indeks og returnerer siste node som er pekeren. 
	hent(int) og oppdater(int, T) ble løst ved bruk av finnNode(int)
	subliste(int fra,int til) ble løst ved hjelp av en peker variabel og en for løkke som kjører til antall. 
	Fra når pekeren er lik "fra" så legger inn node i den liste som skal returneres.

* Oppgave 4: Løste ved å bruke en for løkke og en node peker. Den skal kjøre til peker.verdi er lik verdi.

* Oppgave 5: Løste ved å bruke tre node pekere(ny node, venstre node, høyre node). 
	Oppdater forrige og neste pekere.

* Oppgave 6: T fjern(int) ble løst ve bruke av tre node pekere (noden som skal fjernes, venstre node, høyre node).
	Oppdater neste peker til venstre node og forrige peker til høyre node.
	boolean fjern(T) fjerner noden på samme måte som fjern(int). Brukte while løkke med en indeks peker for å finne noden som skal fjerens.

* Oppgave 8: Løste ved å implementere hva som står i oppgaveteksten.
