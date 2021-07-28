package io.spring.initializr.generator.language.java.writer;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.java.JavaExpressionStatement;
import io.spring.initializr.generator.language.java.JavaFieldDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodInvocation;
import io.spring.initializr.generator.language.java.JavaTypeDeclaration;
import io.spring.initializr.generator.language.java.JavaVariableExpressionStatement;

public class ImportsWriter implements CodeWriter {

	private final List<JavaTypeDeclaration> typeDeclarations;

	private final List<String> imports = new ArrayList<>();

	public ImportsWriter(List<JavaTypeDeclaration> typeDeclarations) {
		this.typeDeclarations = typeDeclarations;
		initImports();
	}

	@Override
	public void write(IndentingWriter writer) {
		Set<String> imports = this.imports.stream().filter(this::requiresImport).sorted()
				.collect(Collectors.toCollection(LinkedHashSet::new));
		if (!imports.isEmpty()) {
			for (String importedType : imports) {
				writer.println("import " + importedType + ";");
			}
			writer.println();
		}
	}

	private void initImports() {
		for (JavaTypeDeclaration typeDeclaration : typeDeclarations) {
			imports.add(typeDeclaration.getExtends());
			typeDeclaration.getAnnotations().forEach(this::findAnnotationImports);
			typeDeclaration.getFieldDeclarations().forEach(this::findFieldImports);
			typeDeclaration.getMethodDeclarations().forEach(this::findMethodImports);
		}
	}

	private void findAnnotationImports(Annotation annotation) {
		imports.add(annotation.getName());
		annotation.getAttributes().forEach((attribute) -> {
			if (attribute.getType() == Class.class) {
				imports.addAll(attribute.getValues());
			}
			if (Enum.class.isAssignableFrom(attribute.getType())) {
				imports.addAll(attribute.getValues().stream().map((value) -> value.substring(0, value.lastIndexOf(".")))
						.collect(Collectors.toList()));
			}
		});
	}

	private void findFieldImports(JavaFieldDeclaration fieldDeclaration) {
		imports.add(fieldDeclaration.getReturnType());
		fieldDeclaration.getAnnotations().forEach(this::findAnnotationImports);
	}

	private void findMethodImports(JavaMethodDeclaration methodDeclaration) {
		imports.add(methodDeclaration.getReturnType());
		methodDeclaration.getAnnotations().forEach(this::findAnnotationImports);
		imports.addAll(methodDeclaration.getParameters().stream().map(Parameter::getType).collect(Collectors.toList()));
		imports.addAll(methodDeclaration.getStatements().stream().filter(JavaExpressionStatement.class::isInstance)
				.map(JavaExpressionStatement.class::cast).map(JavaExpressionStatement::getExpression)
				.filter(JavaMethodInvocation.class::isInstance).map(JavaMethodInvocation.class::cast)
				.map(JavaMethodInvocation::getTarget).collect(Collectors.toList()));
		imports.addAll(
				methodDeclaration.getStatements().stream().filter(JavaVariableExpressionStatement.class::isInstance)
						.map(JavaVariableExpressionStatement.class::cast).map(JavaVariableExpressionStatement::getType)
						.collect(Collectors.toList()));
	}

	private boolean requiresImport(String name) {
		if (name == null || !name.contains(".")) {
			return false;
		}
		String packageName = name.substring(0, name.lastIndexOf('.'));
		return !"java.lang".equals(packageName);
	}

}
