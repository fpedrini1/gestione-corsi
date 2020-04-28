package it.fabio.tesi.home;

import com.vaadin.flow.component.grid.Grid;
import it.fabio.tesi.support.HomeRecordObj;

public class HomeGrid extends Grid<HomeRecordObj> {

    public HomeGrid() {
        setSizeFull();

        addColumn(HomeRecordObj::getAnnoRif)
                .setHeader("Anno")
                .setFlexGrow(5)
                .setSortable(true);
        addColumn(HomeRecordObj::getCorsiRecordPeriodo)
                .setHeader("Periodo")
                .setFlexGrow(5)
                .setSortable(true);
        addColumn(HomeRecordObj::getCorsiRecordDenominazione)
                .setHeader("Denominazione")
                .setFlexGrow(30)
                .setSortable(true);
        addColumn(HomeRecordObj::getDocentiCorso)
                .setHeader("Docenti")
                .setFlexGrow(30)
                .setSortable(true);
        addColumn(HomeRecordObj::getCorsiRecordSsd)
                .setHeader("SSD")
                .setFlexGrow(5)
                .setSortable(true);
        addColumn(HomeRecordObj::getCorsiRecordCfu)
                .setHeader("CFU")
                .setFlexGrow(5)
                .setSortable(true);
    }

    public void refresh(HomeRecordObj item) {
        getDataCommunicator().refresh(item);
    }
}
