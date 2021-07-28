package io.spring.initializr.generator.language.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.spring.initializr.generator.io.IndentingWriter;

public class JavaMethodInvocation implements JavaExpression {

	private final JavaExpression target;

	private final String name;

	private final List<JavaExpression> arguments = new ArrayList<>();

	public JavaMethodInvocation(String target, String name, String... arguments) {
		this.target = new JavaStringExpression(target);
		this.name = name;
		this.arguments.addAll(Arrays.stream(arguments).map(JavaStringExpression::new).collect(Collectors.toList()));
	}

	public JavaMethodInvocation(JavaExpression target, String name, JavaExpression... arguments) {
		this.target = target;
		this.name = name;
		this.arguments.addAll(Arrays.asList(arguments));
	}

	public JavaExpression getTarget() {
		return this.target;
	}

	public String getName() {
		return this.name;
	}

	public void addArgument(String argument) {
		this.arguments.add(new JavaStringExpression(argument));
	}

	public void addArgument(JavaExpression argument) {
		this.arguments.add(argument);
	}

	public List<JavaExpression> getArguments() {
		return this.arguments;
	}

	@Override
	public void write(IndentingWriter writer) {
		writeMethodInvocationTarget(writer);
		writer.print(this.name);
		writer.print("(");
		for (int i = 0; i < this.arguments.size(); i++) {
			JavaExpression argument = this.arguments.get(i);
			argument.write(writer);
			if (i < this.arguments.size() - 1) {
				writer.print(", ");
			}
		}
		writer.print(")");
	}

	@Override
	public List<String> getImports() {
		if (this.target instanceof JavaStringExpression) {
			if (!isStaticMethod()) {
				return Collections.singletonList(((JavaStringExpression) this.target).getContent());
			}
			else {
				return Collections.emptyList();
			}
		}
		else {
			return this.target.getImports();
		}
	}

	@Override
	public List<String> getStaticImports() {
		if (this.target instanceof JavaStringExpression) {
			if (isStaticMethod()) {
				return Collections.singletonList(((JavaStringExpression) this.target).getContent());
			}
			else {
				return Collections.emptyList();
			}
		}
		else {
			return this.target.getStaticImports();
		}
	}

	private void writeMethodInvocationTarget(IndentingWriter writer) {
		if (this.target instanceof JavaStringExpression && !isStaticMethod()) {
			writer.print(getUnqualifiedName(((JavaStringExpression) this.target).getContent()) + ".");
		}
		else if (this.target instanceof JavaMethodInvocation) {
			this.target.write(writer);
			writer.print(".");
		}
	}

	private boolean isStaticMethod() {
		return this.target instanceof JavaStringExpression
				&& getUnqualifiedName(((JavaStringExpression) this.target).getContent()).equals(this.name);
	}

}
