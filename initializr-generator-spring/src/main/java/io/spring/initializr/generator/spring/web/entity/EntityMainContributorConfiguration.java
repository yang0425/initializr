package io.spring.initializr.generator.spring.web.entity;

import java.lang.reflect.Modifier;

import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.java.JavaExpressionStatement;
import io.spring.initializr.generator.language.java.JavaFieldDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaReturnStatement;
import io.spring.initializr.generator.language.java.JavaStringExpression;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.web.MainCodeContributor;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

public class EntityMainContributorConfiguration {

	private static final String CLASS_NAME = "HelloWorld";

	private final String packageName;

	public EntityMainContributorConfiguration(ProjectDescription description) {
		this.packageName = description.getPackageName() + ".entity";
	}

	@Bean
	public MainCodeContributor<EntityMainCustomizer> entityMainContributor(
			ObjectProvider<EntityMainCustomizer> entityMainCustomizers) {
		return new MainCodeContributor<>(this.packageName, CLASS_NAME, EntityMainCustomizer.class,
				entityMainCustomizers);
	}

	@Bean
	public EntityMainCustomizer entityMainTypeCustomizer() {
		return (typeDeclaration) -> typeDeclaration.modifiers(Modifier.PUBLIC);
	}

	@Bean
	public EntityMainCustomizer entityMainFiledCustomizer() {
		return (typeDeclaration) -> typeDeclaration.addFieldDeclaration(
				JavaFieldDeclaration.field("message").modifiers(Modifier.PRIVATE).returning("String"));
	}

	@Bean
	public EntityMainCustomizer entityMainGetMethodCustomizer() {
		return (typeDeclaration) -> typeDeclaration
				.addMethodDeclaration(JavaMethodDeclaration.method("getMessage").modifiers(Modifier.PUBLIC)
						.returning("String").body(new JavaReturnStatement(new JavaStringExpression("message"))));
	}

	@Bean
	public EntityMainCustomizer entityMainSetMethodCustomizer() {
		return (typeDeclaration) -> typeDeclaration.addMethodDeclaration(JavaMethodDeclaration.method("setMessage")
				.modifiers(Modifier.PUBLIC).parameters(new Parameter("String", "message")).returning("void")
				.body(new JavaExpressionStatement(new JavaStringExpression("this.message = message"))));
	}

}
