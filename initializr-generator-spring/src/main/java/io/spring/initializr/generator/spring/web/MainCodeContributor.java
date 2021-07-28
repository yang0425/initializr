package io.spring.initializr.generator.spring.web;

import java.util.List;
import java.util.stream.Collectors;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.spring.code.MainSourceCodeCustomizer;
import io.spring.initializr.generator.spring.util.LambdaSafe;

import org.springframework.beans.factory.ObjectProvider;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class MainCodeContributor<T extends CodeCustomizer> implements
		MainSourceCodeCustomizer<TypeDeclaration, CompilationUnit<TypeDeclaration>, SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>>> {

	private final String packageName;

	private final String className;

	private final Class<T> customizerClass;

	private final ObjectProvider<T> codeCustomizers;

	public MainCodeContributor(String packageName, String className, Class<T> customizerClass,
			ObjectProvider<T> codeCustomizers) {
		this.packageName = packageName;
		this.className = className;
		this.customizerClass = customizerClass;
		this.codeCustomizers = codeCustomizers;
	}

	@Override
	public void customize(SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode) {
		CompilationUnit<TypeDeclaration> compilationUnit = sourceCode.createCompilationUnit(this.packageName,
				this.className);
		TypeDeclaration typeDeclaration = compilationUnit.createTypeDeclaration(this.className);
		List<T> customizers = this.codeCustomizers.orderedStream().collect(Collectors.toList());
		LambdaSafe.callbacks(this.customizerClass, customizers, typeDeclaration)
				.invoke((customizer) -> customizer.customize(typeDeclaration));
	}

}
