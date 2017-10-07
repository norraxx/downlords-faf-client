package com.faforever.client.fx;

public interface Controller<ROOT> {

  ROOT getRoot();

  default void initialize() {

  }
}
