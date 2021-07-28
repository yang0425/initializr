package io.spring.initializr.generator.language.java.writer;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.java.JavaFieldDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaStatement;
import io.spring.initializr.generator.language.java.JavaTypeDeclaration;

public class ImportsWriter implements CodeWriter {

	private final List<JavaTypeDeclaration> typeDeclarations;

	private final List<String> imports = new ArrayList<>();

	private final List<String> staticImports = new ArrayList<>();

	public ImportsWriter(List<JavaTypeDeclaration> typeDeclarations) {
		this.typeDeclarations = typeDeclarations;
		initImports();
	}

	@Override
	public void write(IndentingWriter writer) {
		Set<String> imports = this.imports.stream().filter(this::requiresImport).sorted()
				.collect(Collectors.toCollection(LinkedHashSet::new));
		Set<String> staticImports = this.staticImports.stream().filter(this::requiresImport).sorted()
				.collect(Collectors.toCollection(LinkedHashSet::new));
		if (!imports.isEmpty()) {
			for (String importedType : imports) {
				writer.println("import " + importedType + ";");
			}
			writer.println();
		}
		if (!staticImports.isEmpty()) {
			for (String importedType : staticImports) {
				writer.println("import static " + importedType + ";");
			}
			writer.println();
		}
	}

	private void initImports() {
		for (JavaTypeDeclaration typeDeclaration : this.typeDeclarations) {
			this.imports.add(typeDeclaration.getExtends());
			typeDeclaration.getAnnotations().forEach(this::findAnnotationImports);
			typeDeclaration.getFieldDeclarations().forEach(this::findFieldImports);
			typeDeclaration.getMethodDeclarations().forEach(this::findMethodImports);
		}
	}

	private void findAnnotationImports(Annotation annotation) {
		this.imports.add(annotation.getName());
		annotation.getAttributes().forEach((attribute) -> {
			if (attribute.getType() == Class.class) {
				this.imports.addAll(attribute.getValues());
			}
			if (Enum.class.isAssignableFrom(attribute.getType())) {
				this.imports.addAll(attribute.getValues().stream()
						.map((value) -> value.substring(0, value.lastIndexOf("."))).collect(Collectors.toList()));
			}
		});
	}

	private void findFieldImports(JavaFieldDeclaration fieldDeclaration) {
		this.imports.add(fieldDeclaration.getReturnType());
		fieldDeclaration.getAnnotations().forEach(this::findAnnotationImports);
	}

	private void findMethodImports(JavaMethodDeclaration methodDeclaration) {
		this.imports.add(methodDeclaration.getReturnType());
		methodDeclaration.getAnnotations().forEach(this::findAnnotationImports);
		this.imports.addAll(
				methodDeclaration.getParameters().stream().map(Parameter::getType).collect(Collectors.toList()));
		findMethodBodyImports(methodDeclaration.getStatements());
	}

	private void findMethodBodyImports(List<JavaStatement> statements) {
		for (JavaStatement statement : statements) {
			this.imports.addAll(statement.getImports());
			this.staticImports.addAll(statement.getStaticImports());
		}
	}

	private boolean requiresImport(String name) {
		if (name == null || !name.contains(".")) {
			return false;
		}
		String packageName = name.substring(0, name.lastIndexOf('.'));
		return !"java.lang".equals(packageName);
	}

}
