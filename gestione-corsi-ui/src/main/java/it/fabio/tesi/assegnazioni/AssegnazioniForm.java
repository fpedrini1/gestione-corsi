package it.fabio.tesi.assegnazioni;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import it.fabio.tesi.DatabaseDataService;
import it.fabio.tesi.generated.enums.AssegnazioneContratto;
import it.fabio.tesi.generated.enums.AssegnazioneTipologia;
import it.fabio.tesi.generated.tables.records.DocenteRecord;
import it.fabio.tesi.support.AssegnazioneRecordObj;

public class AssegnazioniForm extends Div {
    private FormLayout content;
    private Select<DocenteRecord> docente;
    private Select<AssegnazioneContratto> contratto;
    private Select<AssegnazioneTipologia> tipologia;
    private IntegerField ore;

    private Button btnSalva;
    private Button btnAnnulla;
    private Button btnElimina;

    private Binder<AssegnazioneRecordObj> binder;
    private AssegnazioneRecordObj currentAssegnazione = new AssegnazioneRecordObj();

    public AssegnazioniForm(AssegnazioniView view, DatabaseDataService service){
        setId("assegnazioni-form");

        content = new FormLayout();

        content.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3));

        content.setSizeUndefined();

        docente = new Select();
        docente.setId("combobox-docente-form");
        docente.setLabel("Docente");
        docente.setItems(service.getAllDocenti());
        docente.setItemLabelGenerator(DocenteRecord::getEmail);
        content.add(docente);

        contratto = new Select();
        contratto.setId("combobox-contratto-form");
        contratto.setLabel("Contratto");
        contratto.setItems(AssegnazioneContratto.values());
        content.add(contratto);

        tipologia = new Select();
        tipologia.setId("combobox-tipologia-form");
        tipologia.setLabel("Tipologia");
        tipologia.setItems(AssegnazioneTipologia.values());
        content.add(tipologia);

        ore = new IntegerField();
        ore.setId("numberfield-ore");
        ore.setLabel("Ore");
        ore.setWidth("100%");
        ore.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(ore);

        binder = new BeanValidationBinder<>(AssegnazioneRecordObj.class);

        binder.forField(docente).bind(AssegnazioneRecordObj::getDocenteRecord, AssegnazioneRecordObj::setDocenteRecord);
        binder.forField(contratto).bind(AssegnazioneRecordObj::getAssegnazioneRecordContratto, AssegnazioneRecordObj::setAssegnazioneRecordContratto);
        binder.forField(tipologia).bind(AssegnazioneRecordObj::getAssegnazioneRecordTipologia, AssegnazioneRecordObj::setAssegnazioneRecordTipologia);
        binder.forField(ore).bind(AssegnazioneRecordObj::getAssegnazioneOre, AssegnazioneRecordObj::setAssegnazioneOre);

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
            if(currentAssegnazione != null && binder.writeBeanIfValid(currentAssegnazione)){
                    currentAssegnazione.getCorsoRecord().setIdcorso(view.getCourseId());
                    view.getLogic().creaSalva(currentAssegnazione);

            }
        });
        btnSalva.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        btnAnnulla = new Button("Annulla");
        btnAnnulla.setId("button-annulla");
        btnAnnulla.setWidth("100%");
        btnAnnulla.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAnnulla.addClickListener(e-> view.getLogic().annullaOperazione());
        btnAnnulla.addClickShortcut(Key.KEY_E, KeyModifier.CONTROL);

        btnElimina = new Button("Elimina");
        btnElimina.setId("button-elimina");
        btnElimina.setWidth("100%");
        btnElimina.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        btnElimina.addClickListener(e-> {
            if(currentAssegnazione != null)
                view.getLogic().creaElimina(currentAssegnazione);
        });

        VerticalLayout buttons = new VerticalLayout();
        buttons.add(btnSalva, btnAnnulla, btnElimina);

        content.setColspan(docente, 3);
        content.setColspan(buttons, 3);
        content.add(buttons);

        add(content);
    }

    public void modificaItem(AssegnazioneRecordObj item) {
        boolean nuovo = false;
        if (item == null) {
            nuovo = true;
            item = new AssegnazioneRecordObj();
            item.setAssegnazioneId(-1);
            btnAnnulla.setEnabled(true);
        }
        btnElimina.setVisible(!nuovo);
        currentAssegnazione = item;
        binder.readBean(item);
    }
}