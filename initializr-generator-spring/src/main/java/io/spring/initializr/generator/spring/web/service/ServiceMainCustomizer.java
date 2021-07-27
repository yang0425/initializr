package io.spring.initializr.generator.spring.web.service;

import io.spring.initializr.generator.language.TypeDeclaration;
import org.springframework.core.Ordered;

@FunctionalInterface
public interface ServiceMainCustomizer<T extends TypeDeclaration> extends Ordered {

  void customize(T typeDeclaration);

  @Override
  default int getOrder() {
    return 0;
  }

}
