package TestsProyecto;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true, plugin = {"pretty"}, extraGlue = "io.tpd.springbootcucumber.bagcommons")
//@ContextConfiguration(classes = {ConfiguradorContextoCucumber.class})
public class CorrerPruebas {
}
