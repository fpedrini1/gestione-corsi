package it.fabio.tesi.cdl;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.fabio.tesi.DatabaseDataService;
import it.fabio.tesi.MainLayout;
import it.fabio.tesi.generated.tables.records.CorsoRecord;
import it.fabio.tesi.support.CorsodilaureaRecordObj;
import it.fabio.tesi.utils.GestioneConst;
import org.jooq.exception.DataAccessException;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Route(value = GestioneConst.PAGE_CDL, layout = MainLayout.class)
@PageTitle(GestioneConst.TITLE_CDL)

public class CdlView extends VerticalLayout {
    private VerticalLayout leftView;
    private VerticalLayout rightView;
    private Integer currentIdCdl;
    private Integer currentAnnoRif;
    private Integer previousAnnoRif;
    private String currentAnno;
    private Span info = new Span();
    private CheckboxGroup<CorsoRecord> checkboxCorso;
    private DatabaseDataService service = new DatabaseDataService();
    private Set<CorsoRecord> corsiAttivi;

    public CdlView() throws ClassNotFoundException {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        leftView = creaSelettoreCdl();
        rightView = creaSelettoreCorsi();
        SplitLayout layout = new SplitLayout(leftView, rightView);
        layout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        layout.setSizeFull();
        add(layout);
    }

    private VerticalLayout creaSelettoreCdl(){
        VerticalLayout layout = new VerticalLayout();
        layout.add(new Span("Seleziona un Corso di Laurea"));
        Select<CorsodilaureaRecordObj> select = new Select();
        select.setId("combobox-cdl-form");
        select.setLabel("Corso di Laurea");
        select.setItemLabelGenerator(CorsodilaureaRecordObj::toString);
        select.setItems(service.getAllCdl());
        select.addValueChangeListener(e -> {
            if(e.getValue() != null) {
                rightView.setEnabled(true);
                info.setText("Selezionato: " + e.getValue().toString());
                this.currentIdCdl = e.getValue().getId();
                this.currentAnno = e.getValue().getAnno();
                if(this.previousAnnoRif != null)
                    this.previousAnnoRif = null;
                updateLista(this.currentAnno);
            }
        });
        layout.add(select);
        Button elimina = new Button("Elimina");
        elimina.addClickListener(e -> {
            service.delete(select.getValue().getCorsodilaureaRecord());
            select.setItems(service.getAllCdl());
        });
        layout.add(elimina);

        layout.add(new Span("Oppure creane uno nuovo"));
        TextField denominazione = new TextField();
        denominazione.setLabel("Denominazione");
        TextField codice = new TextField();
        codice.setLabel("Codice");
        TextField anno = new TextField();
        anno.setLabel("Anno");
        Button salva = new Button("Salva");
        salva.addClickListener(e -> {
            CorsodilaureaRecordObj tempCdl = new CorsodilaureaRecordObj(denominazione.getValue(),
                    codice.getValue(), anno.getValue());
            service.insert(tempCdl.getCorsodilaureaRecord());
            denominazione.setValue("");
            codice.setValue("");
            anno.setValue("");
            select.setItems(service.getAllCdl());
        });
        layout.add(denominazione, codice, anno);
        layout.add(salva);

        return layout;
    }

    private VerticalLayout creaSelettoreCorsi(){
        VerticalLayout layout = new VerticalLayout();
        info.setText("Seleziona un corso di laurea prima di selezionare i corsi.");
        layout.add(info);
        layout.setEnabled(false);
        Select<Integer> selezionaAnnoRif = new Select<>();
        selezionaAnnoRif.setItems(1, 2, 3, 4, 5);
        selezionaAnnoRif.setPlaceholder("Seleziona l'anno.");
        selezionaAnnoRif.setLabel("Anno scolastico");
        selezionaAnnoRif.addValueChangeListener(e -> {
            if(selezionaAnnoRif.getValue() != null)
                checkboxCorso.setEnabled(true);
            this.currentAnnoRif = e.getValue();
            corsiAttivi = new HashSet(service.getCorsiAttivi(this.currentIdCdl, this.currentAnnoRif, this.currentAnno));
            checkboxCorso.setValue(corsiAttivi);
        });
        layout.add(selezionaAnnoRif);
        checkboxCorso = new CheckboxGroup<>();
        checkboxCorso.setEnabled(false);
        checkboxCorso.setLabel("Corsi");
        checkboxCorso.setItemLabelGenerator(CorsoRecord::getDenominazione);
        checkboxCorso.addValueChangeListener(e -> {
            if(this.previousAnnoRif == null && corsiAttivi.size() == 0) {
                if(e.getValue().size() != corsiAttivi.size()) {
                    try {
                        updateDatabase(e.getOldValue(), e.getValue(), this.currentIdCdl, this.currentAnnoRif);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if(this.previousAnnoRif == null) {
                this.previousAnnoRif = this.currentAnnoRif;
            }
            else if(e.getOldValue() != e.getValue()){
                if(this.previousAnnoRif != this.currentAnnoRif) {
                    this.previousAnnoRif = this.currentAnnoRif;
                }
                else{
                try {
                    updateDatabase(e.getOldValue(), e.getValue(), this.currentIdCdl, this.currentAnnoRif);
                } catch (DataAccessException | SQLException ex) {
                    creaNotifica("Il corso è già presente in altri anni", true);
                    checkboxCorso.setValue(e.getOldValue());
                }}
            }
        });

        layout.add(checkboxCorso);

        return layout;
    }

    private void updateLista(String anno){
        this.checkboxCorso.setItems(service.getCorsiByAnno(anno));
    }

    private void updateDatabase(Set<CorsoRecord> caricati, Set<CorsoRecord> cambiati, int idCdl, int annoRif) throws SQLException {
        for(CorsoRecord c : caricati){
            boolean exists = false;
            for(CorsoRecord m : cambiati){
                if(m.getIdcorso() == c.getIdcorso())
                    exists = true;
            }
            if(!exists) {
                service.delete(c, idCdl, annoRif);
                creaNotifica("Corso rimosso dal Corso di Laurea", false);
            }
        }
        for(CorsoRecord c : cambiati){
            boolean toAdd = true;
            for(CorsoRecord m : caricati){
                if(c.getIdcorso() == m.getIdcorso())
                    toAdd = false;
            }
            if(toAdd){
                service.insert(c, idCdl, annoRif);
                creaNotifica("Corso aggiunto al Corso di Laurea", false);
            }
        }
    }

    public void creaNotifica(String msg, boolean isError){
        Notification notifica = new Notification();
        notifica.add(new Span(msg));
        if(isError)
            notifica.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notifica.setDuration(3000);
        notifica.open();
    }

}
