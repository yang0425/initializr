package io.spring.initializr.generator.language.java;

import java.util.List;

import io.spring.initializr.generator.io.IndentingWriter;

public class JavaExpressionStatement implements JavaStatement {

	private final JavaExpression expression;

	public JavaExpressionStatement(JavaExpression expression) {
		this.expression = expression;
	}

	public JavaExpression getExpression() {
		return this.expression;
	}

	@Override
	public List<String> getImports() {
		return this.expression.getImports();
	}

	@Override
	public List<String> getStaticImports() {
		return this.expression.getStaticImports();
	}

	@Override
	public void write(IndentingWriter writer) {
		this.expression.write(writer);
		writer.println(";");
	}

}
