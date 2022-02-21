package com.deswaef.netflixexamples.api.reddit.model;

import java.util.List;

public class RedditData {
    private List<RedditDataChildren> children;

    public List<RedditDataChildren> getChildren() {
        return children;
    }

    public RedditData setChildren(List<RedditDataChildren> children) {
        this.children = children;
        return this;
    }
}
