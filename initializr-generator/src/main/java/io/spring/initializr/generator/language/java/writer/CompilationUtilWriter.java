package io.spring.initializr.generator.language.java.writer;

import java.util.LinkedList;
import java.util.List;

import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.language.java.JavaCompilationUnit;

public class CompilationUtilWriter implements CodeWriter {

	private final JavaCompilationUnit compilationUnit;

	public CompilationUtilWriter(JavaCompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
	}

	@Override
	public void write(IndentingWriter writer) {
		createCodeWriters().forEach((codeWriter) -> codeWriter.write(writer));
	}

	private List<CodeWriter> createCodeWriters() {
		List<CodeWriter> writers = new LinkedList<>();
		writers.add(new PackageWriter(this.compilationUnit.getPackageName()));
		writers.add(new ImportsWriter(this.compilationUnit.getTypeDeclarations()));
		writers.add(new TypeDeclarationsWriter(this.compilationUnit.getTypeDeclarations()));
		return writers;
	}

}
