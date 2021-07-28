package io.spring.initializr.generator.language.java;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.language.SourceStructure;
import org.junit.jupiter.api.io.TempDir;

import org.springframework.util.StreamUtils;

public class BaseJavaSourceCodeWriterTests {

	private static final Language LANGUAGE = new JavaLanguage();

	@TempDir
	Path directory;

	private final JavaSourceCodeWriter writer = new JavaSourceCodeWriter(IndentingWriterFactory.withDefaultSettings());

	protected List<String> writeSingleType(Consumer<JavaTypeDeclaration> consumer) throws IOException {
		JavaSourceCode sourceCode = new JavaSourceCode();
		JavaCompilationUnit compilationUnit = sourceCode.createCompilationUnit("com.example", "Test");
		JavaTypeDeclaration test = compilationUnit.createTypeDeclaration("Test");
		consumer.accept(test);
		return writeSingleType(sourceCode);
	}

	protected List<String> writeSingleType(JavaSourceCode sourceCode) throws IOException {
		Path source = writeSourceCode(sourceCode).resolve("com/example/Test.java");
		try (InputStream stream = Files.newInputStream(source)) {
			String[] lines = StreamUtils.copyToString(stream, StandardCharsets.UTF_8).split("\\r?\\n");
			return Arrays.asList(lines);
		}
	}

	private Path writeSourceCode(JavaSourceCode sourceCode) throws IOException {
		Path srcDirectory = this.directory.resolve(UUID.randomUUID().toString());
		SourceStructure sourceStructure = new SourceStructure(srcDirectory, LANGUAGE);
		this.writer.writeTo(sourceStructure, sourceCode);
		return sourceStructure.getSourcesDirectory();
	}

}
