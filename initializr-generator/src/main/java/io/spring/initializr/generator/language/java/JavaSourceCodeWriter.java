package io.spring.initializr.generator.language.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.language.SourceCodeWriter;
import io.spring.initializr.generator.language.SourceStructure;
import io.spring.initializr.generator.language.java.writer.CodeWriterFactory;

public class JavaSourceCodeWriter implements SourceCodeWriter<JavaSourceCode> {

	private final IndentingWriterFactory indentingWriterFactory;

	public JavaSourceCodeWriter(IndentingWriterFactory indentingWriterFactory) {
		this.indentingWriterFactory = indentingWriterFactory;
	}

	@Override
	public void writeTo(SourceStructure structure, JavaSourceCode sourceCode) throws IOException {
		for (JavaCompilationUnit compilationUnit : sourceCode.getCompilationUnits()) {
			writeTo(structure, compilationUnit);
		}
	}

	private void writeTo(SourceStructure structure, JavaCompilationUnit compilationUnit) throws IOException {
		Path output = structure.createSourceFile(compilationUnit.getPackageName(), compilationUnit.getName());
		Files.createDirectories(output.getParent());
		try (IndentingWriter writer = this.indentingWriterFactory.createIndentingWriter("java",
				Files.newBufferedWriter(output))) {
			CodeWriterFactory.createWriter(compilationUnit).write(writer);
		}
	}

}
