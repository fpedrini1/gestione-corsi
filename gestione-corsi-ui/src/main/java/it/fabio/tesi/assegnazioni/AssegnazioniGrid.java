package it.fabio.tesi.assegnazioni;

import com.vaadin.flow.component.grid.Grid;
import it.fabio.tesi.support.AssegnazioneRecordObj;

public class AssegnazioniGrid extends Grid<AssegnazioneRecordObj> {

    public AssegnazioniGrid() {
        setSizeFull();

        addColumn(AssegnazioneRecordObj::getDocenteRecordEmail)
                .setHeader("Docente")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(AssegnazioneRecordObj::getAssegnazioneRecordContratto)
                .setHeader("Contratto")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(AssegnazioneRecordObj::getAssegnazioneRecordTipologia)
                .setHeader("Tipologia")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(AssegnazioneRecordObj::getAssegnazioneOre)
                .setHeader("Ore")
                .setFlexGrow(20)
                .setSortable(true);
    }

    public void refresh(AssegnazioneRecordObj item) {
        getDataCommunicator().refresh(item);
    }
}
