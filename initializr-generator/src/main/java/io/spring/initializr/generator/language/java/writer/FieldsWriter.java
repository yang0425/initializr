package io.spring.initializr.generator.language.java.writer;

import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.language.java.JavaFieldDeclaration;

public class FieldsWriter implements CodeWriter {

	private static final Map<Predicate<Integer>, String> FIELD_MODIFIERS;

	static {
		Map<Predicate<Integer>, String> fieldModifiers = new LinkedHashMap<>();
		fieldModifiers.put(Modifier::isPublic, "public");
		fieldModifiers.put(Modifier::isProtected, "protected");
		fieldModifiers.put(Modifier::isPrivate, "private");
		fieldModifiers.put(Modifier::isStatic, "static");
		fieldModifiers.put(Modifier::isFinal, "final");
		fieldModifiers.put(Modifier::isTransient, "transient");
		fieldModifiers.put(Modifier::isVolatile, "volatile");
		FIELD_MODIFIERS = fieldModifiers;
	}

	private final List<JavaFieldDeclaration> fieldDeclarations;

	public FieldsWriter(List<JavaFieldDeclaration> fieldDeclarations) {
		this.fieldDeclarations = fieldDeclarations;
	}

	@Override
	public void write(IndentingWriter writer) {
		if (!fieldDeclarations.isEmpty()) {
			writer.indented(() -> {
				for (JavaFieldDeclaration fieldDeclaration : fieldDeclarations) {
					writeFieldDeclaration(writer, fieldDeclaration);
				}
			});
		}
	}

	private void writeFieldDeclaration(IndentingWriter writer, JavaFieldDeclaration fieldDeclaration) {
		AnnotationsWriter annotationsWriter = new AnnotationsWriter(fieldDeclaration);
		annotationsWriter.write(writer);

		ModifiersWriter modifiersWriter = new ModifiersWriter(FIELD_MODIFIERS, fieldDeclaration.getModifiers());
		modifiersWriter.write(writer);
		writer.print(getUnqualifiedName(fieldDeclaration.getReturnType()));
		writer.print(" ");
		writer.print(fieldDeclaration.getName());
		if (fieldDeclaration.isInitialized()) {
			writer.print(" = ");
			writer.print(String.valueOf(fieldDeclaration.getValue()));
		}
		writer.println(";");
		writer.println();
	}

}
