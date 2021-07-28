package io.spring.initializr.generator.spring.web.service;

import java.lang.reflect.Modifier;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.java.JavaConstructorDeclaration;
import io.spring.initializr.generator.language.java.JavaEmptyLineStatement;
import io.spring.initializr.generator.language.java.JavaExpressionStatement;
import io.spring.initializr.generator.language.java.JavaFieldDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodInvocation;
import io.spring.initializr.generator.language.java.JavaReturnStatement;
import io.spring.initializr.generator.language.java.JavaStringExpression;
import io.spring.initializr.generator.language.java.JavaVariableExpressionStatement;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.web.MainCodeContributor;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

public class ServiceMainContributorConfiguration {

	private static final String CLASS_NAME = "HelloWorldService";

	private static final String REPOSITORY_FILED_NAME = "helloWorldRepository";

	private final String packageName;

	private final String repositoryClass;

	private final String entityClass;

	private final String dtoClass;

	public ServiceMainContributorConfiguration(ProjectDescription description) {
		this.packageName = description.getPackageName() + ".service";
		this.repositoryClass = String.format("%s.repository.HelloWorldRepository", description.getPackageName());
		this.entityClass = String.format("%s.entity.HelloWorld", description.getPackageName());
		this.dtoClass = String.format("%s.dto.HelloWorldDto", description.getPackageName());
	}

	@Bean
	public MainCodeContributor<ServiceMainCustomizer> serviceMainContributor(
			ObjectProvider<ServiceMainCustomizer> serviceMainCustomizers) {
		return new MainCodeContributor<>(this.packageName, CLASS_NAME, ServiceMainCustomizer.class,
				serviceMainCustomizers);
	}

	@Bean
	public ServiceMainCustomizer serviceMainAnnotationCustomizer() {
		return (typeDeclaration) -> {
			typeDeclaration.modifiers(Modifier.PUBLIC);

			typeDeclaration.annotate(Annotation.name("org.springframework.stereotype.Service"));
		};
	}

	@Bean
	public ServiceMainCustomizer serviceMainFieldCustomizer() {
		return (typeDeclaration) -> typeDeclaration
				.addFieldDeclaration(JavaFieldDeclaration.field(REPOSITORY_FILED_NAME)
						.modifiers(Modifier.PRIVATE | Modifier.FINAL).returning(this.repositoryClass));
	}

	@Bean
	public ServiceMainCustomizer serviceMainConstructorCustomizer() {
		return (typeDeclaration) -> typeDeclaration.addConstructorDeclaration(JavaConstructorDeclaration.constructor()
				.modifiers(Modifier.PUBLIC).parameters(new Parameter(this.repositoryClass, REPOSITORY_FILED_NAME))
				.body(new JavaExpressionStatement(new JavaStringExpression(
						String.format("this.%s = %s", REPOSITORY_FILED_NAME, REPOSITORY_FILED_NAME)))));
	}

	@Bean
	public ServiceMainCustomizer serviceMainMethodCustomizer() {
		return (typeDeclaration) -> typeDeclaration.addMethodDeclaration(
				JavaMethodDeclaration.method("getHelloWorld").modifiers(Modifier.PUBLIC).returning(this.dtoClass).body(
						new JavaVariableExpressionStatement(this.entityClass, "message",
								new JavaMethodInvocation("helloWorldRepository", "getHelloWorld")),
						new JavaEmptyLineStatement(),
						new JavaReturnStatement(new JavaStringExpression("new HelloWorldDto(message.getMessage())"))));
	}

}
