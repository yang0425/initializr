package io.spring.initializr.generator.language.java.writer;

import java.io.IOException;
import java.util.List;

import io.spring.initializr.generator.language.java.BaseJavaSourceCodeWriterTests;
import io.spring.initializr.generator.language.java.JavaExpressionStatement;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodInvocation;
import io.spring.initializr.generator.language.java.JavaNewInstanceExpression;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImportsWriterTest extends BaseJavaSourceCodeWriterTests {

	@Test
	void importFromExtend() throws IOException {
		List<String> lines = writeSingleType((test) -> test.extend("com.example.build.TestParent"));

		assertThat(lines).contains("import com.example.build.TestParent;");
	}

	@Test
	void importFromMethodInvocationExpression() throws IOException {
		List<String> lines = writeSingleType((test) -> test.addMethodDeclaration(JavaMethodDeclaration.method("test")
				.body(new JavaExpressionStatement(new JavaMethodInvocation("com.example.test.TestUtils", "test")))));

		assertThat(lines).contains("import com.example.test.TestUtils;");
	}

	@Test
	void importFromNewInstanceExpression() throws IOException {
		List<String> lines = writeSingleType((test) -> test
				.addMethodDeclaration(JavaMethodDeclaration.method("test").body(new JavaExpressionStatement(
						new JavaNewInstanceExpression("com.example.test.TestUtils", "test")))));

		assertThat(lines).contains("import com.example.test.TestUtils;");
	}

	@Test
	void staticImportFromMethodInvocationExpression() throws IOException {
		List<String> lines = writeSingleType((test) -> test
				.addMethodDeclaration(JavaMethodDeclaration.method("test").body(new JavaExpressionStatement(
						new JavaMethodInvocation("com.example.test.TestUtils.test", "test")))));

		assertThat(lines).contains("import static com.example.test.TestUtils.test;");
	}

}
