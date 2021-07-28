package io.spring.initializr.generator.spring.web.repository;

import java.lang.reflect.Modifier;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.java.JavaEmptyLineStatement;
import io.spring.initializr.generator.language.java.JavaExpressionStatement;
import io.spring.initializr.generator.language.java.JavaFieldDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodInvocation;
import io.spring.initializr.generator.language.java.JavaVariableExpressionStatement;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.web.TestCodeContributor;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

public class RepositoryTestContributorConfiguration {

	private static final String CLASS_NAME = "HelloWorldRepositoryTest";

	private final String packageName;

	public RepositoryTestContributorConfiguration(ProjectDescription description) {
		this.packageName = description.getPackageName() + ".repository";
	}

	@Bean
	public TestCodeContributor<RepositoryTestCustomizer> repositoryTestCodeContributor(
			ObjectProvider<RepositoryTestCustomizer> codeCustomizers) {
		return new TestCodeContributor<>(this.packageName, CLASS_NAME, RepositoryTestCustomizer.class, codeCustomizers);
	}

	@Bean
	public RepositoryTestCustomizer repositoryTestFieldCustomizer() {
		return (typeDeclaration) -> typeDeclaration.addFieldDeclaration(
				JavaFieldDeclaration.field("helloWorldRepository").modifiers(Modifier.PRIVATE | Modifier.FINAL)
						.value("new HelloWorldRepository()").returning("HelloWorldRepository"));
	}

	@Bean
	public RepositoryTestCustomizer repositoryTestMethodCustomizer() {
		return (typeDeclaration) -> {
			JavaMethodInvocation assertThat = new JavaMethodInvocation("org.assertj.core.api.Assertions.assertThat",
					"assertThat");
			assertThat.addArgument(new JavaMethodInvocation("helloWorld", "getMessage"));
			JavaMethodInvocation isEqualTo = new JavaMethodInvocation(assertThat, "isEqualTo");
			isEqualTo.addArgument("\"Hello World!\"");

			JavaMethodDeclaration methodDeclaration = JavaMethodDeclaration.method("shouldReturnHelloWorld")
					.body(new JavaVariableExpressionStatement("com.example.demo.entity.HelloWorld", "helloWorld",
							new JavaMethodInvocation("helloWorldRepository", "getHelloWorld")),
							new JavaEmptyLineStatement(), new JavaExpressionStatement(isEqualTo));
			methodDeclaration.annotate(Annotation.name("org.junit.jupiter.api.Test"));
			typeDeclaration.addMethodDeclaration(methodDeclaration);
		};
	}

}
