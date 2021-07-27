package io.spring.initializr.generator.spring.web.controller;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.java.JavaConstructorDeclaration;
import io.spring.initializr.generator.language.java.JavaExpressionStatement;
import io.spring.initializr.generator.language.java.JavaFieldDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodInvocation;
import io.spring.initializr.generator.language.java.JavaReturnStatement;
import io.spring.initializr.generator.language.java.JavaStringExpression;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.web.MainCodeContributor;
import java.lang.reflect.Modifier;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

public class ControllerMainContributorConfiguration {

    private static final String CLASS_NAME = "HelloWorldController";
    private static final String SERVICE_FIELD_NAME = "helloWorldService";

    private final String packageName;
    private final String serviceClass;
    private final String dtoClass;

    public ControllerMainContributorConfiguration(ProjectDescription description) {
        this.packageName = description.getPackageName() + ".controller";
        this.serviceClass = String.format("%s.service.HelloWorldService", description.getPackageName());
        this.dtoClass = String.format("%s.dto.HelloWorldDto", description.getPackageName());
    }

    @Bean
    public MainCodeContributor<ControllerMainCustomizer> controllerMainCodeContributor(
        ObjectProvider<ControllerMainCustomizer> codeCustomizers) {
        return new MainCodeContributor<>(packageName, CLASS_NAME, ControllerMainCustomizer.class, codeCustomizers);
    }

    @Bean
    public ControllerMainCustomizer controllerMainAnnotationCustomizer() {
        return (typeDeclaration) -> {
            typeDeclaration.modifiers(Modifier.PUBLIC);
            typeDeclaration.annotate(Annotation.name("org.springframework.web.bind.annotation.RestController"));
            typeDeclaration.annotate(Annotation.name("org.springframework.web.bind.annotation.RequestMapping",
                builder -> builder.attribute("value", String.class, "helloWorld")));
        };
    }

    @Bean
    public ControllerMainCustomizer controllerMainFieldCustomizer() {
        return (typeDeclaration) -> typeDeclaration.addFieldDeclaration(
            JavaFieldDeclaration.field(SERVICE_FIELD_NAME)
                .modifiers(Modifier.PRIVATE | Modifier.FINAL)
                .returning(serviceClass));
    }

    @Bean
    public ControllerMainCustomizer controllerMainConstructorCustomizer() {
        return (typeDeclaration) -> typeDeclaration.addConstructorDeclaration(JavaConstructorDeclaration.constructor()
            .modifiers(Modifier.PUBLIC).parameters(new Parameter(serviceClass, SERVICE_FIELD_NAME))
            .body(new JavaExpressionStatement(
                new JavaStringExpression(String.format("this.%s = %s", SERVICE_FIELD_NAME, SERVICE_FIELD_NAME)))));
    }

    @Bean
    public ControllerMainCustomizer controllerMainMethodCustomizer() {
        return (typeDeclaration) -> {
            JavaMethodDeclaration methodDeclaration = JavaMethodDeclaration.method("helloWorld")
                .modifiers(Modifier.PUBLIC)
                .returning(dtoClass)
                .body(new JavaReturnStatement(new JavaMethodInvocation(SERVICE_FIELD_NAME, "getHelloWorld")));
            methodDeclaration.annotate(Annotation.name("org.springframework.web.bind.annotation.GetMapping"));
            typeDeclaration.addMethodDeclaration(methodDeclaration);
        };
    }

}
