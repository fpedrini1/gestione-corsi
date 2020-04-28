package it.fabio.tesi.dipartimenti;

import com.vaadin.flow.component.UI;
import it.fabio.tesi.DatabaseDataService;
import it.fabio.tesi.LogicInterface;
import it.fabio.tesi.generated.tables.records.DipartimentoRecord;

import static it.fabio.tesi.utils.GestioneConst.*;

public class DipartimentiLogic implements LogicInterface {
    private DipartimentiView view;
    private DatabaseDataService service;

    public DipartimentiLogic(DipartimentiView view, DatabaseDataService service){
        this.view = view;
        this.service = service;
    }

    public void creaModifica(DipartimentoRecord item){
        if(item == null){
            setURI("");
        } else {
            setURI(item.getIddipartimento().toString());
        }
        view.abilitaModifica(item);
    }
    public void creaNuovo(){
        setURI("new");
        DipartimentoRecord newDipartimento = new DipartimentoRecord();
        newDipartimento.setIddipartimento(-1);
        view.abilitaModifica(newDipartimento);
    }

    public void creaSalva(DipartimentoRecord item){
        try {
            boolean nuovo = true;
            if (item.getIddipartimento() == -1)
                nuovo = true;
            else
                nuovo = false;
            if (nuovo)
                view.salvaService(item);
            else
                view.aggiornaService(item);
            setURI("");
            view.creaNotifica(item.getDenominazione() + (nuovo ? OK_SALVA : OK_MODIFICA), false);
        }catch(Exception e){
        }
    }

    public void creaElimina(DipartimentoRecord item){
        view.eliminaService(item);
        view.creaNotifica(item.getDenominazione() + OK_CANCELLA, false);
        setURI("");
    }

    public void annullaOperazione(){
        view.rimuoviSelezione();
        setURI("");
    }

    @Override
    public void enter(String parameter) {
        if (parameter != null && !parameter.isEmpty()) {
            if (parameter.equals("new")) {
                creaNuovo();
            } else {
                try {
                    int intId = Integer.parseInt(parameter);
                    DipartimentoRecord item = service.getDipartimentoById(intId);
                    if (item != null) {
                        view.impostaSelezione(item);
                        view.mostraForm(true);
                    } else {
                        view.creaNotifica(ERROR_NotAvailable, true);
                    }
                } catch (NumberFormatException e) {
                    view.creaNotifica(ERROR_NotValid, true);
                }
            }
        }
    }

    @Override
    public void setURI(String parameter) {
        String fragmentParameter = "";
        if(parameter == null || parameter.isEmpty()) {
            fragmentParameter = "";
        }
        else
            fragmentParameter = parameter;
        UI.getCurrent().navigate(DipartimentiView.class, fragmentParameter);
    }
}
