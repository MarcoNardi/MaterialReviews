# MaterialReviews
Material Reviews è un'applicazione per android sviluppata in compose che implementa alcune delle linee guida del material design 3. L'applicazione si occcupa di recensioni di ristoranti e utilizza Compose e Room.

---

## Funzionalità
All'interno dell'app è possibile:
1. Visualizzare una lista di ristoranti con i loro dettagli
2. Aggiungere recensioni ai ristoranti
3. vedere recensioni di altri utenti
4. modificare il proprio profilo

## App e report
Il repository contiene la cartella del progetto di Android Studio + una cartella Latex da cui è possibile aprire il file [Report.pdf](https://github.com/MarcoNardi/MaterialReviews/blob/main/Latex/Report.pdf)

## Prestazioni
A causa dell'utilizzo di Compose è possibile che l'applicazione abbia problemi di performance, per solverli provare a buildare l'app in modalità release e con R8 attivo come indicato [qua](https://developer.android.com/jetpack/compose/performance#build-release).
Il repository è diviso in due branch principali:
1. Main, il quale contiene la versione dell'app che utilizza solo librerie di android, questa versione ha qualche problema di performance causata dal caricamento di Immagini con il composable Image()
2. lazy, questa versione utilizza la libreria [coil](https://github.com/coil-kt/coil) per il caricamento di immagini, questa versione se buildata in modalità release ha zero problemi di performance

Entrambe le versioni hanno le stesse interfacce utente, e funzionalità, differiscono solo per performance

