package it.fabio.tesi.home;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import it.fabio.tesi.DatabaseDataService;
import it.fabio.tesi.MainLayout;
import it.fabio.tesi.support.CorsodilaureaRecordObj;
import it.fabio.tesi.utils.GestioneConst;

import java.util.ArrayList;

@Route(value = GestioneConst.PAGE_ROOT, layout = MainLayout.class)
@PageTitle(GestioneConst.TITLE_ROOT)
public class HomeView extends VerticalLayout {
    private DatabaseDataService service = new DatabaseDataService();
    private HomeGrid grid = new HomeGrid();
    private VerticalLayout welcomeLayout;

    public HomeView() throws ClassNotFoundException {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        HorizontalLayout menu = creaRicerca();
        add(menu);
        grid.setId("grid-cdl");
        add(grid);
        grid.setVisible(false);

        welcomeLayout = creaWelcome();
        add(welcomeLayout);
    }

    private HorizontalLayout creaRicerca(){
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        layout.setJustifyContentMode(JustifyContentMode.END);
        ComboBox<CorsodilaureaRecordObj> cdCdl = new ComboBox<>();
        cdCdl.setId("combobox-cdl");
        ArrayList<CorsodilaureaRecordObj> list = new ArrayList<>(service.getAllCdl());
        cdCdl.setItems(list);
        cdCdl.setValue(null);
        cdCdl.setAllowCustomValue(false);
        cdCdl.setClearButtonVisible(true);
        cdCdl.setPlaceholder("Mostra un corso di laurea");
        cdCdl.addValueChangeListener(e ->{
            if(e.getValue() != null) {
                welcomeLayout.setVisible(false);
                grid.setVisible(true);
                grid.setItems(service.getAllCorsiByCdl(e.getValue()));
            }else{
                welcomeLayout.setVisible(true);
                grid.setVisible(false);
            }
        });
        cdCdl.setItemLabelGenerator(CorsodilaureaRecordObj::toString);
        layout.addAndExpand(cdCdl);
        layout.setVerticalComponentAlignment(Alignment.START, cdCdl);

        return layout;
    }

    public VerticalLayout creaWelcome() {
        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
        vl.add(new Span("Benvenuto nella pagina principale."));
        vl.add(new Span("Per visualizzare l'anteprima di un corso di laurea" +
                " selezionane uno dal combobox qui sopra."));
        vl.add(new Span("Per creare un nuovo corso di laurea e/o modificare le sue informazioni" +
                " accedi alle varie sezioni dal menù laterale."));
        return vl;
    }
}