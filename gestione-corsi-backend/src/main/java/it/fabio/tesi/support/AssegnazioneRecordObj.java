package it.fabio.tesi.support;

import it.fabio.tesi.generated.enums.AssegnazioneContratto;
import it.fabio.tesi.generated.enums.AssegnazioneTipologia;
import it.fabio.tesi.generated.tables.Assegnazione;
import it.fabio.tesi.generated.tables.Corso;
import it.fabio.tesi.generated.tables.Docente;
import it.fabio.tesi.generated.tables.records.*;
import org.jooq.Record;

public class AssegnazioneRecordObj {
    AssegnazioneRecord assegnazioneRecord;
    DocenteRecord docenteRecord;
    CorsoRecord corsoRecord;

    public AssegnazioneRecordObj(Record record){
        this.assegnazioneRecord = record.into(Assegnazione.ASSEGNAZIONE);
        this.docenteRecord = record.into(Docente.DOCENTE);
        this.corsoRecord = record.into(Corso.CORSO);
    }

    public AssegnazioneRecordObj() {
        this.assegnazioneRecord = new AssegnazioneRecord();
        this.docenteRecord = new DocenteRecord();
        this.corsoRecord = new CorsoRecord();
    }

    public AssegnazioneRecord getAssegnazioneRecord() {
        return assegnazioneRecord;
    }

    public DocenteRecord getDocenteRecord() {
        return docenteRecord;
    }
    public void setDocenteRecord(DocenteRecord docenteRecord) {
        this.docenteRecord = docenteRecord;
    }
    public CorsoRecord getCorsoRecord() {
        return corsoRecord;
    }

    public String getDocenteRecordEmail() { return docenteRecord.getEmail(); }
    public AssegnazioneContratto getAssegnazioneRecordContratto() { return assegnazioneRecord.getContratto(); }
    public void setAssegnazioneRecordContratto(AssegnazioneContratto ct) { this.assegnazioneRecord.setContratto(ct);}
    public AssegnazioneTipologia getAssegnazioneRecordTipologia() { return assegnazioneRecord.getTipologia(); }
    public void setAssegnazioneRecordTipologia(AssegnazioneTipologia tp) { this.assegnazioneRecord.setTipologia(tp);}
    public void setAssegnazioneId(int id) { this.assegnazioneRecord.setIdassegnazione(id); }
    public void setAssegnazioneOre(int ore) { this.assegnazioneRecord.setOre(ore);}
    public Integer getAssegnazioneOre() { return this.assegnazioneRecord.getOre();}
}
