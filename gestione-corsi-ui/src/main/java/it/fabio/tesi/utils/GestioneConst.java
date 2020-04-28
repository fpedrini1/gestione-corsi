package it.fabio.tesi.utils;

public class GestioneConst {
    public static final String APP_NAME = "Gestione Corsi";
    public static final String APP_SHORTNAME = "GestioneCorsi";

    public static final String PAGE_ROOT = "";
    public static final String PAGE_ABOUT = "info";
    public static final String PAGE_DIPARTIMENTI = "dipartimenti";
    public static final String PAGE_DOCENTI = "docenti";
    public static final String PAGE_DOCENTI_DETTAGLI = "docenti/dettagli";
    public static final String PAGE_CORSI = "corsi";
    public static final String PAGE_ASSEGNAZIONI = "assegnazioni";
    public static final String PAGE_CDL = "cdl";

    public static final String TITLE_ABOUT = "Info";
    public static final String TITLE_ROOT = "Home";
    public static final String TITLE_DIPARTIMENTI = "Dipartimenti";
    public static final String TITLE_DOCENTI = "Docenti";
    public static final String TITLE_DOCENTI_DETTAGLI = "Dettagli utente";
    public static final String TITLE_CORSI = "Corsi";
    public static final String TITLE_ASSEGNAZIONI = "Assegnazioni";
    public static final String TITLE_CDL = "Corsi di Laurea";

    public static final String ERROR_NotAvailable = "L'elemento richiesto non è disponibile.";
    public static final String ERROR_NotValid = "Impossibile accedere alla risorsa. Formattazione URL non valida.";
    public static final String ERROR_NotValidCourse = "Corso non selezionato o non più disponibile.";

    public static final String INFO_OreSuperate = "Le ore assegnate superano le ore complessive del corso. " +
            "Modifica le ore complessive del corso e/o delle assegnazioni.";
    public static final String INFO_OreNecessarie = "Questo corso non ha tutte le ore a disposizione assegnate. " +
            "Aggiungi altre assegnazioni o modifica le ore complessive del corso";
    public static final String INFO_TOOLTIP_OK = "Tutte le ore assegnate.";
    public static final String INFO_TOOLTIP_ERR = "Il corso ha superato le ore assegnate.";
    public static final String INFO_TOOLTIP_WARN = "Il corso non ha tutte le ore assegnate.";

    public static final String OK_SALVA = " è stato salvato.";
    public static final String OK_MODIFICA = " è stato modificato.";
    public static final String OK_CANCELLA = " è stato cancellato.";

}
