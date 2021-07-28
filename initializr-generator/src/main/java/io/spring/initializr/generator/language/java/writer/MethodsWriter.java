package io.spring.initializr.generator.language.java.writer;

import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.java.JavaEmptyLineStatement;
import io.spring.initializr.generator.language.java.JavaExpression;
import io.spring.initializr.generator.language.java.JavaExpressionStatement;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodInvocation;
import io.spring.initializr.generator.language.java.JavaReturnStatement;
import io.spring.initializr.generator.language.java.JavaStatement;
import io.spring.initializr.generator.language.java.JavaStringExpression;
import io.spring.initializr.generator.language.java.JavaVariableExpressionStatement;

public class MethodsWriter implements CodeWriter {

	private static final Map<Predicate<Integer>, String> METHOD_MODIFIERS;

	static {
		Map<Predicate<Integer>, String> methodModifiers = new LinkedHashMap<>();
		methodModifiers.put(Modifier::isPublic, "public");
		methodModifiers.put(Modifier::isProtected, "protected");
		methodModifiers.put(Modifier::isPrivate, "private");
		methodModifiers.put(Modifier::isAbstract, "abstract");
		methodModifiers.put(Modifier::isStatic, "static");
		methodModifiers.put(Modifier::isFinal, "final");
		methodModifiers.put(Modifier::isStrict, "strictfp");
		methodModifiers.put(Modifier::isSynchronized, "synchronized");
		methodModifiers.put(Modifier::isNative, "native");
		METHOD_MODIFIERS = methodModifiers;
	}

	private final List<JavaMethodDeclaration> methodDeclarations;

	public MethodsWriter(List<JavaMethodDeclaration> methodDeclarations) {
		this.methodDeclarations = methodDeclarations;
	}

	@Override
	public void write(IndentingWriter writer) {
		if (!methodDeclarations.isEmpty()) {
			writer.indented(() -> {
				for (JavaMethodDeclaration methodDeclaration : methodDeclarations) {
					writeMethodDeclaration(writer, methodDeclaration);
				}
			});
		}
	}

	private void writeMethodDeclaration(IndentingWriter writer, JavaMethodDeclaration methodDeclaration) {
		AnnotationsWriter annotationsWriter = new AnnotationsWriter(methodDeclaration);
		annotationsWriter.write(writer);

		ModifiersWriter modifiersWriter = new ModifiersWriter(METHOD_MODIFIERS, methodDeclaration.getModifiers());
		modifiersWriter.write(writer);
		writer.print(getUnqualifiedName(methodDeclaration.getReturnType()) + " " + methodDeclaration.getName() + "(");
		List<Parameter> parameters = methodDeclaration.getParameters();
		if (!parameters.isEmpty()) {
			writer.print(parameters.stream()
					.map((parameter) -> getUnqualifiedName(parameter.getType()) + " " + parameter.getName())
					.collect(Collectors.joining(", ")));
		}
		writer.println(") {");
		writer.indented(() -> {
			List<JavaStatement> statements = methodDeclaration.getStatements();
			for (JavaStatement statement : statements) {
				if (statement instanceof JavaExpressionStatement) {
					writeExpression(writer, ((JavaExpressionStatement) statement).getExpression());
				}
				else if (statement instanceof JavaReturnStatement) {
					writer.print("return ");
					writeExpression(writer, ((JavaReturnStatement) statement).getExpression());
				}
				else if (statement instanceof JavaEmptyLineStatement) {
					writer.println();
					continue;
				}
				else if (statement instanceof JavaVariableExpressionStatement) {
					writer.print(String.format("%s %s = ",
							getUnqualifiedName(((JavaVariableExpressionStatement) statement).getType()),
							((JavaVariableExpressionStatement) statement).getName()));
					writeExpression(writer, ((JavaVariableExpressionStatement) statement).getExpression());
				}
				writer.println(";");
			}
		});
		writer.println("}");
		writer.println();
	}

	private void writeExpression(IndentingWriter writer, JavaExpression expression) {
		if (expression instanceof JavaMethodInvocation) {
			writeMethodInvocation(writer, (JavaMethodInvocation) expression);
		}
		else if (expression instanceof JavaStringExpression) {
			writer.print(((JavaStringExpression) expression).getContent());
		}
	}

	private void writeMethodInvocation(IndentingWriter writer, JavaMethodInvocation methodInvocation) {
		writer.print(getUnqualifiedName(methodInvocation.getTarget()) + "." + methodInvocation.getName() + "("
				+ String.join(", ", methodInvocation.getArguments()) + ")");
	}

}
