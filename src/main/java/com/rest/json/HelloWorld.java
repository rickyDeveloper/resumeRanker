package com.rest.json;

/**
 * Created by vikasnaiyar on 17/09/17.
 */
public class HelloWorld {

    private final long id;
    private final String content;

    public HelloWorld(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

}
