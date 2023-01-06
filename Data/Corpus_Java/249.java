/**
 * Copyright 2010 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package playn.core;

/**
 * A path object created by {@link Canvas#createPath}.
 */
public interface Path {

  /**
   * Resets the current path, removing all strokes and moving the position to (0, 0).
   *
   * @return this path for convenient call chaining.
   */
  Path reset();

  /**
   * Closes the path, returning the position to the beginning of the first stroke.
   *
   * @return this path for convenient call chaining.
   */
  Path close();

  /**
   * Moves the position to the given location.
   *
   * @return this path for convenient call chaining.
   */
  Path moveTo(float x, float y);

  /**
   * Adds a line to the path, from the current position to the specified target.
   *
   * @return this path for convenient call chaining.
   */
  Path lineTo(float x, float y);

  /**
   * Adds a quadratic curve to the path, from the current position to the specified target, with
   * the specified control point.
   *
   * @return this path for convenient call chaining.
   */
  Path quadraticCurveTo(float cpx, float cpy, float x, float y);

  /**
   * Adds a bezier curve to the path, from the current position to the specified target, using the
   * supplied control points.
   *
   * @return this path for convenient call chaining.
   */
  Path bezierTo(float c1x, float c1y, float c2x, float c2y, float x, float y);

  // TODO(jgw): fill rules (HTML Canvas doesn't seem to have anything)
  // Android has [inverse] winding, even-odd
  // Flash has even-odd, non-zero
}
