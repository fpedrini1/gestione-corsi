package it.fabio.tesi.dipartimenti;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import it.fabio.tesi.DatabaseDataService;
import it.fabio.tesi.MainLayout;
import it.fabio.tesi.generated.tables.records.DipartimentoRecord;
import it.fabio.tesi.utils.GestioneConst;

@Route(value = GestioneConst.PAGE_DIPARTIMENTI, layout = MainLayout.class)
@PageTitle(GestioneConst.TITLE_DIPARTIMENTI)
public class DipartimentiView extends VerticalLayout implements HasUrlParameter<String> {
    private DatabaseDataService service = new DatabaseDataService();
    private DipartimentiGrid grid = new DipartimentiGrid();
    private DipartimentiLogic viewLogic = new DipartimentiLogic(this, service);
    private Dialog form;
    private DipartimentiForm formlayout;

    public DipartimentiView() throws ClassNotFoundException {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        HorizontalLayout menu = creaRicerca();
        grid.setId("grid-dipartimenti");
        grid.setItems(service.getAllDipartimenti());
        grid.asSingleSelect().addValueChangeListener(e -> {
            viewLogic.creaModifica(e.getValue());
        });
        form = new Dialog();
        form.setCloseOnOutsideClick(false);
        formlayout = new DipartimentiForm(viewLogic);
        form.add(formlayout);
        VerticalLayout content = new VerticalLayout();
        content.add(menu, grid);
        content.setSizeFull();
        add(content);
    }

    private HorizontalLayout creaRicerca(){
        TextField ricerca = new TextField();
        ricerca.setId("textfield-ricerca");
        ricerca.setPlaceholder("Trova i dipartimenti per denominazione o descrizione");
        ricerca.setClearButtonVisible(true);
        ricerca.addValueChangeListener(e ->
                grid.setItems(service.getAllDipartimentiByText(e.getValue()))
        );
        ricerca.setValueChangeMode(ValueChangeMode.EAGER);
        Button btnNuovo = new Button("Nuovo Dipartimento");
        btnNuovo.setId("button-nuovo");
        btnNuovo.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnNuovo.setIcon(VaadinIcon.PLUS_CIRCLE_O.create());
        btnNuovo.addClickListener(e ->
                viewLogic.creaNuovo()
        );

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        layout.setJustifyContentMode(JustifyContentMode.END);
        layout.addAndExpand(ricerca);
        layout.add(btnNuovo);
        layout.setVerticalComponentAlignment(Alignment.START, btnNuovo);

        return layout;
    }

    public void abilitaModifica(DipartimentoRecord item){
        formlayout.modificaItem(item);
        mostraForm(item != null);
    }

    public void eliminaService(DipartimentoRecord item){
        grid.getSelectionModel().deselectAll();
        mostraForm(false);
        service.delete(item);
        grid.setItems(service.getAllDipartimenti());
    }

    public void aggiornaService(DipartimentoRecord item){
        grid.getSelectionModel().deselectAll();
        mostraForm(false);
        service.update(item);
        grid.setItems(service.getAllDipartimenti());
    }

    public void salvaService(DipartimentoRecord item){
        grid.getSelectionModel().deselectAll();
        mostraForm(false);
        service.insert(item);
        grid.setItems(service.getAllDipartimenti());
    }

    public void rimuoviSelezione(){
        grid.getSelectionModel().deselectAll();
        mostraForm(false);
    }

    public void impostaSelezione(DipartimentoRecord item){
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

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}