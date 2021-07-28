package io.spring.initializr.generator.spring.web;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.SourceCodeWriter;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import io.spring.initializr.generator.spring.code.TestSourceCodeCustomizer;
import io.spring.initializr.generator.spring.util.LambdaSafe;

import org.springframework.beans.factory.ObjectProvider;

public class TestSourceCodeProjectContributor<T extends TypeDeclaration, C extends CompilationUnit<T>, S extends SourceCode<T, C>>
		implements ProjectContributor {

	private final ProjectDescription description;

	private final Supplier<S> sourceFactory;

	private final SourceCodeWriter<S> sourceWriter;

	private final ObjectProvider<TestSourceCodeCustomizer<?, ?, ?>> testSourceCodeCustomizers;

	public TestSourceCodeProjectContributor(ProjectDescription description, Supplier<S> sourceFactory,
			SourceCodeWriter<S> sourceWriter,
			ObjectProvider<TestSourceCodeCustomizer<?, ?, ?>> testSourceCodeCustomizers) {
		this.description = description;
		this.sourceFactory = sourceFactory;
		this.sourceWriter = sourceWriter;
		this.testSourceCodeCustomizers = testSourceCodeCustomizers;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		S sourceCode = this.sourceFactory.get();
		customizeTestSourceCode(sourceCode);
		this.sourceWriter.writeTo(
				this.description.getBuildSystem().getTestSource(projectRoot, this.description.getLanguage()),
				sourceCode);
	}

	@SuppressWarnings("unchecked")
	private void customizeTestSourceCode(S sourceCode) {
		List<TestSourceCodeCustomizer<?, ?, ?>> customizers = this.testSourceCodeCustomizers.orderedStream()
				.collect(Collectors.toList());
		LambdaSafe.callbacks(TestSourceCodeCustomizer.class, customizers, sourceCode)
				.invoke((customizer) -> customizer.customize(sourceCode));
	}

}
