package it.fabio.tesi.user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Anchor;
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
import it.fabio.tesi.assegnazioni.AssegnazioniView;
import it.fabio.tesi.generated.tables.records.DocenteRecord;
import it.fabio.tesi.support.AssegnazioneRecordObj;
import it.fabio.tesi.utils.GestioneConst;

import java.util.ArrayList;

@Route(value = GestioneConst.PAGE_DOCENTI_DETTAGLI, layout = MainLayout.class)
@PageTitle(GestioneConst.TITLE_DOCENTI_DETTAGLI)
public class UserView extends VerticalLayout implements HasUrlParameter<String> {
    DatabaseDataService service = new DatabaseDataService();
    private UserLogic viewLogic = new UserLogic(this, service);
    private Span spanNome = new Span();
    private Span spanCognome = new Span();
    private Span spanEmail = new Span();
    private Span spanTelefono = new Span();
    private VerticalLayout left = new VerticalLayout();
    private VerticalLayout right = new VerticalLayout();
    public UserView() throws ClassNotFoundException {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addAndExpand(left, right);

        Icon userIcon = new Icon(VaadinIcon.USER_CARD);
        userIcon.setSize("128px");
        left.add(userIcon);

        left.add(userIcon);

        left.add(spanNome);
        left.add(spanCognome);
        left.add(spanEmail);
        left.add(spanTelefono);

        addAndExpand(layout);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.START);
    }

    public void creaNotifica(String msg, boolean isError){
        Notification notifica = new Notification();
        notifica.add(new Span(msg));
        if(isError)
            notifica.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notifica.setDuration(3000);
        notifica.open();
    }

    public void updateComponenti(ArrayList<AssegnazioneRecordObj> lista){
        String currentYear = "";
        ArrayList<Component> currentCorsi = new ArrayList<>();
        int index = 0;
        for(AssegnazioneRecordObj a : lista){
            if((!currentYear.equals(a.getCorsoRecord().getAnno()))
                    && !currentYear.isEmpty()){
                if(currentCorsi.size() > 0) {
                    this.right.add(creaAccordion(currentYear, currentCorsi));
                    currentCorsi.clear();
                    currentYear = a.getCorsoRecord().getAnno();
                }
            }else{
                currentYear = a.getCorsoRecord().getAnno();
            }

            currentCorsi.add(formatDettagli(a));
            index++;

            if(index == lista.size()){
                if(currentCorsi.size() > 0) {
                    this.right.add(creaAccordion(currentYear, currentCorsi));
                    currentCorsi.clear();
                    currentYear = a.getCorsoRecord().getAnno();
                }
            }
        }
    }

    private Accordion creaAccordion(String titolo, ArrayList<Component> listaCmp){
        VerticalLayout layout = new VerticalLayout();;
        Accordion anno = new Accordion();
        for (Component c : listaCmp)
            layout.add(c);
        anno.add(titolo, layout);
        return anno;
    }

    public void updateInfo(DocenteRecord a){
        spanNome.setText(a.getNome());
        spanCognome.setText(a.getCognome());
        spanEmail.setText(a.getEmail());
        spanTelefono.setText(a.getTelefono());
    }

    private Details formatDettagli(AssegnazioneRecordObj a){
        String titolo =  "[" + a.getCorsoRecord().getCodice() + "] " + a.getCorsoRecord().getDenominazione();
        Anchor dettagli = new Anchor(RouteConfiguration.forSessionScope()
                .getUrl(AssegnazioniView.class, a.getCorsoRecord().getIdcorso().toString()),
                a.getAssegnazioneRecordTipologia() + ", " + a.getAssegnazioneRecordContratto() + ", "
                + a.getAssegnazioneOre() + " ore");
        return new Details(titolo, dettagli);
    };

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}