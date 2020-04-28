package it.fabio.tesi.user;

import it.fabio.tesi.DatabaseDataService;
import it.fabio.tesi.LogicInterface;
import it.fabio.tesi.generated.tables.records.DocenteRecord;
import it.fabio.tesi.support.AssegnazioneRecordObj;

import java.util.ArrayList;

import static it.fabio.tesi.utils.GestioneConst.ERROR_NotAvailable;
import static it.fabio.tesi.utils.GestioneConst.ERROR_NotValid;

public class UserLogic implements LogicInterface {
    private UserView view;
    private DatabaseDataService service;

    public UserLogic(UserView view, DatabaseDataService service){
        this.view = view;
        this.service = service;
    }

    @Override
    public void enter(String id) {
        if (id == null || id.isEmpty()){
             view.creaNotifica(ERROR_NotValid, true);
        }else{
            try {
                int intId = Integer.parseInt(id);
                DocenteRecord item = trova(intId);
                if(item.getIddocente() != null) {
                    view.updateComponenti(trovaAssegnazioni(item));
                    view.updateInfo(item);
                }
                else{
                    view.creaNotifica(ERROR_NotAvailable, true);
                }
            } catch (NumberFormatException e) {
                view.creaNotifica(ERROR_NotValid, true);
            }
        }
    }

    private DocenteRecord trova(int id){
        return service.getDocenteById(id);
    }
    private ArrayList<AssegnazioneRecordObj> trovaAssegnazioni(DocenteRecord docente){
        return service.getAllAssegnazioniByDocente(docente);
    }

    @Override
    public void setURI(String parameter) {
        return;
    }
}
