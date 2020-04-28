package it.fabio.tesi.about;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Version;
import it.fabio.tesi.MainLayout;
import it.fabio.tesi.utils.GestioneConst;

@Route(value = GestioneConst.PAGE_ABOUT, layout = MainLayout.class)
@PageTitle(GestioneConst.TITLE_ABOUT)
public class AboutView extends VerticalLayout {

    public AboutView() {
        add(VaadinIcon.INFO_CIRCLE.create());
        add(new Span(" PWA sviluppata con Vaadin "
                + Version.getFullVersion() + ". e "
                + "JOOQ 3.12.3 come progetto di tesi per "
                + "l'Università degli Studi di Bergamo."));

        add(new Span("Matricola: 1042015"));
        add(new Span("E-Mail: f.pedrini1@studenti.unibg.it"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }
}