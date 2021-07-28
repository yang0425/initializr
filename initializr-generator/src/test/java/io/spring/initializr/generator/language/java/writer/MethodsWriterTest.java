package io.spring.initializr.generator.language.java.writer;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;

import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.java.BaseJavaSourceCodeWriterTests;
import io.spring.initializr.generator.language.java.JavaExpressionStatement;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodInvocation;
import io.spring.initializr.generator.language.java.JavaNewInstanceExpression;
import io.spring.initializr.generator.language.java.JavaReturnStatement;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MethodsWriterTest extends BaseJavaSourceCodeWriterTests {

	@Test
	void methodWithReturnAndMethodInvocation() throws IOException {
		List<String> lines = writeSingleType(
				(test) -> test.addMethodDeclaration(JavaMethodDeclaration.method("trim").returning("java.lang.String")
						.modifiers(Modifier.PUBLIC).parameters(new Parameter("java.lang.String", "value"))
						.body(new JavaReturnStatement(new JavaMethodInvocation("value", "trim")))));

		assertThat(lines).contains("    public String trim(String value) {", "        return value.trim();", "    }",
				"", "}");
	}

	@Test
	void methodWithNewInstanceStatement() throws IOException {
		List<String> lines = writeSingleType((test) -> test
				.addMethodDeclaration(JavaMethodDeclaration.method("hello").returning("com.demo.HelloWorld")
						.body(new JavaReturnStatement(new JavaNewInstanceExpression("com.demo.HelloWorld", "hello")))));

		assertThat(lines).contains("    HelloWorld hello() {", "        return new HelloWorld(hello);", "    }", "",
				"}");
	}

	@Test
	void methodWithStaticImport() throws IOException {
		List<String> lines = writeSingleType((test) -> test.addMethodDeclaration(JavaMethodDeclaration.method("test")
				.body(new JavaExpressionStatement(new JavaMethodInvocation("com.demo.HelloWorld.print", "print")))));

		assertThat(lines).contains("    void test() {", "        print();", "    }", "", "}");
	}

	@Test
	void methodWithLinkedInvocation() throws IOException {
		List<String> lines = writeSingleType((test) -> test.addMethodDeclaration(
				JavaMethodDeclaration.method("test").body(new JavaExpressionStatement(new JavaMethodInvocation(
						new JavaMethodInvocation("com.demo.HelloWorld.print", "print"), "test")))));

		assertThat(lines).contains("    void test() {", "        print().test();", "    }", "", "}");
	}

	@Test
	void methodWithInvocationInInvocationParameter() throws IOException {
		List<String> lines = writeSingleType((test) -> test.addMethodDeclaration(JavaMethodDeclaration.method("test")
				.body(new JavaExpressionStatement(
						new JavaMethodInvocation(new JavaMethodInvocation("com.demo.HelloWorld.print", "print"), "test",
								new JavaMethodInvocation("target", "get", "0"))))));

		assertThat(lines).contains("    void test() {", "        print().test(target.get(0));", "    }", "", "}");
	}

}
