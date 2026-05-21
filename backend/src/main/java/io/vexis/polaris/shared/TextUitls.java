package io.vexis.polaris.shared;

import org.springframework.stereotype.Component;

@Component
public class TextUitls {

    public static String normalizeText(String text) {
        return text.toLowerCase();
    }
}
