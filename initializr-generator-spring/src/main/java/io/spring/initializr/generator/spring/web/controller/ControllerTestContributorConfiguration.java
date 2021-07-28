package io.spring.initializr.generator.spring.web.controller;

import java.lang.reflect.Modifier;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.java.JavaEmptyLineStatement;
import io.spring.initializr.generator.language.java.JavaExpressionStatement;
import io.spring.initializr.generator.language.java.JavaFieldDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodInvocation;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.web.TestCodeContributor;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

public class ControllerTestContributorConfiguration {

	private static final String CLASS_NAME = "HelloWorldControllerTest";

	private static final String CONTROLLER_FIELD_NAME = "helloWorldController";

	private static final String SERVICE_FIELD_NAME = "helloWorldService";

	private final String packageName;

	private final String serviceClass;

	public ControllerTestContributorConfiguration(ProjectDescription description) {
		this.packageName = description.getPackageName() + ".controller";
		this.serviceClass = String.format("%s.service.HelloWorldService", description.getPackageName());
	}

	@Bean
	public TestCodeContributor<ControllerTestCustomizer> controllerTestCodeContributor(
			ObjectProvider<ControllerTestCustomizer> codeCustomizers) {
		return new TestCodeContributor<>(this.packageName, CLASS_NAME, ControllerTestCustomizer.class, codeCustomizers);
	}

	@Bean
	public ControllerTestCustomizer controllerTestAnnotationCustomizer() {
		return (typeDeclaration) -> typeDeclaration.annotate(Annotation.name(
				"org.junit.jupiter.api.extension.ExtendWith",
				((builder) -> builder.attribute("value", Class.class, "org.mockito.junit.jupiter.MockitoExtension"))));
	}

	@Bean
	public ControllerTestCustomizer controllerTestFieldCustomizer() {
		return (typeDeclaration) -> {
			JavaFieldDeclaration serviceField = JavaFieldDeclaration.field(SERVICE_FIELD_NAME)
					.modifiers(Modifier.PRIVATE).returning(this.serviceClass);
			serviceField.annotate(Annotation.name("org.mockito.Mock"));
			typeDeclaration.addFieldDeclaration(serviceField);

			JavaFieldDeclaration controllerField = JavaFieldDeclaration.field(CONTROLLER_FIELD_NAME)
					.modifiers(Modifier.PRIVATE).returning("HelloWorldController");
			controllerField.annotate(Annotation.name("org.mockito.InjectMocks"));
			typeDeclaration.addFieldDeclaration(controllerField);
		};
	}

	@Bean
	public ControllerTestCustomizer controllerTestMethodCustomizer() {
		return (typeDeclaration) -> {
			JavaMethodDeclaration methodDeclaration = JavaMethodDeclaration.method("shouldCallServiceAndGetHelloWorld")
					.returning("void")
					.body(new JavaExpressionStatement(new JavaMethodInvocation(CONTROLLER_FIELD_NAME, "helloWorld")),
							new JavaEmptyLineStatement(), new JavaExpressionStatement(
									new JavaMethodInvocation(new JavaMethodInvocation("org.mockito.Mockito.verify",
											"verify", "helloWorldService"), "getHelloWorld")));
			methodDeclaration.annotate(Annotation.name("org.junit.jupiter.api.Test"));
			typeDeclaration.addMethodDeclaration(methodDeclaration);
		};
	}

}
