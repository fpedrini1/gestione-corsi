/*
 * This file is generated by jOOQ.
 */
package it.fabio.tesi.generated;


import it.fabio.tesi.generated.tables.Assegnazione;
import it.fabio.tesi.generated.tables.Cdlassegnazione;
import it.fabio.tesi.generated.tables.Corso;
import it.fabio.tesi.generated.tables.Corsodilaurea;
import it.fabio.tesi.generated.tables.Dipartimento;
import it.fabio.tesi.generated.tables.Docente;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in gestionecorsi
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>gestionecorsi.assegnazione</code>.
     */
    public static final Assegnazione ASSEGNAZIONE = Assegnazione.ASSEGNAZIONE;

    /**
     * The table <code>gestionecorsi.cdlassegnazione</code>.
     */
    public static final Cdlassegnazione CDLASSEGNAZIONE = Cdlassegnazione.CDLASSEGNAZIONE;

    /**
     * The table <code>gestionecorsi.corso</code>.
     */
    public static final Corso CORSO = Corso.CORSO;

    /**
     * The table <code>gestionecorsi.corsodilaurea</code>.
     */
    public static final Corsodilaurea CORSODILAUREA = Corsodilaurea.CORSODILAUREA;

    /**
     * The table <code>gestionecorsi.dipartimento</code>.
     */
    public static final Dipartimento DIPARTIMENTO = Dipartimento.DIPARTIMENTO;

    /**
     * The table <code>gestionecorsi.docente</code>.
     */
    public static final Docente DOCENTE = Docente.DOCENTE;
}
