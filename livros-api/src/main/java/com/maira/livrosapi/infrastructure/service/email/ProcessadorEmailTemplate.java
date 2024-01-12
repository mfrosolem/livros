package com.maira.livrosapi.infrastructure.service.email;

import com.maira.livrosapi.domain.service.EnvioEmailService.Mensagem;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Component
public class ProcessadorEmailTemplate {

    private final Configuration freemarkerConfig;

    public ProcessadorEmailTemplate(Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }

    protected String processarTemplate(Mensagem mensagem) {
        try {
            Template template = freemarkerConfig.getTemplate(mensagem.getCorpo());
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, mensagem.getVariaveis());
        } catch (Exception ex) {
            throw new EmailException("Não foi possível montar template do e-mail.", ex);
        }
    }
}
