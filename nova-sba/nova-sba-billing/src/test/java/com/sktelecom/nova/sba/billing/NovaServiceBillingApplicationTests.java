package com.sktelecom.nova.sba.billing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@SpringBootTest
class NovaServiceBillingApplicationTests {

    @Test
    void contextLoads() {
        ApplicationModules modules = ApplicationModules.of("com.sktelecom.nova");

        modules.forEach(System.out::println);

        modules.verify();
    }

    @Test
    void documentation() {
        new Documenter(ApplicationModules.of("com.sktelecom.nova"))
                .writeDocumentation()
                .writeAggregatingDocument()
                .writeModulesAsPlantUml(Documenter.DiagramOptions.defaults()
                        .withStyle(Documenter.DiagramOptions.DiagramStyle.UML))
                .writeIndividualModulesAsPlantUml(Documenter.DiagramOptions.defaults()
                        .withStyle(Documenter.DiagramOptions.DiagramStyle.UML))
                .writeModuleCanvases();

        Documenter.DiagramOptions.defaults()
                .withStyle(Documenter.DiagramOptions.DiagramStyle.UML);
    }

}


