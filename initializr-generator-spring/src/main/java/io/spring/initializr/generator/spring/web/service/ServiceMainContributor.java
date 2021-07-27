package io.spring.initializr.generator.spring.web.service;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.spring.code.MainSourceCodeCustomizer;
import io.spring.initializr.generator.spring.util.LambdaSafe;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;

class ServiceMainContributor implements
    MainSourceCodeCustomizer<TypeDeclaration, CompilationUnit<TypeDeclaration>, SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>>> {

  private static final String CLASS_NAME = "HelloWorldService";

  private final String packageName;

  private final ObjectProvider<ServiceMainCustomizer<?>> serviceMainCustomizers;

  ServiceMainContributor(String packageName,
                         ObjectProvider<ServiceMainCustomizer<?>> serviceMainCustomizers) {
    this.packageName = packageName;
    this.serviceMainCustomizers = serviceMainCustomizers;
  }

  @Override
  public void customize(SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode) {
    CompilationUnit<TypeDeclaration> compilationUnit =
        sourceCode.createCompilationUnit(this.packageName, CLASS_NAME);
    TypeDeclaration servletInitializer = compilationUnit.createTypeDeclaration(CLASS_NAME);
    customizeControllerMain(servletInitializer);
  }

  @SuppressWarnings("unchecked")
  private void customizeControllerMain(TypeDeclaration controllerMain) {
    List<ServiceMainCustomizer<?>> customizers = this.serviceMainCustomizers.orderedStream()
        .collect(Collectors.toList());
    LambdaSafe.callbacks(ServiceMainCustomizer.class, customizers, controllerMain)
        .invoke((customizer) -> customizer.customize(controllerMain));
  }

}
