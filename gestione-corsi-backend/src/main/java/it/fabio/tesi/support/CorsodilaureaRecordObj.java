package it.fabio.tesi.support;

import static it.fabio.tesi.generated.Tables.CORSODILAUREA;

import it.fabio.tesi.generated.tables.Corsodilaurea;
import it.fabio.tesi.generated.tables.records.CorsodilaureaRecord;
import org.jooq.Record;

public class CorsodilaureaRecordObj {
    CorsodilaureaRecord cdl = new CorsodilaureaRecord();

    public CorsodilaureaRecordObj(Record record){
        this.cdl = record.into(CORSODILAUREA);
    }

    public CorsodilaureaRecordObj(String denominazione, String codice, String anno){
        this.cdl.setDenominazione(denominazione);
        this.cdl.setCodice(codice);
        this.cdl.setAnno(anno);
    }

    public Integer getId(){
        return cdl.getIdcorsodilaurea();
    }
    public String getAnno() { return cdl.getAnno(); }
    public CorsodilaureaRecord getCorsodilaureaRecord() { return this.cdl; }

    @Override
    public String toString(){
        return (this.cdl.getCodice() + " - " + this.cdl.getDenominazione() + " [" + this.cdl.getAnno() + "]");
    }
}
