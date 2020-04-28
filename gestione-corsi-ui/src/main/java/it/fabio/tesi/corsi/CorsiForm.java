package it.fabio.tesi.corsi;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import it.fabio.tesi.DatabaseDataService;
import it.fabio.tesi.generated.tables.records.DipartimentoRecord;
import it.fabio.tesi.support.CorsoRecordObj;

public class CorsiForm extends Div {
    private FormLayout content;

    CorsiLogic view;
    DatabaseDataService service;

    private Button btnSalva;
    private Button btnAnnulla;
    private Button btnElimina;
    private ComboBox<DipartimentoRecord> dipartimento;

    private Binder<CorsoRecordObj> binder;
    private CorsoRecordObj currentItem = new CorsoRecordObj();
    private DipartimentoRecord currentDipartimento = new DipartimentoRecord();

    public CorsiForm(CorsiLogic viewLogic, DatabaseDataService service){
        this.service = service;
        this.view = viewLogic;

        setId("corsi-form");

        content = new FormLayout();

        content.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3));

        content.setSizeUndefined();
        TextField codice = new TextField();
        codice.setLabel("Codice");
        codice.setWidth("100%");
        codice.setRequired(true);
        codice.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(codice);

        TextField ssd = new TextField();
        ssd.setLabel("SSD");
        ssd.setWidth("100%");
        ssd.setRequired(true);
        ssd.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(ssd);

        TextField codicemutuazione = new TextField();
        codicemutuazione.setLabel("Codice Mutuazione");
        codicemutuazione.setWidth("100%");
        codicemutuazione.setRequired(false);
        codicemutuazione.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(codicemutuazione);

        TextField denominazione = new TextField();
        denominazione.setLabel("Denominazione");
        denominazione.setWidth("100%");
        denominazione.setRequired(false);
        denominazione.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(denominazione);

        IntegerField ore = new IntegerField();
        ore.setLabel("Ore totali");
        ore.setWidth("100%");
        ore.setHasControls(true);
        ore.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(ore);

        IntegerField cfu = new IntegerField();
        cfu.setLabel("Crediti Formativi Universitari (CFU)");
        cfu.setWidth("100%");
        cfu.setHasControls(true);
        cfu.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(cfu);

        IntegerField studenti = new IntegerField();
        studenti.setLabel("Numero studenti");
        studenti.setWidth("100%");
        studenti.setHasControls(true);
        studenti.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(studenti);

        TextField parametro = new TextField();
        parametro.setLabel("Parametro");
        parametro.setWidth("100%");
        parametro.setRequired(false);
        parametro.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(parametro);

        TextField annoaccademico = new TextField();
        annoaccademico.setLabel("Anno Accademico");
        annoaccademico.setWidth("100%");
        annoaccademico.setRequired(true);
        annoaccademico.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(annoaccademico);

        IntegerField semestre = new IntegerField();
        semestre.setLabel("Periodo");
        semestre.setWidth("100%");
        semestre.setHasControls(true);
        semestre.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(semestre);

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

        binder = new BeanValidationBinder<>(CorsoRecordObj.class);

        binder.forField(codice).bind(CorsoRecordObj::getCorsiRecordCodice, CorsoRecordObj::setCorsiRecordCodice);
        binder.forField(ssd).bind(CorsoRecordObj::getCorsiRecordSsd, CorsoRecordObj::setCorsiRecordSsd);
        binder.forField(denominazione).bind(CorsoRecordObj::getCorsiRecordDenominazione, CorsoRecordObj::setCorsiRecordDenominazione);
        binder.forField(codicemutuazione).bind(CorsoRecordObj::getCorsiRecordCodiceMutuazione, CorsoRecordObj::setCorsiRecordCodiceMutuazione);
        binder.forField(parametro).bind(CorsoRecordObj::getCorsiRecordParametro, CorsoRecordObj::setCorsiRecordParametro);
        binder.forField(ore).bind(CorsoRecordObj::getCorsiRecordOre, CorsoRecordObj::setCorsiRecordOre);
        binder.forField(semestre).bind(CorsoRecordObj::getCorsiRecordPeriodo, CorsoRecordObj::setCorsiRecordPeriodo);
        binder.forField(studenti).bind(CorsoRecordObj::getCorsiRecordNumStudenti, CorsoRecordObj::setCorsiRecordNumStudenti);
        binder.forField(cfu).bind(CorsoRecordObj::getCorsiRecordCfu, CorsoRecordObj::setCorsiRecordCfu);
        binder.forField(annoaccademico).withValidator(v -> v.length() == 9, "Utilizzare il formato AAAA/AAAA")
                .bind(CorsoRecordObj::getCorsiRecordAnnoAccademico, CorsoRecordObj::setCorsiRecordAnnoAccademico);

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
                if (dipartimento.getValue() != null) {
                    currentItem.getCorsoRecord().setDipartimentoIddipartimento(dipartimento.getValue().getIddipartimento());
                    viewLogic.creaSalva(currentItem);
                } else {
                    dipartimento.setInvalid(true);
                    dipartimento.setErrorMessage("Seleziona un dipartimento dalla lista");
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

        content.setColspan(denominazione, 2);
        content.setColspan(buttons, 3);
        content.add(buttons);

        add(content);
    }

    public void modificaItem(CorsoRecordObj item) {
        boolean nuovo = false;
        if (item == null) {
            nuovo = true;
            item = new CorsoRecordObj();
            item.setCorsiRecordId(-1);
            btnAnnulla.setEnabled(true);
        } else{
            if(item.getCorsoRecord().getDipartimentoIddipartimento() != null) {
                currentDipartimento = service.getDipartimentoById(item.getCorsoRecord().getDipartimentoIddipartimento());
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