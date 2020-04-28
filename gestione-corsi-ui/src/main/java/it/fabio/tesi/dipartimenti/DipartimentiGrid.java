package it.fabio.tesi.dipartimenti;

import com.vaadin.flow.component.grid.Grid;
import it.fabio.tesi.generated.tables.records.DipartimentoRecord;

public class DipartimentiGrid extends Grid<DipartimentoRecord> {

    public DipartimentiGrid() {
        setSizeFull();

        addColumn(DipartimentoRecord::getIddipartimento)
                .setHeader("ID")
                .setFlexGrow(20)
                .setVisible(false);
        addColumn(DipartimentoRecord::getDenominazione)
                .setHeader("Denominazione")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(DipartimentoRecord::getDescrizione)
                .setHeader("Descrizione")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(DipartimentoRecord::getPaese)
                .setHeader("Paese")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(DipartimentoRecord::getIndirizzo)
                .setHeader("Indirizzo")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(DipartimentoRecord::getTelefono)
                .setHeader("Telefono")
                .setFlexGrow(20)
                .setSortable(true);
    }

    public void refresh(DipartimentoRecord item) {
        getDataCommunicator().refresh(item);
    }
}
