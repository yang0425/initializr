package io.spring.initializr.generator.spring.web.repository;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaReturnStatement;
import io.spring.initializr.generator.language.java.JavaStringExpression;
import io.spring.initializr.generator.language.java.JavaTypeDeclaration;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import java.lang.reflect.Modifier;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("web")
public class RepositoryGenerationConfiguration {

    private final ProjectDescription description;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RepositoryGenerationConfiguration(ProjectDescription description) {
        this.description = description;
    }

    @Bean
    public RepositoryMainContributor repositoryMainContributor(
        ObjectProvider<RepositoryMainCustomizer<?>> repositoryMainCustomizers) {
        return new RepositoryMainContributor(this.description.getPackageName() + ".repository",
            repositoryMainCustomizers);
    }

    @Bean
    public RepositoryMainCustomizer<TypeDeclaration> repositoryMainAnnotationCustomizer() {
        return (typeDeclaration) -> typeDeclaration
            .annotate(Annotation.name("org.springframework.stereotype.Repository"));
    }

    @Bean
    public RepositoryMainCustomizer<JavaTypeDeclaration> repositoryMainMethodCustomizer() {
        return (typeDeclaration) -> {
            typeDeclaration.modifiers(Modifier.PUBLIC);
            typeDeclaration.addMethodDeclaration(
                JavaMethodDeclaration.method("getHelloWorld").modifiers(Modifier.PUBLIC)
                    .returning("String")
                    .body(new JavaReturnStatement(
                        new JavaStringExpression("\"Hello World!\""))));
        };
    }

}
