package ru.rosroble.client.ui.properties;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleBundle {
    private static final String path = "ru.rosroble.client.ui.properties.";
    private static final ResourceBundle bundleRu = ResourceBundle.getBundle(path + "resources_ru_RU", new Locale("ru", "RU"));
    private static final ResourceBundle bundleEn = ResourceBundle.getBundle(path + "resources_en_CA", new Locale("en", "CA"));
    private static final ResourceBundle bundleCs = ResourceBundle.getBundle(path + "resources_cs_CZ");
    private static final ResourceBundle bundleSq = ResourceBundle.getBundle(path + "resources_sq_AL");

    private static String currentLanguage = "English";

    public static ResourceBundle getBundle(String language) {
        switch (language) {
            case "English":
                return bundleEn;
            case "Русский":
                return bundleRu;
            case "Česko":
                return bundleCs;
            case "Shqiptare":
                return bundleSq;
        }
        throw new InvalidParameterException();
    }

    public static ResourceBundle getCurrentBundle() {
        return getBundle(currentLanguage);
    }

    public static void setCurrentLanguage(String language) {
        currentLanguage = language;
    }

    public static String getValue(String key) {
        return new String(getBundle(currentLanguage).getString(key).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
}
