package it.fabio.tesi.docenti;

import com.vaadin.flow.component.UI;
import it.fabio.tesi.DatabaseDataService;
import it.fabio.tesi.LogicInterface;
import it.fabio.tesi.generated.tables.records.DocenteRecord;

import static it.fabio.tesi.utils.GestioneConst.*;

public class DocentiLogic implements LogicInterface {
    private DocentiView view;
    DatabaseDataService service;

    public DocentiLogic(DocentiView view, DatabaseDataService service){
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
                    DocenteRecord item = trova(intId);
                    if(item.getIddocente() != null) {
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

    private DocenteRecord trova(int id){
        return service.getDocenteById(id);
    }

    public void creaSalva(DocenteRecord item){
        try {
            boolean nuovo = true;
            if (item.getIddocente() != -1)
                nuovo = false;
            view.rimuoviSelezione();
            if (nuovo)
                view.salvaService(item);
            else
                view.aggiornaService(item);
            setURI("");
            view.creaNotifica(item.getEmail() + (nuovo ? OK_SALVA : OK_MODIFICA), false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void creaElimina(DocenteRecord item){
        view.rimuoviSelezione();
        view.eliminaService(item);
        view.creaNotifica(item.getEmail() + OK_CANCELLA, false);
        setURI("");
    }

    public void creaModifica(DocenteRecord item) {
        if (item == null) {
            setURI("");
        } else {
            setURI(item.getIddocente() + "");
        }
        view.abilitaModifica(item);
    }

    public void creaNuovo(){
        view.rimuoviSelezione();
        setURI("new");
        DocenteRecord newDocente = new DocenteRecord();
        newDocente.setIddocente(-1);
        view.abilitaModifica(newDocente);
    }

    public void setURI(String id) {
        String fragmentParameter = "";
        if(id == null || id.isEmpty()) {
            fragmentParameter = "";
        }
        else
            fragmentParameter = id;
        UI.getCurrent().navigate(DocentiView.class, fragmentParameter);
    }


}