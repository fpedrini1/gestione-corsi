package it.fabio.tesi.support;

import it.fabio.tesi.generated.tables.Cdlassegnazione;
import it.fabio.tesi.generated.tables.records.CdlassegnazioneRecord;
import org.jooq.Record;

public class HomeRecordObj extends CorsoRecordObj{
    public CorsodilaureaRecordObj cdl;
    public CdlassegnazioneRecord cdlAssegnazione;

    public HomeRecordObj(Record r){
        super(r);
        this.cdl = new CorsodilaureaRecordObj(r);
        this.cdlAssegnazione = r.into(Cdlassegnazione.CDLASSEGNAZIONE);
    }

    public Integer getAnnoRif(){
        return this.cdlAssegnazione.getAnnorif();
    }


}
