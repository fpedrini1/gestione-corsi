package it.fabio.tesi.assegnazioni;

import com.vaadin.flow.component.UI;
import it.fabio.tesi.DatabaseDataService;
import it.fabio.tesi.support.AssegnazioneRecordObj;

import static it.fabio.tesi.utils.GestioneConst.*;

public class AssegnazioniLogic {
    private AssegnazioniView view;
    private DatabaseDataService service;
    private Integer courseId;

    public AssegnazioniLogic(AssegnazioniView view, DatabaseDataService service, int courseId){
        this.view = view;
        this.service = service;
        this.courseId = courseId;
    }

    public void creaModifica(AssegnazioneRecordObj item){
        if(item == null){
            setURI("");
        } else {
            setURI(item.getAssegnazioneRecord().getIdassegnazione().toString());
        }
        view.abilitaModifica(item);
    }
    public void creaNuovo(int courseId){
        setURI(courseId, "new");
        AssegnazioneRecordObj newAssegnazione = new AssegnazioneRecordObj();
        newAssegnazione.setAssegnazioneId(-1);
        view.abilitaModifica(newAssegnazione);
    }

    public void creaSalva(AssegnazioneRecordObj item){
        try {
            boolean nuovo = true;
            if (item.getAssegnazioneRecord().getIdassegnazione() == -1)
                nuovo = true;
            else
                nuovo = false;
            if (nuovo)
                view.salvaService(item);
            else
                view.aggiornaService(item);
            setURI("");
            view.creaNotifica(item.getAssegnazioneRecordTipologia() + (nuovo ? OK_SALVA : OK_MODIFICA), false);
        }catch(Exception e){
        }
    }

    public void creaElimina(AssegnazioneRecordObj item){
        view.eliminaService(item);
        view.creaNotifica(item.getAssegnazioneRecordTipologia() + OK_CANCELLA, false);
        setURI("");
    }

    public void annullaOperazione(){
        view.rimuoviSelezione();
        setURI("");
    }

    public void enter(Integer courseId ,String parameter) {
        if (parameter != null && !parameter.isEmpty()) {
            if (parameter.equals("new")) {
                this.courseId = courseId;
                creaNuovo(courseId);
            } else {
                try {
                    int intId = Integer.parseInt(parameter);
                    AssegnazioneRecordObj item = service.getAssegnazioneById(intId);
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

    public void setURI(String parameter) {
        String fragmentParameter = "";
        if(parameter == null || parameter.isEmpty()) {
            fragmentParameter = courseId + "";
        }
        else
            fragmentParameter = courseId + "_" + parameter;
        UI.getCurrent().navigate(AssegnazioniView.class, fragmentParameter);
    }

    public void setURI(int courseId, String parameter) {
        this.courseId = courseId;
        String fragmentParameter = "";
        if(parameter == null || parameter.isEmpty()) {
            fragmentParameter = courseId + "";
        }
        else
            fragmentParameter = courseId + "_" + parameter;
        UI.getCurrent().navigate(AssegnazioniView.class, fragmentParameter);
    }
}
