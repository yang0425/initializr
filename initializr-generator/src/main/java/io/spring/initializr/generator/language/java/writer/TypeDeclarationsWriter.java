package io.spring.initializr.generator.language.java.writer;

import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.language.java.JavaTypeDeclaration;

public class TypeDeclarationsWriter implements CodeWriter {

	private static final Map<Predicate<Integer>, String> TYPE_MODIFIERS;

	static {
		Map<Predicate<Integer>, String> typeModifiers = new LinkedHashMap<>();
		typeModifiers.put(Modifier::isPublic, "public");
		typeModifiers.put(Modifier::isProtected, "protected");
		typeModifiers.put(Modifier::isPrivate, "private");
		typeModifiers.put(Modifier::isAbstract, "abstract");
		typeModifiers.put(Modifier::isStatic, "static");
		typeModifiers.put(Modifier::isFinal, "final");
		typeModifiers.put(Modifier::isStrict, "strictfp");
		TYPE_MODIFIERS = typeModifiers;
	}

	private final List<JavaTypeDeclaration> typeDeclarations;

	public TypeDeclarationsWriter(List<JavaTypeDeclaration> typeDeclarations) {
		this.typeDeclarations = typeDeclarations;
	}

	@Override
	public void write(IndentingWriter writer) {
		this.typeDeclarations.stream().flatMap(this::createCodeWriters)
				.forEach((codeWriter) -> codeWriter.write(writer));
	}

	private Stream<CodeWriter> createCodeWriters(JavaTypeDeclaration type) {
		List<CodeWriter> codeWriters = new LinkedList<>();
		codeWriters.add(new AnnotationsWriter(type));
		codeWriters.add(new ModifiersWriter(TYPE_MODIFIERS, type.getModifiers()));
		codeWriters.add((writer) -> writer.print("class " + type.getName()));
		if (type.getExtends() != null) {
			codeWriters.add((writer) -> writer.print(" extends " + getUnqualifiedName(type.getExtends())));
		}
		codeWriters.add((writer) -> writer.println(" {"));
		codeWriters.add(IndentingWriter::println);

		codeWriters.add(new FieldsWriter(type.getFieldDeclarations()));
		codeWriters.add(new ConstructorsWriter(type.getName(), type.getConstructorDeclarations()));
		codeWriters.add(new MethodsWriter(type.getMethodDeclarations()));

		codeWriters.add((writer) -> writer.println("}"));

		return codeWriters.stream();
	}

}
