/*
 * Copyright (c) 2017 UniqueStudio
 *
 *
 * This file is part of ParsingPlayer.
 *
 * ParsingPlayer is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with ParsingPlayer; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */

package com.hustunique.parsingplayer.player.media;

/**
 * Created by JianGuo on 2/19/17.
 * Listener for state in {@link ParsingMediaManager}
 */

public interface MediaStateChangeListener {
    void onPrepared();
    void onError(String msg);
    void onPlayCompleted();
    void onBufferingStart();
    void onBufferingEnd();
}
