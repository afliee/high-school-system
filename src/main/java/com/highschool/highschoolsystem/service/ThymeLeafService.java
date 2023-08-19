package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.util.mail.EmailDetails;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Service
@AllArgsConstructor
public class ThymeLeafService {
    public static final String EMAIL_TEMPLATE = "email/email-template";
    public static final String TEMPLATE_PREFIX = "/templates/";
    public static final String TEMPLATE_SUFFIX = ".html";
    public static final String UTF_8 = "UTF-8";

    private static final TemplateEngine templateEngine;

    static {
        templateEngine = getTemplateEngine();
    }

    public static TemplateEngine getTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(getTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(getResourceBundleMessageSource());
        return templateEngine;
    }

    private static ITemplateResolver getTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(TEMPLATE_PREFIX);
        templateResolver.setSuffix(TEMPLATE_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(UTF_8);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    public static ResourceBundleMessageSource getResourceBundleMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(EMAIL_TEMPLATE);
        return messageSource;
    }

    public String getContent(EmailDetails emailDetails) {
        Context context = new Context();
        context.setVariable("name", emailDetails.getTo());
        context.setVariable("content", emailDetails.getContent());
        context.setVariable("subject", emailDetails.getSubject());

        return getTemplateEngine().process(EMAIL_TEMPLATE, context);
    }
}
