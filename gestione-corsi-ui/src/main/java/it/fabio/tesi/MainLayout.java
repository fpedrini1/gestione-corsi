package it.fabio.tesi;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import it.fabio.tesi.about.AboutView;
import it.fabio.tesi.cdl.CdlView;
import it.fabio.tesi.corsi.CorsiView;
import it.fabio.tesi.dipartimenti.DipartimentiView;
import it.fabio.tesi.docenti.DocentiView;
import it.fabio.tesi.home.HomeView;
import it.fabio.tesi.utils.GestioneConst;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsModule("./styles/shared-styles.js")
@PWA(name = GestioneConst.APP_NAME, shortName = GestioneConst.APP_SHORTNAME)
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class MainLayout extends AppLayout {

    private final Tabs menu;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        Image img = new Image();
        img.setSrc("icons/icon.png");
        img.setHeight("44px");
        img.setWidth("44px");
        addToNavbar(true, new DrawerToggle(), img, new Label("Gestione Corsi"));
        menu = createMenuTabs();
        addToDrawer(menu);
    }

    private static Tabs createMenuTabs() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(getAvailableTabs());
        return tabs;
    }

    private static Tab[] getAvailableTabs() {
        final List<Tab> tabs = new ArrayList<>();
        tabs.add(createTab(GestioneConst.TITLE_ROOT, HomeView.class, new Icon(VaadinIcon.HOME)));
        tabs.add(createTab(GestioneConst.TITLE_CDL, CdlView.class, new Icon(VaadinIcon.LIST)));
        tabs.add(createTab(GestioneConst.TITLE_CORSI, CorsiView.class, new Icon(VaadinIcon.ACADEMY_CAP)));
        tabs.add(createTab(GestioneConst.TITLE_DIPARTIMENTI, DipartimentiView.class, new Icon(VaadinIcon.BUILDING)));
        tabs.add(createTab(GestioneConst.TITLE_DOCENTI, DocentiView.class, new Icon(VaadinIcon.USERS)));
        tabs.add(createTab(GestioneConst.TITLE_ABOUT, AboutView.class, new Icon(VaadinIcon.INFO_CIRCLE)));
        return tabs.toArray(new Tab[tabs.size()]);
    }

    private static Tab createTab(String title, Class<? extends Component> viewClass, Icon icon) {
        return createTab(populateLink(new RouterLink(null, viewClass), title, icon));
    }

    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.add(content);
        return tab;
    }

    private static <T extends HasComponents> T populateLink(T a, String title, Icon icon) {
        a.add(icon);
        a.add(title);
        return a;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        selectTab();
    }

    private void selectTab() {
        String target = RouteConfiguration.forSessionScope().getUrl(getContent().getClass());
        Optional<Component> tabToSelect = menu.getChildren().filter(tab -> {
            Component child = tab.getChildren().findFirst().get();
            return child instanceof RouterLink && ((RouterLink) child).getHref().equals(target);
        }).findFirst();
        tabToSelect.ifPresent(tab -> menu.setSelectedTab((Tab) tab));
    }
}