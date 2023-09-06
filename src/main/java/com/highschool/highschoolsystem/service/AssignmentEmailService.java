package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.entity.AssignmentEntity;
import com.highschool.highschoolsystem.entity.Submitting;
import com.highschool.highschoolsystem.util.mail.EmailDetails;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class AssignmentEmailService {
    public static final String EMAIL_TEMPLATE = "email/assignment-template";
    private static final String GRADING_TEMPLATE = "email/grading-template";
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

    public String getContent(EmailDetails emailDetails, Object content) {
        if (!(content instanceof AssignmentEntity)) {
            throw new RuntimeException("Content must be instance of AssignmentEntity");
        }
        AssignmentEntity assignmentEntity = (AssignmentEntity) content;
        Context context = new Context();

        context.setVariable("name", emailDetails.getTo());
        context.setVariable("teacher", assignmentEntity.getTeacher());
        context.setVariable("title", assignmentEntity.getTitle());
        context.setVariable("url", assignmentEntity.getAttachment());
        context.setVariable("subject", emailDetails.getSubject());
        context.setVariable("description", assignmentEntity.getDescription());

        return getTemplateEngine().process(EMAIL_TEMPLATE, context);
    }

    public String getContent(EmailDetails emailDetails, Submitting submitting) {
        Context context = new Context();

        context.setVariable("name", emailDetails.getTo());
        context.setVariable("teacher", submitting.getAssignment().getTeacher());
        context.setVariable("title", submitting.getAssignment().getTitle());
        context.setVariable("url", submitting.getAssignment().getAttachment());
        context.setVariable("subject", emailDetails.getSubject());
        context.setVariable("comment", submitting.getComment());
        context.setVariable("score", submitting.getScore());
        context.setVariable("totalPoint", submitting.getAssignment().getPoints());

        return getTemplateEngine().process(GRADING_TEMPLATE, context);
    }
}
