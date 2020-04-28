package it.fabio.tesi.support;

import it.fabio.tesi.generated.tables.Corso;
import it.fabio.tesi.generated.tables.records.CorsoRecord;
import it.fabio.tesi.generated.tables.records.DipartimentoRecord;
import org.jooq.Record;

public class CorsoRecordObj {
    CorsoRecord corsoRecord;
    DipartimentoRecord dipartimentoRecord;
    int oreAssegnate = 0;
    int totaleAssegnazioni = 0;
    String docentiAssegnazioni = "";

    public CorsoRecordObj(CorsoRecord record) {
        this.corsoRecord = record.into(Corso.CORSO);
    }
    public CorsoRecordObj(Record record) {
        corsoRecord = record.into(Corso.CORSO);
        try {
            if (record.get("oreTotaliPerCorso") != null)
                oreAssegnate = record.getValue("oreTotaliPerCorso", Integer.class);
            if (record.get("assegnazioniTotaliPerCorso") != null)
                totaleAssegnazioni = record.getValue("assegnazioniTotaliPerCorso", Integer.class);
            if (record.get("docentiCorso") != null)
                docentiAssegnazioni = record.getValue("docentiCorso", String.class);
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }
    public CorsoRecordObj(){
        this.corsoRecord = new CorsoRecord();
        this.dipartimentoRecord = new DipartimentoRecord();
    }

    public CorsoRecord getCorsoRecord() {
        return corsoRecord;
    }
    public DipartimentoRecord getDipartimentoRecord(){ return this.dipartimentoRecord;}
    public Integer getCorsiRecordId(){
        return corsoRecord.getIdcorso();
    }
    public void setCorsiRecordId(int id){
        this.corsoRecord.setIdcorso(id);
    }
    public String getCorsiRecordCodice(){
        return corsoRecord.getCodice();
    }
    public void setCorsiRecordCodice(String codice){
        this.corsoRecord.setCodice(codice);
    }
    public String getCorsiRecordSsd(){
        return corsoRecord.getSsd();
    }
    public void setCorsiRecordSsd(String ssd){
        this.corsoRecord.setSsd(ssd);
    }
    public String getCorsiRecordCodiceMutuazione(){
        return corsoRecord.getCodicemutuazione();
    }
    public void setCorsiRecordCodiceMutuazione(String cm){
        this.corsoRecord.setCodicemutuazione(cm);
    }
    public String getCorsiRecordDenominazione(){
        return corsoRecord.getDenominazione();
    }
    public void setCorsiRecordDenominazione(String d){
        this.corsoRecord.setDenominazione(d);
    }
    public String getCorsiRecordParametro(){
        return corsoRecord.getParametro();
    }
    public void setCorsiRecordParametro(String p){
        this.corsoRecord.setParametro(p);
    }
    public Integer getCorsiRecordOre(){
        return corsoRecord.getOretotali();
    }
    public void setCorsiRecordOre(int ore){
        this.corsoRecord.setOretotali(ore);
    }
    public Integer getCorsiRecordPeriodo(){
        return corsoRecord.getPeriodo();
    }
    public void setCorsiRecordPeriodo(int periodo){
        this.corsoRecord.setPeriodo(periodo);
    }
    public Integer getCorsiRecordNumStudenti(){
        return corsoRecord.getNumstudenti();
    }
    public void setCorsiRecordNumStudenti(int numS){
        this.corsoRecord.setNumstudenti(numS);
    }
    public Integer getCorsiRecordCfu(){
        return corsoRecord.getCfu();
    }
    public void setCorsiRecordCfu(int cfu){
        this.corsoRecord.setCfu(cfu);
    }
    public String getCorsiRecordAnnoAccademico(){
        return corsoRecord.getAnno();
    }
    public void setCorsiRecordAnnoAccademico(String anno){
        this.corsoRecord.setAnno(anno);
    }
    public Integer getOreRimanenti(){
        return this.getCorsiRecordOre() - this.oreAssegnate;
    }
    public String getDocentiCorso() {
        if(this.docentiAssegnazioni.length()>0)
            return this.docentiAssegnazioni;
        else
            return "Non Definito";
    }
}
