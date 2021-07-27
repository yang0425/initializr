package io.spring.initializr.generator.spring.web.controller;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.language.java.JavaConstructorDeclaration;
import io.spring.initializr.generator.language.java.JavaExpressionStatement;
import io.spring.initializr.generator.language.java.JavaFieldDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodInvocation;
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
public class ControllerGenerationConfiguration {

    private final ProjectDescription description;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ControllerGenerationConfiguration(ProjectDescription description) {
        this.description = description;
    }

    @Bean
    public ControllerMainContributor controllerMainContributor(
        ObjectProvider<ControllerMainCustomizer<?>> controllerMainCustomizers) {
        return new ControllerMainContributor(this.description.getPackageName() + ".controller",
            controllerMainCustomizers);
    }

    @Bean
    public ControllerMainCustomizer<TypeDeclaration> controllerMainAnnotationCustomizer() {
        return (typeDeclaration) -> {
            typeDeclaration.annotate(Annotation.name("org.springframework.web.bind.annotation.RestController"));
            typeDeclaration.annotate(Annotation.name("org.springframework.web.bind.annotation.RequestMapping",
                builder -> builder.attribute("value", String.class, "helloWorld")));
        };
    }

    @Bean
    public ControllerMainCustomizer<JavaTypeDeclaration> controllerMainMethodCustomizer() {
        return (typeDeclaration) -> {
            String serviceClass = String.format("%s.service.HelloWorldService", this.description.getPackageName());
            String serviceFiledName = "helloWorldService";
            typeDeclaration.modifiers(Modifier.PUBLIC);
            typeDeclaration.addFieldDeclaration(JavaFieldDeclaration.field(serviceFiledName)
                .modifiers(Modifier.PRIVATE | Modifier.FINAL)
                .returning(serviceClass));
            typeDeclaration.addConstructorDeclaration(JavaConstructorDeclaration.constructor()
                .modifiers(Modifier.PUBLIC).parameters(new Parameter(serviceClass, serviceFiledName))
                .body(new JavaExpressionStatement(
                    new JavaStringExpression(String.format("this.%s = %s", serviceFiledName, serviceFiledName)))));
            JavaMethodDeclaration methodDeclaration = JavaMethodDeclaration.method("helloWorld")
                .modifiers(Modifier.PUBLIC)
                .returning("String")
                .body(new JavaReturnStatement(
                    new JavaMethodInvocation(serviceFiledName, "getHelloWorld")));
            methodDeclaration.annotate(Annotation.name("org.springframework.web.bind.annotation.GetMapping"));
            typeDeclaration.addMethodDeclaration(methodDeclaration);
        };
    }

}
