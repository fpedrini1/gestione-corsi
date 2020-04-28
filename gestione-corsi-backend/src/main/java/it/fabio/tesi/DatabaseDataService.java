package it.fabio.tesi;

import it.fabio.tesi.generated.tables.records.CorsoRecord;
import it.fabio.tesi.generated.tables.records.CorsodilaureaRecord;
import it.fabio.tesi.generated.tables.records.DipartimentoRecord;
import it.fabio.tesi.generated.tables.records.DocenteRecord;
import it.fabio.tesi.support.AssegnazioneRecordObj;
import it.fabio.tesi.support.CorsoRecordObj;
import it.fabio.tesi.support.CorsodilaureaRecordObj;
import it.fabio.tesi.support.HomeRecordObj;
import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import static it.fabio.tesi.generated.Tables.*;
import static it.fabio.tesi.generated.tables.Corso.CORSO;
import static org.jooq.impl.DSL.*;

public class DatabaseDataService {
    private String username = "USERNAME";
    private String password = "PASSWORD";
    private final String url = "jdbc:mysql://URL:3306/gestionecorsi?serverTimezone=UTC";
    private Table<?> oreTotali;

    public DatabaseDataService() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
           throw new ClassNotFoundException("Driver SQL non trovato.");
        }
        impostaTabelle();
    }

    private void impostaTabelle(){
        this.oreTotali = DSL.select(ASSEGNAZIONE.CORSO_IDCORSO.as("idcorsoJoin"),
                (sum(ASSEGNAZIONE.ORE).as("oreTotaliPerCorso")),
                count().as("assegnazioniTotaliPerCorso"),
                groupConcatDistinct(concat(DOCENTE.COGNOME, val(" "), DOCENTE.NOME)).as("docentiCorso"))
                .from(ASSEGNAZIONE)
                .leftJoin(DOCENTE).on(ASSEGNAZIONE.DOCENTE_IDDOCENTE.eq(DOCENTE.IDDOCENTE))
                .groupBy(ASSEGNAZIONE.CORSO_IDCORSO)
                .asTable("oreTotaliCorso");
    }

    //region Metodi Dipartimenti
    public ArrayList<DipartimentoRecord> getAllDipartimenti() {
        ArrayList<DipartimentoRecord> listaDipartimenti = new ArrayList <> ();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(DIPARTIMENTO).fetch();
            for (Record r: result) {
                listaDipartimenti.add(r.into(DIPARTIMENTO));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaDipartimenti;
    }

    public DipartimentoRecord getDipartimentoById(int id) {
        DipartimentoRecord dipartimento = new DipartimentoRecord();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            dipartimento = create.selectFrom(DIPARTIMENTO).where(DIPARTIMENTO.IDDIPARTIMENTO.eq(id)).fetchAny();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dipartimento;
    }

    public ArrayList<DipartimentoRecord> getAllDipartimentiByText(String str) {
        ArrayList<DipartimentoRecord> listaDipartimenti = new ArrayList <> ();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(DIPARTIMENTO)
                    .where((DIPARTIMENTO.DESCRIZIONE
                            .likeIgnoreCase("%" + str +"%"))
                            .or(DIPARTIMENTO.DENOMINAZIONE
                                    .likeIgnoreCase("%" + str +"%"))).fetch();
            for (Record r: result) {
                listaDipartimenti.add(r.into(DIPARTIMENTO));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDipartimenti;
    }

    public void insert(@NotNull DipartimentoRecord dipartimento) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            dipartimento.setIddipartimento(null);
            create.insertInto(DIPARTIMENTO).set(dipartimento).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(@NotNull DipartimentoRecord dipartimento){
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            create.update(DIPARTIMENTO).set(dipartimento).where(DIPARTIMENTO.IDDIPARTIMENTO.eq(dipartimento.getIddipartimento())).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(@NotNull DipartimentoRecord dipartimento){
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            create.delete(DIPARTIMENTO)
                    .where(DIPARTIMENTO.IDDIPARTIMENTO.eq(dipartimento.getIddipartimento()))
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //endregion
    //region Metodi Docenti
    public ArrayList<DocenteRecord> getAllDocenti() {
        ArrayList<DocenteRecord> listaDocenti = new ArrayList <> ();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(DOCENTE).fetch();
            for (Record r: result) {
                listaDocenti.add(r.into(DOCENTE));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDocenti;
    }

    public DocenteRecord getDocenteById(int id) {
        DocenteRecord docente = new DocenteRecord();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            docente = create.selectFrom(DOCENTE).where(DOCENTE.IDDOCENTE.eq(id)).fetchAny();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return docente;
    }

    public ArrayList<DocenteRecord> getAllDocentiByText(String str) {
        ArrayList<DocenteRecord> listaDocenti = new ArrayList <> ();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(DOCENTE)
                    .where((DOCENTE.EMAIL.likeIgnoreCase("%" + str +"%"))
                            .or(DOCENTE.COGNOME.likeIgnoreCase("%" + str +"%"))).fetch();
            for (Record r: result) {
                listaDocenti.add(r.into(DOCENTE));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDocenti;
    }

    public ArrayList<DocenteRecord> getAllDocentiByDip(@NotNull DipartimentoRecord dip) {
        ArrayList< DocenteRecord > listaDocenti = new ArrayList <> ();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(DOCENTE)
                    .where(DOCENTE.DIPARTIMENTO_IDDIPARTIMENTO.eq(dip.getIddipartimento())).fetch();
            for (Record r: result) {
                listaDocenti.add(r.into(DOCENTE));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDocenti;
    }

    public ArrayList<DocenteRecord> getAllDocentiByDipAndText(@NotNull DipartimentoRecord dip, String str) {
        ArrayList< DocenteRecord > listaDocenti = new ArrayList <> ();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(DOCENTE)
                    .where(DOCENTE.DIPARTIMENTO_IDDIPARTIMENTO.eq(dip.getIddipartimento())
                            .and((DOCENTE.EMAIL.likeIgnoreCase("%" + str +"%"))
                                    .or(DOCENTE.COGNOME.likeIgnoreCase("%" + str +"%")))).fetch();
            for (Record r: result) {
                listaDocenti.add(r.into(DOCENTE));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDocenti;
    }

    public void insert(@NotNull DocenteRecord docente) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            docente.setIddocente(null);
            create.insertInto(DOCENTE).set(docente).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(@NotNull DocenteRecord docente){
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            create.update(DOCENTE).set(docente).where(DOCENTE.IDDOCENTE.eq(docente.getIddocente())).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(@NotNull DocenteRecord docente){
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            create.delete(DOCENTE)
                    .where(DOCENTE.IDDOCENTE.eq(docente.getIddocente()))
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //endregion
    //region Metodi Corsi
    public ArrayList<CorsoRecord> getAllCorsi() {
        ArrayList< CorsoRecord > listaCorsi = new ArrayList <> ();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(CORSO).fetch();
            for (Record r: result) {
                listaCorsi.add(r.into(CORSO));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCorsi;
    }

    public ArrayList<CorsoRecordObj> getAllCorsiObj() {
        ArrayList< CorsoRecordObj > listaCorsi = new ArrayList <> ();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

            Result<Record> result = create.select()
                    .from(CORSO).leftJoin(oreTotali)
                    .on(CORSO.IDCORSO.eq((Field<Integer>) oreTotali.field("idcorsoJoin"))).fetch();

            for (Record r: result) {
                listaCorsi.add(new CorsoRecordObj(r));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCorsi;
    }

    public ArrayList<HomeRecordObj> getAllCorsiByCdl(CorsodilaureaRecordObj cdl) {
        ArrayList< HomeRecordObj > listaCorsi = new ArrayList <> ();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select()
                    .from(CORSO).leftJoin(oreTotali)
                    .on(CORSO.IDCORSO.eq((Field<Integer>) oreTotali.field("idcorsoJoin")))
                    .leftJoin(CDLASSEGNAZIONE).on(CDLASSEGNAZIONE.CORSO_IDCORSO.eq(CORSO.IDCORSO))
                    .where(CDLASSEGNAZIONE.CORSODILAUREA_IDCORSODILAUREA.eq(cdl.getId()))
                    .orderBy(CDLASSEGNAZIONE.ANNORIF.asc(), CORSO.PERIODO.asc())
                    .fetch();
            for (Record r: result) {
                listaCorsi.add(new HomeRecordObj(r));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCorsi;
    }

    public CorsoRecord getCorsoById(int id) {
        CorsoRecord corso = new CorsoRecord();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(CORSO).where(CORSO.IDCORSO.eq(id)).fetch();
            for (Record r: result) {
                corso = r.into(CORSO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return corso;
    }

    public ArrayList<CorsoRecord> getCorsiByAnno(String anno) {
        ArrayList<CorsoRecord> listaCorsi = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(CORSO)
                    .where(CORSO.ANNO.eq(anno)).fetch();
            for (Record r: result) {
                listaCorsi.add(r.into(CORSO));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCorsi;
    }

    public ArrayList<CorsoRecordObj> getAllCorsiByText(String str) {
        ArrayList< CorsoRecordObj > listaCorsi = new ArrayList <> ();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(CORSO)
                    .leftJoin(oreTotali)
                    .on(CORSO.IDCORSO.eq((Field<Integer>) oreTotali.field("idcorsoJoin")))
                    .where((CORSO.CODICE.likeIgnoreCase("%" + str +"%")).or(CORSO.DENOMINAZIONE.likeIgnoreCase("%" + str +"%"))).fetch();
            for (Record r: result) {
                listaCorsi.add(new CorsoRecordObj(r));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCorsi;
    }

    public ArrayList<CorsoRecordObj> getAllCorsiByDip(DipartimentoRecord dip) {
        ArrayList< CorsoRecordObj > listaCorsi = new ArrayList <> ();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(CORSO)
                    .leftJoin(oreTotali)
                    .on(CORSO.IDCORSO.eq((Field<Integer>) oreTotali.field("idcorsoJoin")))
                    .where(CORSO.DIPARTIMENTO_IDDIPARTIMENTO.eq(dip.getIddipartimento())).fetch();
            for (Record r: result) {
                listaCorsi.add(new CorsoRecordObj(r));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCorsi;
    }

    public ArrayList<CorsoRecordObj> getAllCorsiByDipAndText(DipartimentoRecord dip, String str) {
        ArrayList<CorsoRecordObj> listaCorsi = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(CORSO)
                    .leftJoin(oreTotali)
                    .on(CORSO.IDCORSO.eq((Field<Integer>) oreTotali.field("idcorsoJoin")))
                    .where(CORSO.DIPARTIMENTO_IDDIPARTIMENTO.eq(dip.getIddipartimento()).and((CORSO.CODICE.likeIgnoreCase("%" + str +"%")).or(CORSO.DENOMINAZIONE.likeIgnoreCase("%" + str +"%")))).fetch();
            for (Record r: result) {
                listaCorsi.add(new CorsoRecordObj(r));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCorsi;
    }

    public void insert(CorsoRecordObj corso) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            create.insertInto(CORSO,
                    CORSO.CODICE, CORSO.SSD, CORSO.DENOMINAZIONE, CORSO.CODICEMUTUAZIONE,
                    CORSO.ORETOTALI, CORSO.CFU, CORSO.NUMSTUDENTI, CORSO.PARAMETRO,
                    CORSO.ANNO, CORSO.PERIODO, CORSO.DIPARTIMENTO_IDDIPARTIMENTO)
                    .values(corso.getCorsiRecordCodice(), corso.getCorsiRecordSsd(), corso.getCorsiRecordDenominazione(), corso.getCorsiRecordCodiceMutuazione(),
                            corso.getCorsiRecordOre(), corso.getCorsiRecordCfu(), corso.getCorsiRecordNumStudenti(), corso.getCorsiRecordParametro(),
                            corso.getCorsiRecordAnnoAccademico(), corso.getCorsiRecordPeriodo(), corso.getDipartimentoRecord().getIddipartimento())
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(CorsoRecordObj corso){
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            create.update(CORSO)
                    .set(CORSO.CODICE, corso.getCorsiRecordCodice())
                    .set(CORSO.SSD, corso.getCorsiRecordSsd())
                    .set(CORSO.DENOMINAZIONE, corso.getCorsiRecordDenominazione())
                    .set(CORSO.CODICEMUTUAZIONE, corso.getCorsiRecordCodiceMutuazione())
                    .set(CORSO.CFU, corso.getCorsiRecordCfu())
                    .set(CORSO.ORETOTALI, corso.getCorsiRecordOre())
                    .set(CORSO.NUMSTUDENTI, corso.getCorsiRecordNumStudenti())
                    .set(CORSO.ANNO, corso.getCorsiRecordAnnoAccademico())
                    .set(CORSO.PERIODO, corso.getCorsiRecordPeriodo())
                    .set(CORSO.PARAMETRO, corso.getCorsiRecordParametro())
                    .set(CORSO.DIPARTIMENTO_IDDIPARTIMENTO, corso.getCorsoRecord().getDipartimentoIddipartimento())
                    .where(CORSO.IDCORSO.eq(corso.getCorsiRecordId()))
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void delete(CorsoRecordObj corso){
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            create.delete(CORSO)
                    .where(CORSO.IDCORSO.eq(corso.getCorsiRecordId()))
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //endregion
    //region Metodi Assegnazioni
    public ArrayList<AssegnazioneRecordObj> getAllAssegnazioniByIdCorso(int idCorso) {
        ArrayList< AssegnazioneRecordObj > listaAssegnazioni = new ArrayList <> ();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(ASSEGNAZIONE)
                    .join(DOCENTE).on(ASSEGNAZIONE.DOCENTE_IDDOCENTE.eq(DOCENTE.IDDOCENTE))
                    .join(CORSO).on(ASSEGNAZIONE.CORSO_IDCORSO.eq(CORSO.IDCORSO))
                    .where(ASSEGNAZIONE.CORSO_IDCORSO.eq(idCorso)).fetch();
            for (Record r: result) {
                listaAssegnazioni.add(new AssegnazioneRecordObj(r));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaAssegnazioni;
    }

    public ArrayList<AssegnazioneRecordObj> getAllAssegnazioniByDocente(DocenteRecord docente) {
        ArrayList< AssegnazioneRecordObj > listaAssegnazioni = new ArrayList <> ();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(ASSEGNAZIONE)
                    .join(DOCENTE).on(ASSEGNAZIONE.DOCENTE_IDDOCENTE.eq(DOCENTE.IDDOCENTE))
                    .join(CORSO).on(ASSEGNAZIONE.CORSO_IDCORSO.eq(CORSO.IDCORSO))
                    .where(ASSEGNAZIONE.DOCENTE_IDDOCENTE.eq(docente.getIddocente()))
                    .orderBy(CORSO.ANNO.desc())
                    .fetch();
            for (Record r: result) {
                listaAssegnazioni.add(new AssegnazioneRecordObj(r));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaAssegnazioni;
    }

    public AssegnazioneRecordObj getAssegnazioneById(int id) {
        AssegnazioneRecordObj corso = new AssegnazioneRecordObj();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(ASSEGNAZIONE).where(ASSEGNAZIONE.IDASSEGNAZIONE.eq(id)).fetch();
            for (Record r: result) {
                corso = new AssegnazioneRecordObj(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return corso;
    }

    public void insert(AssegnazioneRecordObj assegnazione) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            create.insertInto(ASSEGNAZIONE,
                    ASSEGNAZIONE.DOCENTE_IDDOCENTE, ASSEGNAZIONE.CORSO_IDCORSO, ASSEGNAZIONE.CONTRATTO,
                    ASSEGNAZIONE.TIPOLOGIA, ASSEGNAZIONE.ORE)
                    .values(assegnazione.getDocenteRecord().getIddocente(), assegnazione.getCorsoRecord().getIdcorso(), assegnazione.getAssegnazioneRecordContratto(),
                            assegnazione.getAssegnazioneRecordTipologia(), assegnazione.getAssegnazioneOre())
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(AssegnazioneRecordObj assegnazione){
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            create.update(ASSEGNAZIONE)
                    .set(ASSEGNAZIONE.DOCENTE_IDDOCENTE, assegnazione.getDocenteRecord().getIddocente())
                    .set(ASSEGNAZIONE.CONTRATTO, assegnazione.getAssegnazioneRecordContratto())
                    .set(ASSEGNAZIONE.TIPOLOGIA, assegnazione.getAssegnazioneRecordTipologia())
                    .set(ASSEGNAZIONE.ORE, assegnazione.getAssegnazioneOre())
                    .where(ASSEGNAZIONE.IDASSEGNAZIONE.eq(assegnazione.getAssegnazioneRecord().getIdassegnazione()))
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void delete(AssegnazioneRecordObj assegnazione){
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            create.delete(ASSEGNAZIONE)
                    .where(ASSEGNAZIONE.IDASSEGNAZIONE.eq(assegnazione.getAssegnazioneRecord().getIdassegnazione()))
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //endregion
    //region Metodi CdL e Assegnazioni
    public ArrayList<CorsodilaureaRecordObj> getAllCdl() {
        ArrayList< CorsodilaureaRecordObj > listaCdl = new ArrayList <> ();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(CORSODILAUREA).fetch();
            for (Record r: result) {
                listaCdl.add(new CorsodilaureaRecordObj(r));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCdl;
    }

    public void insert(CorsodilaureaRecord cdl) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            create.insertInto(CORSODILAUREA,
                    CORSODILAUREA.DENOMINAZIONE, CORSODILAUREA.CODICE, CORSODILAUREA.ANNO)
                    .values(cdl.getDenominazione(), cdl.getCodice(), cdl.getAnno())
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(CorsoRecord corso, int idCdl, int annoRif) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            create.insertInto(CDLASSEGNAZIONE, CDLASSEGNAZIONE.CORSO_IDCORSO, CDLASSEGNAZIONE.CORSODILAUREA_IDCORSODILAUREA,
                    CDLASSEGNAZIONE.ANNORIF)
                    .values(corso.getIdcorso(), idCdl, annoRif)
                    .execute();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException(e);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getCause().toString());
        }
    }

    public ArrayList<CorsoRecord> getCorsiAttivi(Integer cldId, Integer annoRif, String anno){
        ArrayList<CorsoRecord> listaCorsi = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(CORSO)
                    .join(CDLASSEGNAZIONE).on(CDLASSEGNAZIONE.CORSO_IDCORSO.eq(CORSO.IDCORSO))
                    .where(CORSO.ANNO.eq(anno)
                            .and(CDLASSEGNAZIONE.CORSODILAUREA_IDCORSODILAUREA.eq(cldId))
                            .and(CDLASSEGNAZIONE.ANNORIF.eq(annoRif)))
                    .fetch();
            for (Record r: result) {
                listaCorsi.add(r.into(CORSO));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCorsi;
    }

    public void delete(CorsodilaureaRecord cdl){
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            create.delete(CORSODILAUREA)
                    .where(CORSODILAUREA.IDCORSODILAUREA.eq(cdl.getIdcorsodilaurea()))
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(CorsoRecord corso, int idCdl, int annoRif){
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            create.delete(CDLASSEGNAZIONE)
                    .where(CDLASSEGNAZIONE.CORSO_IDCORSO.eq(corso.getIdcorso())
                            .and(CDLASSEGNAZIONE.ANNORIF.eq(annoRif))
                            .and(CDLASSEGNAZIONE.CORSODILAUREA_IDCORSODILAUREA.eq(idCdl)))
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //endregion
}
