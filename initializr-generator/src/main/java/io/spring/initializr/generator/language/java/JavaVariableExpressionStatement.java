package io.spring.initializr.generator.language.java;

import java.util.ArrayList;
import java.util.List;

import io.spring.initializr.generator.io.IndentingWriter;

public class JavaVariableExpressionStatement implements JavaStatement {

	private final String type;

	private final String name;

	private final JavaExpression expression;

	public JavaVariableExpressionStatement(String type, String name, JavaExpression expression) {
		this.type = type;
		this.name = name;
		this.expression = expression;
	}

	public String getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}

	public JavaExpression getExpression() {
		return this.expression;
	}

	@Override
	public List<String> getImports() {
		List<String> imports = new ArrayList<>();
		imports.add(this.type);
		imports.addAll(this.expression.getImports());
		return imports;
	}

	@Override
	public List<String> getStaticImports() {
		return this.expression.getStaticImports();
	}

	@Override
	public void write(IndentingWriter writer) {
		writer.print(String.format("%s %s = ", getUnqualifiedName(this.type), this.name));
		this.expression.write(writer);
		writer.println(";");
	}

}
