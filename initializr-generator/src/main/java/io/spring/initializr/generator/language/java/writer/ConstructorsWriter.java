package io.spring.initializr.generator.language.java.writer;

import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.java.JavaConstructorDeclaration;
import io.spring.initializr.generator.language.java.JavaExpression;
import io.spring.initializr.generator.language.java.JavaExpressionStatement;
import io.spring.initializr.generator.language.java.JavaMethodInvocation;
import io.spring.initializr.generator.language.java.JavaStatement;
import io.spring.initializr.generator.language.java.JavaStringExpression;

public class ConstructorsWriter implements CodeWriter {

	private static final Map<Predicate<Integer>, String> CONSTRUCTOR_MODIFIERS;

	static {
		Map<Predicate<Integer>, String> constructorModifiers = new LinkedHashMap<>();
		constructorModifiers.put(Modifier::isPublic, "public");
		constructorModifiers.put(Modifier::isProtected, "protected");
		constructorModifiers.put(Modifier::isPrivate, "private");
		CONSTRUCTOR_MODIFIERS = constructorModifiers;
	}

	private final String typeName;

	private final List<JavaConstructorDeclaration> constructorDeclarations;

	public ConstructorsWriter(String typeName, List<JavaConstructorDeclaration> constructorDeclarations) {
		this.typeName = typeName;
		this.constructorDeclarations = constructorDeclarations;
	}

	@Override
	public void write(IndentingWriter writer) {
		if (!this.constructorDeclarations.isEmpty()) {
			writer.indented(() -> {
				for (JavaConstructorDeclaration constructorDeclaration : this.constructorDeclarations) {
					writeConstructorDeclaration(writer, constructorDeclaration);
				}
			});
		}
	}

	private void writeConstructorDeclaration(IndentingWriter writer,
			JavaConstructorDeclaration constructorDeclaration) {
		ModifiersWriter modifiersWriter = new ModifiersWriter(CONSTRUCTOR_MODIFIERS,
				constructorDeclaration.getModifiers());
		modifiersWriter.write(writer);
		writer.print(this.typeName + "(");
		List<Parameter> parameters = constructorDeclaration.getParameters();
		if (!parameters.isEmpty()) {
			writer.print(parameters.stream()
					.map((parameter) -> getUnqualifiedName(parameter.getType()) + " " + parameter.getName())
					.collect(Collectors.joining(", ")));
		}
		writer.println(") {");
		writer.indented(() -> {
			List<JavaStatement> statements = constructorDeclaration.getStatements();
			for (JavaStatement statement : statements) {
				if (statement instanceof JavaExpressionStatement) {
					writeExpression(writer, ((JavaExpressionStatement) statement).getExpression());
				}
				writer.println(";");
			}
		});
		writer.println("}");
		writer.println();
	}

	private void writeExpression(IndentingWriter writer, JavaExpression expression) {
		if (expression instanceof JavaMethodInvocation) {
			expression.write(writer);
		}
		else if (expression instanceof JavaStringExpression) {
			writer.print(((JavaStringExpression) expression).getContent());
		}
	}

}
