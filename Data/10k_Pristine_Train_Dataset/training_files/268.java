/*
 * Copyright 2003-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrains.mps.smodel.event;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.mps.openapi.event.AbstractModelChangeEvent;
import org.jetbrains.mps.openapi.event.SNodeAddEvent;
import org.jetbrains.mps.openapi.event.SNodeRemoveEvent;
import org.jetbrains.mps.openapi.event.SPropertyChangeEvent;
import org.jetbrains.mps.openapi.event.SReferenceChangeEvent;
import org.jetbrains.mps.openapi.model.SNodeChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of model change {@link SNodeChangeListener listener} that collects events as commanded.
 * Multiple start/stop sequences are possible.
 * Nested start/stop are not supported. Initial state is stopped.
 * <p/>
 * Not thread-safe, it's caller's responsibility to ensure single-thread use or to synchronize access.
 *
 * @author Artem Tikhomirov
 */
public class NodeChangeCollector implements SNodeChangeListener {
  private final List<AbstractModelChangeEvent> myEvents = new ArrayList<AbstractModelChangeEvent>();
  private boolean myEnabled;

  /**
   * Unconditionally enable event collection
   */
  public void start() {
    myEnabled = true;
  }

  /**
   * Unconditionally disable event collection.
   * Events collected so far are kept.
   */
  public void stop() {
    myEnabled = false;
  }

  /**
   * Gives access to events collected so far and clears all collected events.
   * Doesn't change start/stop state.
   * @return ordered collection of model change events, or empty list if none had happened.
   */
  @NotNull
  public List<AbstractModelChangeEvent> purge() {
    ArrayList<AbstractModelChangeEvent> rv = new ArrayList<AbstractModelChangeEvent>(myEvents);
    myEvents.clear();
    return rv;
  }

  @Override
  public void propertyChanged(@NotNull SPropertyChangeEvent event) {
    if (myEnabled) {
      myEvents.add(event);
    }
  }

  @Override
  public void referenceChanged(@NotNull SReferenceChangeEvent event) {
    if (myEnabled) {
      myEvents.add(event);
    }
  }

  @Override
  public void nodeAdded(@NotNull SNodeAddEvent event) {
    if (myEnabled) {
      myEvents.add(event);
    }
  }

  @Override
  public void nodeRemoved(@NotNull SNodeRemoveEvent event) {
    if (myEnabled) {
      myEvents.add(event);
    }
  }
}
