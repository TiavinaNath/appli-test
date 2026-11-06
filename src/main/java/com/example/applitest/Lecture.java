package com.example.applitest;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/** One way of reading a variable, reduced to what can be shown: never the value itself. */
public record Lecture(String forme, Integer longueur, String empreinte) {
  private static final int EMPREINTE_HEX_LENGTH = 8;

  public static Lecture of(String forme, String valeur) {
    if (valeur == null) {
      return new Lecture(forme, null, null);
    }
    return new Lecture(forme, valeur.length(), sha256Prefix(valeur));
  }

  public boolean presente() {
    return longueur != null;
  }

  private static String sha256Prefix(String valeur) {
    try {
      var digest = MessageDigest.getInstance("SHA-256").digest(valeur.getBytes(UTF_8));
      return HexFormat.of().formatHex(digest).substring(0, EMPREINTE_HEX_LENGTH);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
  }
}
