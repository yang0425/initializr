package io.spring.initializr.generator.spring.web.service;

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
public class ServiceGenerationConfiguration {

    private final ProjectDescription description;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ServiceGenerationConfiguration(ProjectDescription description) {
        this.description = description;
    }

    @Bean
    public ServiceMainContributor serviceMainContributor(
        ObjectProvider<ServiceMainCustomizer<?>> serviceMainCustomizers) {
        return new ServiceMainContributor(this.description.getPackageName() + ".service",
            serviceMainCustomizers);
    }

    @Bean
    public ServiceMainCustomizer<TypeDeclaration> serviceMainAnnotationCustomizer() {
        return (typeDeclaration) -> typeDeclaration
            .annotate(Annotation.name("org.springframework.stereotype.Service"));
    }

    @Bean
    public ServiceMainCustomizer<JavaTypeDeclaration> serviceMainMethodCustomizer() {
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
