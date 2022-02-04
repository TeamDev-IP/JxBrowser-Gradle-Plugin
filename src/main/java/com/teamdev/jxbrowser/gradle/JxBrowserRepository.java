package com.teamdev.jxbrowser.gradle;

/**
 * A location of the JxBrowser Maven repositories.
 */
public enum JxBrowserRepository {

    /**
     * A repository in the United States.
     */
    US("https://us-maven.pkg.dev/jxbrowser/releases"),

    /**
     * A repository in Europe.
     */
    EUROPE("https://europe-maven.pkg.dev/jxbrowser/releases");

    private final String url;

    JxBrowserRepository(String url) {
        this.url = url;
    }

    String url() {
        return url;
    }
}
