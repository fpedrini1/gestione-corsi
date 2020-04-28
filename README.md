# GestioneCorsi

## Versioni
[![Version](https://img.shields.io/badge/Vaadin-14.1.5-blue)]() 
[![Version](https://img.shields.io/badge/JOOQ-3.12.3-blue)]()

## Progetto
GestioneCorsi è una applicazione scritta in Java per una tesi di laurea triennale in ingegneria informatica. Come base
di progetto è stato utilizzato "Simple App" disponibile su https://vaadin.com/start/.
Il progetto si pone come obiettivo una gestione organizzata di vari corsi di laurea e delle relative informazioni, 
ad esempio gli insegnamenti, la distribuzione delle ore, i vari docenti e i dipartimenti.

## Prerequisiti

Necessario Java 8+ installato per l'esecuzione.
Necessario RDBMS configurato.

## Struttura progetto

Il progetto è stato suddiviso nel seguente modo:

- gestionecorsi: cartella principale del progetto
- gestionecorsi-ui: moduli principali con le varie view
- gestionecorsi-it: moduli di testing
- gestionecorsi-backend: classi generate da JOOQ e di supporto per l'accesso ai dati
- gestionecorsi-dumps: file .sql per la creazione del database

## Esecuzione in IDE

Impostare i parametri per l'accesso al RDBMS in gestione-corsi-backend nei seguenti file:
- DatabaseDataService.class
- pom.xml

Per compilare completamente il progetto utilizzare il comando `install` dalla cartella principale.
Per l'esecuzione utilizzare il comando `jetty:run` dalla cartella gestionecorsi-ui. Questo comando compila anche i file
presenti in gestione-corsi-ui.
Per avviare la fase di testing utilzizare il comando `verify -Pintegration-tests` della cartella gestionecorsi-it.

## Avvio su WebServer

Prima di avviarlo è necessario creare un pacchetto WAR con il comando `package -Pproduction`. Caricare il file creato
nella cartella $JETTY_HOME/webapps. L'indirizzo di default definito dal nome del file WAR (es: $HOST:8080/nomefilewar)

## Stato progetto
- [x] Implementazione funzionalità
- [ ] Integration Testing
- [ ] JavaDoc