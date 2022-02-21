/**
 * Wire
 * Copyright (C) 2016 Wire Swiss GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.waz.zclient.controllers.mentioning;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.waz.api.IConversation;
import com.waz.api.UpdateListener;
import com.waz.api.User;
import com.waz.api.UsersList;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MentioningController implements IMentioningController {

    private static final Pattern QUERY_PATTERN = Pattern.compile("\\B@(\\w+)(\\B)?");
    private final UpdateListener conversationUpdateListener = new UpdateListener() {
        @Override
        public void updated() {
            updateMembers();
        }

    };
    private final UpdateListener membersUpdateListener = new UpdateListener() {
        @Override
        public void updated() {
            performSearch();
        }
    };
    private Set<MentioningObserver> observers = new HashSet<>();
    private IConversation conversation;
    private UsersList members;
    private String query;

    @Override
    public void addObserver(MentioningObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(MentioningObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void query(String query, float x, float y) {
        updatePosition(x, y);
        if (this.query != null &&
            this.query.equals(query)) {
            return;
        }
        this.query = query;
        performSearch();
    }

    private void updatePosition(float x, float y) {
        for (MentioningObserver observer : observers) {
            observer.onCursorPositionChanged(x, y);
        }
    }

    @Override
    public String extractQuery(int cursorPosition, String text) {
        if (cursorPosition == -1 || TextUtils.isEmpty(text)) {
            return null;
        }
        final String[] words = text.split("[ \n]+");
        int length = 0;
        String query = null;
        for (String word : words) {
            final int queryLength = Math.max(cursorPosition - length - 1, 0);
            length += word.length() + 1;
            if (length > cursorPosition) {
                final Matcher queryMatcher = QUERY_PATTERN.matcher(word);
                if (queryMatcher.matches()) {
                    query = word.replaceFirst(QUERY_PATTERN.pattern(), "$1").toLowerCase(Locale.getDefault());
                    query = query.substring(0, queryLength);
                }
                break;
            }
        }
        return query;
    }

    @Override
    public void setCurrentConversation(@NonNull IConversation conversation) {
        if (this.conversation != null) {
            this.conversation.removeUpdateListener(conversationUpdateListener);
            this.conversation = null;
        }
        this.query = null;
        this.conversation = conversation;
        this.conversation.addUpdateListener(conversationUpdateListener);
        conversationUpdateListener.updated();
    }

    @Override
    public void hide() {
        for (MentioningObserver observer : observers) {
            observer.onQueryResultChanged(Collections.<User>emptyList());
        }
    }

    @Override
    public void completeUser(@NonNull User user) {
        for (MentioningObserver observer : observers) {
            observer.onMentionedUserSelected(query, user);
        }
        query = null;
    }

    private void performSearch() {
        List<User> result = new LinkedList<>();
        if (conversation != null &&
            conversation.getType() == IConversation.Type.GROUP &&
            !TextUtils.isEmpty(query)) {
            final int membersSize = members == null ? 0 : members.size();
            for (int i = 0; i < membersSize; i++) {
                final User user = members.get(i);
                if (user.isMe()) {
                    continue;
                }
                final String name = user.getName().toLowerCase(Locale.getDefault());
                if (name.contains(query)) {
                    result.add(user);
                }
            }
        }
        for (MentioningObserver observer : observers) {
            observer.onQueryResultChanged(result);
        }
    }

    @Override
    public void tearDown() {
        observers.clear();
        if (conversation != null) {
            conversation.removeUpdateListener(conversationUpdateListener);
            conversation = null;
        }
        if (members != null) {
            members.removeUpdateListener(membersUpdateListener);
            members = null;
        }
        query = null;
    }

    private void updateMembers() {
        if (members != null) {
            members.removeUpdateListener(membersUpdateListener);
            members = null;
        }
        members = conversation.getUsers();
        if (members == null) {
            return;
        }
        members.addUpdateListener(membersUpdateListener);
        performSearch();
    }
}
