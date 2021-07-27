package io.spring.initializr.generator.language.java;

public class JavaVariableExpressionStatement extends JavaStatement {

    private final String type;
    private final String name;
    private final JavaExpression expression;

    public JavaVariableExpressionStatement(String type, String name, JavaExpression expression) {
        this.type = type;
        this.name = name;
        this.expression = expression;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public JavaExpression getExpression() {
        return expression;
    }
}
