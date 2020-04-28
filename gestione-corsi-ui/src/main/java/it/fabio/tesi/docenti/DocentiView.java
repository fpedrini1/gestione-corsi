package it.fabio.tesi.docenti;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
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
import it.fabio.tesi.generated.tables.records.DocenteRecord;
import it.fabio.tesi.user.UserView;
import it.fabio.tesi.utils.GestioneConst;

import java.util.ArrayList;

@Route(value = GestioneConst.PAGE_DOCENTI, layout = MainLayout.class)
@PageTitle(GestioneConst.TITLE_DOCENTI)
public class DocentiView extends VerticalLayout implements HasUrlParameter<String> {
    private DatabaseDataService service = new DatabaseDataService();
    private DocentiGrid grid = new DocentiGrid();
    private DocentiLogic viewLogic = new DocentiLogic(this, service);
    private Dialog form;
    private DocentiForm formlayout;

    public DocentiView() throws ClassNotFoundException {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        HorizontalLayout menu = creaRicerca();
        grid.setId("grid-docenti");
        grid.setItems(service.getAllDocenti());
        grid.asSingleSelect().addValueChangeListener(e -> {
            viewLogic.creaModifica(e.getValue());
        });
        GridContextMenu<DocenteRecord> contextMenu = new GridContextMenu<>(grid);
        contextMenu.setId("context-docenti");
        contextMenu.setDynamicContentHandler(e -> {
            if (e == null)
                return false;
            contextMenu.removeAll();
            Integer userId = e.getIddocente();
            contextMenu.addItem("Vai al profilo", c ->
                    UI.getCurrent().navigate(UserView.class, userId.toString())
            );
            return true;
        });
        formlayout = new DocentiForm(viewLogic, service);
        form = new Dialog();
        form.setCloseOnOutsideClick(false);
        form.add(formlayout);
        VerticalLayout content = new VerticalLayout();
        content.add(menu, grid);
        content.setSizeFull();
        add(content);
    }

    private HorizontalLayout creaRicerca(){
        TextField ricerca = new TextField();
        ricerca.setId("textfield-ricerca");
        ricerca.setPlaceholder("Trova i docenti per denominazione o descrizione");
        ricerca.setClearButtonVisible(true);
        ricerca.addValueChangeListener(e ->
                grid.setItems(service.getAllDocentiByText(e.getValue()))
        );
        ricerca.setValueChangeMode(ValueChangeMode.EAGER);

        ComboBox<DipartimentoRecord> cbDipartimento = new ComboBox<>();
        cbDipartimento.setId("combobox-dipartimento");
        ArrayList<DipartimentoRecord> list = new ArrayList<>(service.getAllDipartimenti());
        cbDipartimento.setItems(list);
        cbDipartimento.setValue(null);
        cbDipartimento.setAllowCustomValue(false);
        cbDipartimento.setClearButtonVisible(true);
        cbDipartimento.setPlaceholder("Filtra per dipartimento");
        cbDipartimento.addValueChangeListener(e -> {
            if(ricerca.isEmpty()) {
                if (e.getValue() != null)
                    grid.setItems(service.getAllDocentiByDip(e.getValue()));
                else
                    grid.setItems(service.getAllDocenti());
            }
            else
                if (e.getValue() == null)
                    grid.setItems(service.getAllDocentiByText(ricerca.getValue().trim()));
                else
                    grid.setItems(service.getAllDocentiByDipAndText(e.getValue(), ricerca.getValue().trim()));
        });
        cbDipartimento.setItemLabelGenerator(DipartimentoRecord::getDenominazione);

        Button btnNuovo = new Button("Nuovo Docente");
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
        layout.add(cbDipartimento);
        layout.add(btnNuovo);
        layout.setVerticalComponentAlignment(Alignment.START, btnNuovo);

        return layout;
    }

    public void abilitaModifica(DocenteRecord item){
        formlayout.modificaItem(item);
        mostraForm(item != null);
    }

    public void eliminaService(DocenteRecord item){
        grid.getSelectionModel().deselectAll();
        mostraForm(false);
        service.delete(item);
        grid.setItems(service.getAllDocenti());
    }

    public void aggiornaService(DocenteRecord item){
        grid.getSelectionModel().deselectAll();
        mostraForm(false);
        service.update(item);
        grid.setItems(service.getAllDocenti());
    }

    public void salvaService(DocenteRecord item){
        grid.getSelectionModel().deselectAll();
        mostraForm(false);
        service.insert(item);
        grid.setItems(service.getAllDocenti());
    }

    public void rimuoviSelezione(){
        grid.getSelectionModel().deselectAll();
        mostraForm(false);
    }

    public void impostaSelezione(DocenteRecord item){
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