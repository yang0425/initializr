package io.spring.initializr.generator.language.java;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.time.temporal.ChronoUnit;
import java.util.List;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.Parameter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class JavaSourceCodeWriterTests extends BaseJavaSourceCodeWriterTests {

	@Nested
	class Exception {

		@Test
		void nullPackageInvalidCompilationUnit() {
			JavaSourceCode sourceCode = new JavaSourceCode();
			assertThatIllegalArgumentException().isThrownBy(() -> sourceCode.createCompilationUnit(null, "Test"));
		}

		@Test
		void nullNameInvalidCompilationUnit() {
			JavaSourceCode sourceCode = new JavaSourceCode();
			assertThatIllegalArgumentException()
					.isThrownBy(() -> sourceCode.createCompilationUnit("com.example", null));
		}

	}

	@Nested
	class Type {

		@Test
		void emptyCompilationUnit() throws IOException {
			JavaSourceCode sourceCode = new JavaSourceCode();
			sourceCode.createCompilationUnit("com.example", "Test");
			List<String> lines = writeSingleType(sourceCode);
			assertThat(lines).containsExactly("package com.example;");
		}

		@Test
		void emptyTypeDeclaration() throws IOException {
			List<String> lines = writeSingleType((test) -> {
			});

			assertThat(lines).containsExactly("package com.example;", "", "class Test {", "", "}");
		}

		@Test
		void emptyTypeDeclarationWithModifiers() throws IOException {
			List<String> lines = writeSingleType((test) -> test.modifiers(Modifier.PROTECTED | Modifier.ABSTRACT));

			assertThat(lines).containsExactly("package com.example;", "", "protected abstract class Test {", "", "}");
		}

		@Test
		void emptyTypeDeclarationWithSuperClass() throws IOException {
			List<String> lines = writeSingleType((test) -> test.extend("com.example.build.TestParent"));

			assertThat(lines).containsExactly("package com.example;", "", "import com.example.build.TestParent;", "",
					"class Test extends TestParent {", "", "}");
		}

	}

	@Nested
	class Constructor {

		@Test
		void constructor() throws IOException {
			List<String> lines = writeSingleType((test) -> test.addConstructorDeclaration(JavaConstructorDeclaration
					.constructor().modifiers(Modifier.PUBLIC).parameters(new Parameter("java.lang.String", "value"))
					.body(new JavaExpressionStatement(new JavaStringExpression("this.value = value")))));

			assertThat(lines).containsExactly("package com.example;", "", "class Test {", "",
					"    public Test(String value) {", "        this.value = value;", "    }", "", "}");
		}

	}

	@Nested
	class Method {

		@Test
		void method() throws IOException {
			List<String> lines = writeSingleType((test) -> test
					.addMethodDeclaration(JavaMethodDeclaration.method("trim").returning("java.lang.String")
							.modifiers(Modifier.PUBLIC).parameters(new Parameter("java.lang.String", "value"))
							.body(new JavaReturnStatement(new JavaMethodInvocation("value", "trim")))));

			assertThat(lines).containsExactly("package com.example;", "", "class Test {", "",
					"    public String trim(String value) {", "        return value.trim();", "    }", "", "}");
		}

		@Test
		void methodWithPlainText() throws IOException {
			List<String> lines = writeSingleType((test) -> test.addMethodDeclaration(
					JavaMethodDeclaration.method("hello").returning("java.lang.String").modifiers(Modifier.PUBLIC)
							.body(new JavaReturnStatement(new JavaStringExpression("\"Hello World!\"")))));

			assertThat(lines).containsExactly("package com.example;", "", "class Test {", "",
					"    public String hello() {", "        return \"Hello World!\";", "    }", "", "}");
		}

		@Test
		void methodWithSimpleAnnotation() throws IOException {
			List<String> lines = writeSingleType((test) -> {
				JavaMethodDeclaration method = JavaMethodDeclaration.method("something").returning("void").parameters()
						.body();
				method.annotate(Annotation.name("com.example.test.TestAnnotation"));
				test.addMethodDeclaration(method);
			});

			assertThat(lines).containsExactly("package com.example;", "", "import com.example.test.TestAnnotation;", "",
					"class Test {", "", "    @TestAnnotation", "    void something() {", "    }", "", "}");
		}

	}

	@Nested
	class Field {

		@Test
		void field() throws IOException {
			List<String> lines = writeSingleType((test) -> {
				test.modifiers(Modifier.PUBLIC);
				test.addFieldDeclaration(JavaFieldDeclaration.field("testString").modifiers(Modifier.PRIVATE)
						.returning("java.lang.String"));
			});

			assertThat(lines).containsExactly("package com.example;", "", "public class Test {", "",
					"    private String testString;", "", "}");
		}

		@Test
		void fieldImport() throws IOException {
			List<String> lines = writeSingleType((test) -> test.addFieldDeclaration(
					JavaFieldDeclaration.field("testString").modifiers(Modifier.PUBLIC).returning("com.example.One")));

			assertThat(lines).containsExactly("package com.example;", "", "import com.example.One;", "", "class Test {",
					"", "    public One testString;", "", "}");
		}

		@Test
		void fieldAnnotation() throws IOException {
			List<String> lines = writeSingleType((test) -> {
				test.modifiers(Modifier.PUBLIC);
				JavaFieldDeclaration field = JavaFieldDeclaration.field("testString").modifiers(Modifier.PRIVATE)
						.returning("java.lang.String");
				field.annotate(Annotation.name("org.springframework.beans.factory.annotation.Autowired"));
				test.addFieldDeclaration(field);
			});

			assertThat(lines).containsExactly("package com.example;", "",
					"import org.springframework.beans.factory.annotation.Autowired;", "", "public class Test {", "",
					"    @Autowired", "    private String testString;", "", "}");
		}

		@Test
		void fields() throws IOException {
			List<String> lines = writeSingleType((test) -> {
				test.modifiers(Modifier.PUBLIC);
				test.addFieldDeclaration(JavaFieldDeclaration.field("testString").modifiers(Modifier.PRIVATE)
						.value("\"Test String\"").returning("java.lang.String"));
				test.addFieldDeclaration(JavaFieldDeclaration.field("testChar")
						.modifiers(Modifier.PRIVATE | Modifier.TRANSIENT).value("'\\u03a9'").returning("char"));
				test.addFieldDeclaration(JavaFieldDeclaration.field("testInt")
						.modifiers(Modifier.PRIVATE | Modifier.FINAL).value(1337).returning("int"));
				test.addFieldDeclaration(JavaFieldDeclaration.field("testDouble").modifiers(Modifier.PRIVATE)
						.value("3.14").returning("Double"));
				test.addFieldDeclaration(JavaFieldDeclaration.field("testLong").modifiers(Modifier.PRIVATE)
						.value("1986L").returning("Long"));
				test.addFieldDeclaration(JavaFieldDeclaration.field("testFloat").modifiers(Modifier.PUBLIC)
						.value("99.999f").returning("float"));
				test.addFieldDeclaration(JavaFieldDeclaration.field("testBool").value("true").returning("boolean"));
			});
			assertThat(lines).containsExactly("package com.example;", "", "public class Test {", "",
					"    private String testString = \"Test String\";", "",
					"    private transient char testChar = '\\u03a9';", "", "    private final int testInt = 1337;", "",
					"    private Double testDouble = 3.14;", "", "    private Long testLong = 1986L;", "",
					"    public float testFloat = 99.999f;", "", "    boolean testBool = true;", "", "}");
		}

	}

	@Nested
	class AnnotationTest {

		@Test
		void annotationWithSimpleAttribute() throws IOException {
			List<String> lines = writeClassAnnotation(Annotation.name("org.springframework.test.TestApplication",
					(builder) -> builder.attribute("counter", Integer.class, "42")));
			assertThat(lines).containsExactly("package com.example;", "",
					"import org.springframework.test.TestApplication;", "", "@TestApplication(counter = 42)",
					"class Test {", "", "}");
		}

		@Test
		void annotationWithSimpleStringAttribute() throws IOException {
			List<String> lines = writeClassAnnotation(Annotation.name("org.springframework.test.TestApplication",
					(builder) -> builder.attribute("name", String.class, "test")));
			assertThat(lines).containsExactly("package com.example;", "",
					"import org.springframework.test.TestApplication;", "", "@TestApplication(name = \"test\")",
					"class Test {", "", "}");
		}

		@Test
		void annotationWithOnlyValueAttribute() throws IOException {
			List<String> lines = writeClassAnnotation(Annotation.name("org.springframework.test.TestApplication",
					(builder) -> builder.attribute("value", String.class, "test")));
			assertThat(lines).containsExactly("package com.example;", "",
					"import org.springframework.test.TestApplication;", "", "@TestApplication(\"test\")",
					"class Test {", "", "}");
		}

		@Test
		void annotationWithSimpleEnumAttribute() throws IOException {
			List<String> lines = writeClassAnnotation(Annotation.name("org.springframework.test.TestApplication",
					(builder) -> builder.attribute("unit", Enum.class, "java.time.temporal.ChronoUnit.SECONDS")));
			assertThat(lines).containsExactly("package com.example;", "", "import java.time.temporal.ChronoUnit;",
					"import org.springframework.test.TestApplication;", "",
					"@TestApplication(unit = ChronoUnit.SECONDS)", "class Test {", "", "}");
		}

		@Test
		void annotationWithClassArrayAttribute() throws IOException {
			List<String> lines = writeClassAnnotation(Annotation.name("org.springframework.test.TestApplication",
					(builder) -> builder.attribute("target", Class.class, "com.example.One", "com.example.Two")));
			assertThat(lines).containsExactly("package com.example;", "", "import com.example.One;",
					"import com.example.Two;", "import org.springframework.test.TestApplication;", "",
					"@TestApplication(target = { One.class, Two.class })", "class Test {", "", "}");
		}

		@Test
		void annotationWithSeveralAttributes() throws IOException {
			List<String> lines = writeClassAnnotation(Annotation.name("org.springframework.test.TestApplication",
					(builder) -> builder.attribute("target", Class.class, "com.example.One").attribute("unit",
							ChronoUnit.class, "java.time.temporal.ChronoUnit.NANOS")));
			assertThat(lines).containsExactly("package com.example;", "", "import com.example.One;",
					"import java.time.temporal.ChronoUnit;", "import org.springframework.test.TestApplication;", "",
					"@TestApplication(target = One.class, unit = ChronoUnit.NANOS)", "class Test {", "", "}");
		}

		private List<String> writeClassAnnotation(Annotation annotation) throws IOException {
			JavaSourceCode sourceCode = new JavaSourceCode();
			JavaCompilationUnit compilationUnit = sourceCode.createCompilationUnit("com.example", "Test");
			JavaTypeDeclaration test = compilationUnit.createTypeDeclaration("Test");
			test.annotate(annotation);
			return writeSingleType(sourceCode);
		}

	}

}
