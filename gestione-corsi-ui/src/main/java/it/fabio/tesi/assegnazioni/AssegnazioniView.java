package it.fabio.tesi.assegnazioni;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import it.fabio.tesi.DatabaseDataService;
import it.fabio.tesi.MainLayout;
import it.fabio.tesi.support.AssegnazioneRecordObj;
import it.fabio.tesi.utils.GestioneConst;

import java.util.ArrayList;

import static it.fabio.tesi.utils.GestioneConst.*;

@Route(value = GestioneConst.PAGE_ASSEGNAZIONI, layout = MainLayout.class)
@PageTitle(GestioneConst.TITLE_ASSEGNAZIONI)
public class AssegnazioniView extends VerticalLayout implements HasUrlParameter<String> {
    private DatabaseDataService service = new DatabaseDataService();
    private AssegnazioniGrid grid = new AssegnazioniGrid();
    private ArrayList<AssegnazioneRecordObj> gridList = new ArrayList<>();
    private int courseId = 0;
    private AssegnazioniLogic viewLogic = null;
    private Dialog form;
    private AssegnazioniForm formlayout;
    private Dialog window = new Dialog();

    public AssegnazioniView() throws ClassNotFoundException {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        HorizontalLayout menu = creaRicerca();
        grid.setId("grid-assegnazioni");
        grid.asSingleSelect().addValueChangeListener(e -> {
            viewLogic.creaModifica(e.getValue());
        });
        form = new Dialog();
        form.setCloseOnOutsideClick(false);
        formlayout = new AssegnazioniForm(this, service);
        form.add(formlayout);
        VerticalLayout content = new VerticalLayout();
        content.add(menu, grid);
        content.setSizeFull();
        add(content);
    }

    private HorizontalLayout creaRicerca(){
        Span spacer = new Span();

        Button btnNuovo = new Button("Nuova Assegnazione");
        btnNuovo.setId("button-nuovo");
        btnNuovo.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnNuovo.setIcon(VaadinIcon.PLUS_CIRCLE_O.create());
        btnNuovo.addClickListener(e ->
                viewLogic.creaNuovo(this.courseId)
        );

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        layout.setJustifyContentMode(JustifyContentMode.END);
        layout.addAndExpand(spacer);
        layout.add(btnNuovo);
        layout.setVerticalComponentAlignment(Alignment.START, btnNuovo);

        return layout;
    }

    public void abilitaModifica(AssegnazioneRecordObj item){
        formlayout.modificaItem(item);
        mostraForm(item != null);
    }

    public void eliminaService(AssegnazioneRecordObj item){
        grid.getSelectionModel().deselectAll();
        mostraForm(false);
        service.delete(item);
        gridList = service.getAllAssegnazioniByIdCorso(this.courseId);
        grid.setItems(gridList);
    }

    public void aggiornaService(AssegnazioneRecordObj item){
        grid.getSelectionModel().deselectAll();
        mostraForm(false);
        service.update(item);
        gridList = service.getAllAssegnazioniByIdCorso(this.courseId);
        grid.setItems(gridList);
    }

    public void salvaService(AssegnazioneRecordObj item){
        grid.getSelectionModel().deselectAll();
        mostraForm(false);
        service.insert(item);
        gridList = service.getAllAssegnazioniByIdCorso(this.courseId);
        grid.setItems(gridList);
    }

    public void rimuoviSelezione(){
        grid.getSelectionModel().deselectAll();
        mostraForm(false);
    }

    public void impostaSelezione(AssegnazioneRecordObj item){
        grid.getSelectionModel().select(item);
    }

    public void mostraForm(boolean mostra){
        if(mostra){
            if(!form.isOpened())
                form.open();
        } else
            if(form.isOpened())
                form.close();
    }

    public void creaNotifica(String msg, boolean isError){
        Notification notifica = new Notification();
        notifica.add(new Span(msg));
        if(isError)
            notifica.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notifica.setDuration(3000);
        notifica.open();
    }

    public AssegnazioniLogic getLogic(){
        return this.viewLogic;
    }
    public Integer getCourseId() { return this.courseId; }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if(parameter != null) {
            String[] params = parameter.split("_");
            if (params.length == 0)
                creaNotifica(ERROR_NotValidCourse, true);
            else if (params.length == 1) {
                this.courseId = Integer.parseInt(parameter);
                gridList = service.getAllAssegnazioniByIdCorso(this.courseId);
                grid.setItems(gridList);
                grid.getColumns().get(3).setFooter(labelOre(gridList, this.courseId));
                viewLogic = new AssegnazioniLogic(this, service, courseId);
            } else if (params.length == 2) {
                this.courseId = Integer.parseInt(params[0]);
                viewLogic = new AssegnazioniLogic(this, service, courseId);
                viewLogic.enter(this.courseId, params[1]);
            } else creaNotifica(ERROR_NotValidCourse, true);
        }
        else {
            creaNotifica(ERROR_NotValidCourse, true);
        }
    }

    public String labelOre(ArrayList<AssegnazioneRecordObj> list, int courseId){
        Integer somma = 0;
        Integer oreCorso = 0;
        if(list.size() > 0)
            for(AssegnazioneRecordObj a : list){
                somma += a.getAssegnazioneOre();
                oreCorso = a.getCorsoRecord().getOretotali();
            }
        else
            oreCorso = service.getCorsoById(courseId).getOretotali();
        if(somma > oreCorso)
            creaDialog(new Icon(VaadinIcon.EXCLAMATION), INFO_OreSuperate, "red");
        else if(somma < oreCorso)
            creaDialog(new Icon(VaadinIcon.EXCLAMATION), INFO_OreNecessarie, "orange");
        return somma + " / " + oreCorso + " ore assegnate";
    }

    private void creaDialog(Icon icona, String testo, String color){
        window.removeAll();
        window.setCloseOnEsc(true);
        window.setCloseOnOutsideClick(true);
        icona.setColor(color);
        Label messaggio = new Label(testo);
        window.add(icona, messaggio);
        window.open();
    }
}