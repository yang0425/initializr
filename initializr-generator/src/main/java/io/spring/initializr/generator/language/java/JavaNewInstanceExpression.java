package io.spring.initializr.generator.language.java;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.spring.initializr.generator.io.IndentingWriter;

public class JavaNewInstanceExpression implements JavaExpression {

	private final String type;

	private final List<String> arguments;

	public JavaNewInstanceExpression(String type, String... arguments) {
		this.type = type;
		this.arguments = Arrays.asList(arguments);
	}

	public String getType() {
		return this.type;
	}

	public List<String> getArguments() {
		return this.arguments;
	}

	@Override
	public List<String> getImports() {
		return Collections.singletonList(this.type);
	}

	@Override
	public List<String> getStaticImports() {
		return Collections.emptyList();
	}

	@Override
	public void write(IndentingWriter writer) {
		writer.print("new ");
		writer.print(getUnqualifiedName(this.type));
		writer.print("(");
		writer.print(String.join(", ", this.getArguments()));
		writer.print(")");
	}

}
