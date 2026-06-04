package io.vexis.polaris.shared;

import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class TextUtils {

  private static final String DEFAULT_COLOR = "#6B7280";

  public static String normalizeText(String text) {
    if (text == null) {
      return null;
    }
    return text.toLowerCase();
  }

  public static String normalizeTag(String tag) {
    return Normalizer.normalize(tag, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .replaceAll("[^a-zA-Z0-9]", "")
            .toUpperCase();
  }

  public static String normalizeColor(String color) {
    return color == null ? DEFAULT_COLOR : color;
  }
}
