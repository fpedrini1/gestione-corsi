package it.fabio.tesi.corsi;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import it.fabio.tesi.support.CorsoRecordObj;

import static it.fabio.tesi.utils.GestioneConst.*;

public class CorsiGrid extends Grid<CorsoRecordObj> {

    public CorsiGrid() {
        setSizeFull();

        addColumn(CorsoRecordObj::getCorsiRecordId)
                .setHeader("ID")
                .setFlexGrow(20)
                .setVisible(false);
        addColumn(CorsoRecordObj::getCorsiRecordCodice)
                .setHeader("Codice")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(CorsoRecordObj::getCorsiRecordSsd)
                .setHeader("SSD")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(CorsoRecordObj::getCorsiRecordDenominazione)
                .setHeader("Denominazione")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(CorsoRecordObj::getCorsiRecordAnnoAccademico)
                .setHeader("Anno")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(CorsoRecordObj::getCorsiRecordPeriodo)
                .setHeader("Semestre")
                .setFlexGrow(5)
                .setSortable(true);
        addColumn(CorsoRecordObj::getCorsiRecordCfu)
                .setHeader("CFU")
                .setFlexGrow(5)
                .setSortable(true);
        addColumn(CorsoRecordObj::getCorsiRecordNumStudenti)
                .setHeader("Studenti")
                .setFlexGrow(5)
                .setSortable(true);
        addComponentColumn(item -> creaIcona(item))
                .setHeader("Stato")
                .setVisible(true);
    }

    public void refresh(CorsoRecordObj item) {
        getDataCommunicator().refresh(item);
    }

    public Icon creaIcona(CorsoRecordObj item){
        Icon icona = new Icon(VaadinIcon.CIRCLE);
        String tooltip = null;
        icona.setId("icon-status");
        if(item.getOreRimanenti() == 0) {
            icona.setColor("green");
            tooltip = INFO_TOOLTIP_OK;
        }
        else if(item.getOreRimanenti() > 0) {
            icona.setColor("orange");
            tooltip = INFO_TOOLTIP_WARN;
        }
        else {
            icona.setColor("red");
            tooltip = INFO_TOOLTIP_ERR;
        }
        icona.getElement().setProperty("title", tooltip);
        return icona;
    }
}
