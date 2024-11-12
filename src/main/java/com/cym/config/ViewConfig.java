package com.cym.config;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.view.freemarker.FreemarkerRender;

/**
 * @author noear 2024/11/12 created
 */
@Configuration
public class ViewConfig {
    @Bean
    public void init(FreemarkerRender render) throws Exception {
        render.getProvider().setSetting("classic_compatible", "true");
        render.getProvider().setSetting("number_format", "0.##");

        if (render.getProviderOfDebug() != null) {
            render.getProviderOfDebug().setSetting("classic_compatible", "true");
            render.getProviderOfDebug().setSetting("number_format", "0.##");
        }
    }
}
