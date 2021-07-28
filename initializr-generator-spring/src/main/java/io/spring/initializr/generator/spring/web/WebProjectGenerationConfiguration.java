package io.spring.initializr.generator.spring.web;

import io.spring.initializr.generator.condition.ConditionalOnLanguage;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.language.java.JavaCompilationUnit;
import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.language.java.JavaSourceCode;
import io.spring.initializr.generator.language.java.JavaSourceCodeWriter;
import io.spring.initializr.generator.language.java.JavaTypeDeclaration;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.code.TestSourceCodeCustomizer;
import io.spring.initializr.generator.spring.web.controller.ControllerMainContributorConfiguration;
import io.spring.initializr.generator.spring.web.controller.ControllerTestContributorConfiguration;
import io.spring.initializr.generator.spring.web.dto.DtoMainContributorConfiguration;
import io.spring.initializr.generator.spring.web.entity.EntityMainContributorConfiguration;
import io.spring.initializr.generator.spring.web.repository.RepositoryMainContributorConfiguration;
import io.spring.initializr.generator.spring.web.repository.RepositoryTestContributorConfiguration;
import io.spring.initializr.generator.spring.web.service.ServiceMainContributorConfiguration;
import io.spring.initializr.generator.spring.web.service.ServiceTestContributorConfiguration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("web")
@ConditionalOnLanguage(JavaLanguage.ID)
@Import({ ControllerMainContributorConfiguration.class, ControllerTestContributorConfiguration.class,
		ServiceMainContributorConfiguration.class, ServiceTestContributorConfiguration.class,
		RepositoryMainContributorConfiguration.class, RepositoryTestContributorConfiguration.class,
		EntityMainContributorConfiguration.class, DtoMainContributorConfiguration.class })
public class WebProjectGenerationConfiguration {

	private final ProjectDescription description;

	private final IndentingWriterFactory indentingWriterFactory;

	public WebProjectGenerationConfiguration(ProjectDescription description,
			IndentingWriterFactory indentingWriterFactory) {
		this.description = description;
		this.indentingWriterFactory = indentingWriterFactory;
	}

	@Bean
	public TestSourceCodeProjectContributor<JavaTypeDeclaration, JavaCompilationUnit, JavaSourceCode> testJavaSourceCodeProjectContributor(
			ObjectProvider<TestSourceCodeCustomizer<?, ?, ?>> testSourceCodeCustomizers) {
		return new TestSourceCodeProjectContributor<>(this.description, JavaSourceCode::new,
				new JavaSourceCodeWriter(this.indentingWriterFactory), testSourceCodeCustomizers);
	}

}
