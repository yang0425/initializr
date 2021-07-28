package io.spring.initializr.generator.language.java.writer;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.language.Annotatable;
import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.Annotation.Attribute;

public class AnnotationsWriter implements CodeWriter {

	private final Annotatable annotatable;

	public AnnotationsWriter(Annotatable annotatable) {
		this.annotatable = annotatable;
	}

	@Override
	public void write(IndentingWriter writer) {
		annotatable.getAnnotations().stream().flatMap(this::createCodeWriters)
				.forEach(codeWriter -> codeWriter.write(writer));
	}

	private Stream<CodeWriter> createCodeWriters(Annotation annotation) {
		List<CodeWriter> codeWriters = new LinkedList<>();

		codeWriters.add(writer -> writer.print("@" + getUnqualifiedName(annotation.getName())));

		List<Attribute> attributes = annotation.getAttributes();
		if (!attributes.isEmpty()) {
			codeWriters.add(writer -> writer.print("("));
			if (attributes.size() == 1 && attributes.get(0).getName().equals("value")) {
				codeWriters.add(writer -> writer.print(formatAnnotationAttribute(attributes.get(0))));
			}
			else {
				codeWriters.add(writer -> writer.print(attributes.stream()
						.map((attribute) -> attribute.getName() + " = " + formatAnnotationAttribute(attribute))
						.collect(Collectors.joining(", "))));
			}

			codeWriters.add(writer -> writer.print(")"));
		}
		codeWriters.add(IndentingWriter::println);
		return codeWriters.stream();
	}

	private String formatAnnotationAttribute(Attribute attribute) {
		List<String> values = attribute.getValues();
		if (attribute.getType().equals(Class.class)) {
			return formatValues(values, (value) -> String.format("%s.class", getUnqualifiedName(value)));
		}
		if (Enum.class.isAssignableFrom(attribute.getType())) {
			return formatValues(values, (value) -> {
				String enumValue = value.substring(value.lastIndexOf(".") + 1);
				String enumClass = value.substring(0, value.lastIndexOf("."));
				return String.format("%s.%s", getUnqualifiedName(enumClass), enumValue);
			});
		}
		if (attribute.getType().equals(String.class)) {
			return formatValues(values, (value) -> String.format("\"%s\"", value));
		}
		return formatValues(values, (value) -> String.format("%s", value));
	}

	private String formatValues(List<String> values, Function<String, String> formatter) {
		String result = values.stream().map(formatter).collect(Collectors.joining(", "));
		return (values.size() > 1) ? "{ " + result + " }" : result;
	}

}
