package it.fabio.tesi.corsi;

import com.vaadin.flow.component.UI;
import it.fabio.tesi.DatabaseDataService;
import it.fabio.tesi.LogicInterface;
import it.fabio.tesi.support.CorsoRecordObj;

import static it.fabio.tesi.utils.GestioneConst.*;

public class CorsiLogic implements LogicInterface {
    private CorsiView view;
    DatabaseDataService service;

    public CorsiLogic(CorsiView view, DatabaseDataService service){
        this.view = view;
        this.service = service;
    }

    public void annullaOperazione(){
        view.rimuoviSelezione();
        setURI("");
    }

    public void enter(String id) {
        if (id != null && !id.isEmpty()) {
            if (id.equals("new")) {
                creaNuovo();
            } else {
                try {
                    int intId = Integer.parseInt(id);
                    CorsoRecordObj item = new CorsoRecordObj(service.getCorsoById(intId));
                    if(item.getCorsiRecordId() != null) {
                        view.impostaSelezione(item);
                        view.mostraForm(true);
                    }
                    else{
                        view.creaNotifica(ERROR_NotAvailable, true);
                    }
                } catch (NumberFormatException e) {
                    view.creaNotifica(ERROR_NotValid, true);
                }
            }
        }
    }

    public void creaSalva(CorsoRecordObj item){
        try {
            boolean nuovo = true;
            if (item.getCorsiRecordId() != -1)
                nuovo = false;
            view.rimuoviSelezione();
            if (nuovo)
                view.salvaService(item);
            else
                view.aggiornaService(item);
            setURI("");
            view.creaNotifica(item.getCorsiRecordDenominazione() + (nuovo ? OK_SALVA : OK_MODIFICA), false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void creaElimina(CorsoRecordObj item){
        view.rimuoviSelezione();
        view.eliminaService(item);
        view.creaNotifica(item.getCorsiRecordDenominazione() + OK_CANCELLA, false);
        setURI("");
    }

    public void creaModifica(CorsoRecordObj item) {
        if (item == null) {
            setURI("");
        } else {
            setURI(item.getCorsiRecordId() + "");
        }
        view.abilitaModifica(item);
    }

    public void creaNuovo(){
        view.rimuoviSelezione();
        setURI("new");
        CorsoRecordObj newCorso = new CorsoRecordObj();
        newCorso.setCorsiRecordId(-1);
        view.abilitaModifica(newCorso);
    }

    public void setURI(String id) {
        String fragmentParameter = "";
        if(id == null || id.isEmpty()) {
            fragmentParameter = "";
        }
        else
            fragmentParameter = id;
        UI.getCurrent().navigate(CorsiView.class, fragmentParameter);
    }


}