package com.wildbeeslabs.jentle.algorithms;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class TranslationsTest {

    @Test(dataProvider = "languages")
    public void readTesting(final String language) throws IOException {
        System.out.println("Translation for key 'testing' in language '" + language + "' is : " +
            getTranslation("testing", language));
    }

    @Test(dataProvider = "languages")
    public void readSuperheroes(final String language) throws IOException {
        System.out.println("For language '" + language + "' - superhero firstname and lastname: " +
            getTranslation("superhero.firstname", language) + " " + getTranslation
            ("superhero.lastname", language));
    }
    // getTranslation(key, language)

    protected String getTranslation(final String key, final String language) throws IOException {
        final Properties prop = new Properties();
        final FileInputStream input = new FileInputStream("src/main/resources/languages/" + language + ".properties");
        prop.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        input.close();
        return prop.getProperty(key);
    }

    @DataProvider(name = "languages")
    public Object[][] languages() {
        return new Object[][]{
            {"en"},
            {"de"},
            {"es"},
            {"no"}
        };
    }
}
