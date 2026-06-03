package io.vexis.polaris.shared;

import org.springframework.stereotype.Component;

@Component
public class TextUtils {

  public static String normalizeText(String text) {
    if (text == null) {
      return null;
    }
    return text.toLowerCase();
  }
}
