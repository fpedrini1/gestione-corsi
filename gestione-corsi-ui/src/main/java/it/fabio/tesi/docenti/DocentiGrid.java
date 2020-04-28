package it.fabio.tesi.docenti;

import com.vaadin.flow.component.grid.Grid;
import it.fabio.tesi.generated.tables.records.DipartimentoRecord;
import it.fabio.tesi.generated.tables.records.DocenteRecord;

public class DocentiGrid extends Grid<DocenteRecord> {

    public DocentiGrid() {
        setSizeFull();

        addColumn(DocenteRecord::getIddocente)
                .setHeader("ID")
                .setFlexGrow(20)
                .setVisible(false);
        addColumn(DocenteRecord::getEmail)
                .setHeader("E-Mail")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(DocenteRecord::getNome)
                .setHeader("Nome")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(DocenteRecord::getCognome)
                .setHeader("Cognome")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(DocenteRecord::getTelefono)
                .setHeader("Telefono")
                .setFlexGrow(20)
                .setSortable(true);
        addColumn(DocenteRecord::getDipartimentoIddipartimento)
                .setHeader("Dipartimento")
                .setFlexGrow(20)
                .setVisible(false);
    }

    public void refresh(DocenteRecord item) {
        getDataCommunicator().refresh(item);
    }
}
