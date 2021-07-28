package io.spring.initializr.generator.spring.web.service;

import java.lang.reflect.Modifier;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.java.JavaEmptyLineStatement;
import io.spring.initializr.generator.language.java.JavaExpressionStatement;
import io.spring.initializr.generator.language.java.JavaFieldDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodInvocation;
import io.spring.initializr.generator.language.java.JavaNewInstanceExpression;
import io.spring.initializr.generator.language.java.JavaVariableExpressionStatement;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.web.TestCodeContributor;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

public class ServiceTestContributorConfiguration {

	private static final String CLASS_NAME = "HelloWorldServiceTest";

	private static final String SERVICE_FIELD_NAME = "helloWorldService";

	private static final String REPOSITORY_FIELD_NAME = "helloWorldRepository";

	private final String packageName;

	private final String repositoryClass;

	public ServiceTestContributorConfiguration(ProjectDescription description) {
		this.packageName = description.getPackageName() + ".service";
		this.repositoryClass = String.format("%s.repository.HelloWorldRepository", description.getPackageName());
	}

	@Bean
	public TestCodeContributor<ServiceTestCustomizer> serviceTestCodeContributor(
			ObjectProvider<ServiceTestCustomizer> codeCustomizers) {
		return new TestCodeContributor<>(this.packageName, CLASS_NAME, ServiceTestCustomizer.class, codeCustomizers);
	}

	@Bean
	public ServiceTestCustomizer serviceTestAnnotationCustomizer() {
		return (typeDeclaration) -> typeDeclaration.annotate(Annotation.name(
				"org.junit.jupiter.api.extension.ExtendWith",
				((builder) -> builder.attribute("value", Class.class, "org.mockito.junit.jupiter.MockitoExtension"))));
	}

	@Bean
	public ServiceTestCustomizer serviceTestFieldCustomizer() {
		return (typeDeclaration) -> {
			JavaFieldDeclaration repositoryField = JavaFieldDeclaration.field(REPOSITORY_FIELD_NAME)
					.modifiers(Modifier.PRIVATE).returning(this.repositoryClass);
			repositoryField.annotate(Annotation.name("org.mockito.Mock"));
			typeDeclaration.addFieldDeclaration(repositoryField);

			JavaFieldDeclaration serviceField = JavaFieldDeclaration.field(SERVICE_FIELD_NAME)
					.modifiers(Modifier.PRIVATE).returning("HelloWorldService");
			serviceField.annotate(Annotation.name("org.mockito.InjectMocks"));
			typeDeclaration.addFieldDeclaration(serviceField);
		};
	}

	@Bean
	public ServiceTestCustomizer serviceTestMethodCustomizer() {
		return (typeDeclaration) -> {
			JavaMethodInvocation given = new JavaMethodInvocation("org.mockito.BDDMockito.given", "given");
			given.addArgument(new JavaMethodInvocation("helloWorldRepository", "getHelloWorld"));
			JavaMethodInvocation willReturn = new JavaMethodInvocation(given, "willReturn");
			willReturn.addArgument("helloWorld");

			JavaMethodInvocation assertThat = new JavaMethodInvocation("org.assertj.core.api.Assertions.assertThat",
					"assertThat");
			assertThat.addArgument(new JavaMethodInvocation("helloWorldDto", "getMessage"));
			JavaMethodInvocation isEqualTo = new JavaMethodInvocation(assertThat, "isEqualTo");
			isEqualTo.addArgument("\"Hello World Test\"");

			JavaMethodDeclaration methodDeclaration = JavaMethodDeclaration.method("shouldGetMessageFromRepository")
					.body(new JavaVariableExpressionStatement("com.example.demo.entity.HelloWorld", "helloWorld",
							new JavaNewInstanceExpression("HelloWorld")),
							new JavaExpressionStatement(
									new JavaMethodInvocation("helloWorld", "setMessage", "\"Hello World Test\"")),
							new JavaExpressionStatement(willReturn), new JavaEmptyLineStatement(),
							new JavaVariableExpressionStatement("com.example.demo.dto.HelloWorldDto", "helloWorldDto",
									new JavaMethodInvocation("helloWorldService", "getHelloWorld")),
							new JavaEmptyLineStatement(), new JavaExpressionStatement(isEqualTo));
			methodDeclaration.annotate(Annotation.name("org.junit.jupiter.api.Test"));
			typeDeclaration.addMethodDeclaration(methodDeclaration);
		};
	}

}
