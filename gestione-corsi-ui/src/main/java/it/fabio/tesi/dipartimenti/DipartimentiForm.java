package it.fabio.tesi.dipartimenti;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import it.fabio.tesi.generated.tables.records.DipartimentoRecord;

public class DipartimentiForm extends Div {
    private FormLayout content;
    private TextField denominazione;
    private TextField descrizione;
    private TextField paese;
    private TextField indirizzo;
    private TextField telefono;

    private Button btnSalva;
    private Button btnAnnulla;
    private Button btnElimina;

    private Binder<DipartimentoRecord> binder;
    private DipartimentoRecord currentDipartimento = new DipartimentoRecord();

    public DipartimentiForm(DipartimentiLogic viewLogic){
        setId("dipartimento-form");

        content = new FormLayout();

        content.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3));

        content.setSizeUndefined();

        denominazione = new TextField();
        denominazione.setId("textfield-denominazione");
        denominazione.setLabel("Denominazione");
        denominazione.setWidth("100%");
        denominazione.setRequired(true);
        denominazione.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(denominazione);

        descrizione = new TextField();
        descrizione.setId("textfield-descrizione");
        descrizione.setLabel("Descrizione");
        descrizione.setWidth("100%");
        descrizione.setRequired(false);
        descrizione.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(descrizione);

        paese = new TextField();
        paese.setId("textfield-paese");
        paese.setLabel("Paese");
        paese.setWidth("100%");
        paese.setRequired(false);
        paese.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(paese);

        indirizzo = new TextField();
        indirizzo.setId("textfield-indirizzo");
        indirizzo.setLabel("Indirizzo");
        indirizzo.setWidth("100%");
        indirizzo.setRequired(false);
        indirizzo.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(indirizzo);

        telefono = new TextField();
        telefono.setId("textfield-telefono");
        telefono.setLabel("Telefono");
        telefono.setWidth("100%");
        telefono.setRequired(false);
        telefono.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(telefono);

        binder = new BeanValidationBinder<>(DipartimentoRecord.class);
        binder.bindInstanceFields(this);

        binder.forField(denominazione)
                .withValidator(e-> e.length() >= 3, "Il campo deve essere più lungo di 3 caratteri")
                .bind(DipartimentoRecord::getDenominazione, DipartimentoRecord::setDenominazione);
        binder.forField(descrizione).bind(DipartimentoRecord::getDescrizione, DipartimentoRecord::setDescrizione);
        binder.forField(paese).bind(DipartimentoRecord::getPaese, DipartimentoRecord::setPaese);
        binder.forField(indirizzo).bind(DipartimentoRecord::getIndirizzo, DipartimentoRecord::setIndirizzo);
        binder.forField(telefono).bind(DipartimentoRecord::getTelefono, DipartimentoRecord::setTelefono);

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
            if(currentDipartimento != null && binder.writeBeanIfValid(currentDipartimento)){
                if(currentDipartimento.getDenominazione().equals(""))
                    denominazione.setInvalid(true);
                else
                    viewLogic.creaSalva(currentDipartimento);
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
            if(currentDipartimento != null)
                viewLogic.creaElimina(currentDipartimento);
        });

        VerticalLayout buttons = new VerticalLayout();
        buttons.add(btnSalva, btnAnnulla, btnElimina);

        content.setColspan(denominazione, 3);
        content.setColspan(descrizione, 3);
        content.setColspan(buttons, 3);
        content.add(buttons);

        add(content);
    }

    public void modificaItem(DipartimentoRecord item) {
        boolean nuovo = false;
        if (item == null) {
            nuovo = true;
            item = new DipartimentoRecord();
            item.setIddipartimento(-1);
            btnAnnulla.setEnabled(true);
        }
        btnElimina.setVisible(!nuovo);
        currentDipartimento = item;
        binder.readBean(item);
    }
}