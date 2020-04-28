package it.fabio.tesi.docenti;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import it.fabio.tesi.DatabaseDataService;
import it.fabio.tesi.generated.tables.records.DipartimentoRecord;
import it.fabio.tesi.generated.tables.records.DocenteRecord;

public class DocentiForm extends Div {
    private FormLayout content;

    DocentiLogic view;
    DatabaseDataService service;

    private Button btnSalva;
    private Button btnAnnulla;
    private Button btnElimina;
    private ComboBox<DipartimentoRecord> dipartimento;

    private Binder<DocenteRecord> binder;
    private DocenteRecord currentItem = new DocenteRecord();
    private DipartimentoRecord currentDipartimento = new DipartimentoRecord();

    public DocentiForm(DocentiLogic viewLogic, DatabaseDataService service){
        this.service = service;
        this.view = viewLogic;

        setId("docenti-form");

        content = new FormLayout();

        content.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2));

        content.setSizeUndefined();

        TextField email = new TextField();
        email.setId("textfield-email");
        email.setLabel("Email");
        email.setWidth("100%");
        email.setRequired(true);
        email.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(email);

        TextField nome = new TextField();
        nome.setId("textfield-nome");
        nome.setLabel("Nome");
        nome.setWidth("100%");
        nome.setRequired(false);
        nome.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(nome);

        TextField cognome = new TextField();
        cognome.setId("textfield-cognome");
        cognome.setLabel("Cognome");
        cognome.setWidth("100%");
        cognome.setRequired(false);
        cognome.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(cognome);

        TextField telefono = new TextField();
        telefono.setId("textfield-telefono");
        telefono.setLabel("Telefono");
        telefono.setWidth("100%");
        telefono.setRequired(false);
        telefono.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(telefono);

        dipartimento = new ComboBox<>();
        dipartimento.setId("combobox-dipartimento-form");
        dipartimento.setLabel("Dipartimento");
        dipartimento.setItems(service.getAllDipartimenti());
        dipartimento.setItemLabelGenerator(DipartimentoRecord::getDenominazione);
        dipartimento.addValueChangeListener(e->{
            if(e.getOldValue() != e.getValue())
                btnSalva.setEnabled(true);
            else
                btnSalva.setEnabled(false);
        });
        content.add(dipartimento);

        binder = new BeanValidationBinder<>(DocenteRecord.class);

        binder.forField(email).bind(DocenteRecord::getEmail, DocenteRecord::setEmail);
        binder.forField(nome).bind(DocenteRecord::getNome, DocenteRecord::setNome);
        binder.forField(cognome).bind(DocenteRecord::getCognome, DocenteRecord::setCognome);
        binder.forField(telefono).bind(DocenteRecord::getTelefono, DocenteRecord::setTelefono);

        binder.addStatusChangeListener(e ->{
            boolean valido = !e.hasValidationErrors();
            boolean cambiato = binder.hasChanges();
            btnSalva.setEnabled(valido && cambiato);
        });

        btnSalva = new Button("Salva");
        btnSalva.setId("button-salva");
        btnSalva.setWidth("100%");
        btnSalva.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        btnSalva.addClickListener(e -> {
            if(currentItem != null && binder.writeBeanIfValid(currentItem)){
                if(!currentItem.getEmail().contains("@"))
                    email.setInvalid(true);
                else
                    if(dipartimento.getValue() != null){
                        currentItem.setDipartimentoIddipartimento(dipartimento.getValue().getIddipartimento());
                        viewLogic.creaSalva(currentItem);
                    }
                else{
                    dipartimento.setInvalid(true);
                }
            }
        });
        btnSalva.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        btnAnnulla = new Button("Annulla");
        btnAnnulla.setId("button-annulla");
        btnAnnulla.setWidth("100%");
        btnAnnulla.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAnnulla.addClickListener(e-> viewLogic.annullaOperazione());
        btnAnnulla.addClickShortcut(Key.KEY_E, KeyModifier.CONTROL);

        btnElimina = new Button("Elimina");
        btnElimina.setId("button-elimina");
        btnElimina.setWidth("100%");
        btnElimina.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        btnElimina.addClickListener(e-> {
            if(currentItem != null)
                viewLogic.creaElimina(currentItem);
        });

        VerticalLayout buttons = new VerticalLayout();
        buttons.add(btnSalva, btnAnnulla, btnElimina);

        content.setColspan(email, 3);
        content.setColspan(buttons, 3);
        content.add(buttons);

        add(content);
    }

    public void modificaItem(DocenteRecord item) {
        boolean nuovo = false;
        if (item == null) {
            nuovo = true;
            item = new DocenteRecord();
            item.setIddocente(-1);
            btnAnnulla.setEnabled(true);
        } else{
            if(item.getDipartimentoIddipartimento() != null) {
                currentDipartimento = service.getDipartimentoById(item.getDipartimentoIddipartimento());
                dipartimento.setValue(currentDipartimento);
            }
            else{
                dipartimento.setValue(null);
            }
        }
        btnElimina.setVisible(!nuovo);
        currentItem = item;
        binder.readBean(item);
    }
}