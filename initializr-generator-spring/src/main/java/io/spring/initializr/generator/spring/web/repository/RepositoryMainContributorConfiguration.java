package io.spring.initializr.generator.spring.web.repository;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaReturnStatement;
import io.spring.initializr.generator.language.java.JavaStringExpression;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.web.MainCodeContributor;
import java.lang.reflect.Modifier;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

public class RepositoryMainContributorConfiguration {

    private static final String CLASS_NAME = "HelloWorldRepository";

    private final String packageName;

    public RepositoryMainContributorConfiguration(ProjectDescription description) {
        this.packageName = description.getPackageName() + ".repository";
    }

    @Bean
    public MainCodeContributor<RepositoryMainCustomizer> repositoryMainContributor(
        ObjectProvider<RepositoryMainCustomizer> repositoryMainCustomizers) {
        return new MainCodeContributor<>(packageName, CLASS_NAME, RepositoryMainCustomizer.class,
            repositoryMainCustomizers);
    }

    @Bean
    public RepositoryMainCustomizer repositoryMainAnnotationCustomizer() {
        return (typeDeclaration) -> {
            typeDeclaration.modifiers(Modifier.PUBLIC);

            typeDeclaration.annotate(Annotation.name("org.springframework.stereotype.Repository"));
        };
    }

    @Bean
    public RepositoryMainCustomizer repositoryMainMethodCustomizer() {
        return (typeDeclaration) -> typeDeclaration.addMethodDeclaration(
            JavaMethodDeclaration.method("getHelloWorld").modifiers(Modifier.PUBLIC)
                .returning("String")
                .body(new JavaReturnStatement(new JavaStringExpression("\"Hello World!\""))));
    }

}
