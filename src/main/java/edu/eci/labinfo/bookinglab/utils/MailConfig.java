package edu.eci.labinfo.bookinglab.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

/**
 * Clase que define la configuracion de FreeMarker para el envio de correos
 *
 * @author Daniel Antonio Santanilla
 * @version 1.0
 */
@Component
public class MailConfig {

    /**
     * Configuracion de FreeMarker con la ubicacion de las plantillas
     *
     * @return Configuracion de FreeMarker
     */
    @Primary
    @Bean
    FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath("classpath:/templates");
        return bean;
    }

}
