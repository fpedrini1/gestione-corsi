package it.fabio.tesi.about;

import com.vaadin.flow.component.html.testbench.SpanElement;
import com.vaadin.testbench.TestBenchTestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;

public class AboutViewIT extends TestBenchTestCase {
    @Before
    public void setup() {
        setDriver(new ChromeDriver());
        getDriver().get("http://localhost:8080/info/");
    }

    @Test
    public void verificaVersione() {
        SpanElement span = $(SpanElement.class).first();
        Assert.assertTrue("La parola 'Vaadin' è contenuta nel messaggio della versione? ",
                span.getText().contains("Vaadin"));
    }

    @Test
    public void verificaMatricola() {
        SpanElement span = $(SpanElement.class).get(1);
        Assert.assertTrue("La matricola è 1042015 ed è il primo componente label?",
                span.getText().contains("1042015"));
    }

    @Test
    public void verificaEmail() {
        SpanElement span = $(SpanElement.class).last();
        Assert.assertTrue("L'e-mail è f.pedrini1@studenti.unibg.it ed è l'ultimo componente label?",
                span.getText().contains("f.pedrini1@studenti.unibg.it"));
    }

    @After
    public void tearDown() {
        getDriver().quit();
    }
}
