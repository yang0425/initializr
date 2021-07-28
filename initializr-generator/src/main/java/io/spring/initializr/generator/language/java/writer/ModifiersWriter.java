package io.spring.initializr.generator.language.java.writer;

import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import io.spring.initializr.generator.io.IndentingWriter;

public class ModifiersWriter implements CodeWriter {

	private final Map<Predicate<Integer>, String> availableModifiers;

	private final int declaredModifiers;

	public ModifiersWriter(Map<Predicate<Integer>, String> availableModifiers, int declaredModifiers) {
		this.availableModifiers = availableModifiers;
		this.declaredModifiers = declaredModifiers;
	}

	@Override
	public void write(IndentingWriter writer) {
		String modifiers = this.availableModifiers.entrySet().stream()
				.filter((entry) -> entry.getKey().test(this.declaredModifiers)).map(Map.Entry::getValue)
				.collect(Collectors.joining(" "));
		if (!modifiers.isEmpty()) {
			writer.print(modifiers);
			writer.print(" ");
		}
	}

}
