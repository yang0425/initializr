package io.spring.initializr.generator.spring.web.dto;

import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.java.JavaConstructorDeclaration;
import io.spring.initializr.generator.language.java.JavaExpressionStatement;
import io.spring.initializr.generator.language.java.JavaFieldDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaReturnStatement;
import io.spring.initializr.generator.language.java.JavaStringExpression;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.web.MainCodeContributor;
import java.lang.reflect.Modifier;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

public class DtoMainContributorConfiguration {

    private static final String CLASS_NAME = "HelloWorldDto";

    private final String packageName;

    public DtoMainContributorConfiguration(ProjectDescription description) {
        this.packageName = description.getPackageName() + ".dto";
    }

    @Bean
    public MainCodeContributor<DtoMainCustomizer> dtoMainContributor(
        ObjectProvider<DtoMainCustomizer> dtoMainCustomizers) {
        return new MainCodeContributor<>(packageName, CLASS_NAME, DtoMainCustomizer.class,
            dtoMainCustomizers);
    }

    @Bean
    public DtoMainCustomizer dtoMainTypeCustomizer() {
        return (typeDeclaration) -> typeDeclaration.modifiers(Modifier.PUBLIC);
    }

    @Bean
    public DtoMainCustomizer dtoMainFiledCustomizer() {
        return (typeDeclaration) -> typeDeclaration.addFieldDeclaration(
            JavaFieldDeclaration.field("message")
                .modifiers(Modifier.PRIVATE | Modifier.FINAL)
                .returning("String"));
    }

    @Bean
    public DtoMainCustomizer dtoMainConstructorCustomizer() {
        return (typeDeclaration) -> typeDeclaration.addConstructorDeclaration(
            JavaConstructorDeclaration.constructor()
                .modifiers(Modifier.PUBLIC)
                .parameters(new Parameter("String", "message"))
                .body(new JavaExpressionStatement(new JavaStringExpression("this.message = message"))));
    }

    @Bean
    public DtoMainCustomizer dtoMainGetMethodCustomizer() {
        return (typeDeclaration) -> typeDeclaration.addMethodDeclaration(
            JavaMethodDeclaration.method("getMessage").modifiers(Modifier.PUBLIC)
                .returning("String")
                .body(new JavaReturnStatement(new JavaStringExpression("message"))));
    }

}
