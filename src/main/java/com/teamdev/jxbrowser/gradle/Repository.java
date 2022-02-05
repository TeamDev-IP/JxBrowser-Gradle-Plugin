package com.teamdev.jxbrowser.gradle;

/**
 * Available JxBrowser repositories.
 */
public enum Repository {

    /**
     * A repository located in the United States.
     */
    US("https://us-maven.pkg.dev/jxbrowser/releases"),

    /**
     * A repository located in Europe.
     */
    EUROPE("https://europe-maven.pkg.dev/jxbrowser/releases");

    private final String url;

    Repository(String url) {
        this.url = url;
    }

    String url() {
        return url;
    }
}
