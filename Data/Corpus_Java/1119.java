/**
 * Copyright 2010 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package playn.java;

import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import playn.core.Gradient;

class JavaGradient extends Gradient {

  static JavaGradient create(Linear cfg) {
    Point2D.Float start = new Point2D.Float(cfg.x0, cfg.y0);
    Point2D.Float end = new Point2D.Float(cfg.x1, cfg.y1);
    Color[] javaColors = convertColors(cfg.colors);
    return new JavaGradient(new LinearGradientPaint(start, end, cfg.positions, javaColors));
  }

  static JavaGradient create(Radial cfg) {
    Point2D.Float center = new Point2D.Float(cfg.x, cfg.y);
    Color[] javaColors = convertColors(cfg.colors);
    return new JavaGradient(new RadialGradientPaint(center, cfg.r, cfg.positions, javaColors));
  }

  private static Color[] convertColors(int[] colors) {
    Color[] javaColors = new Color[colors.length];
    for (int i = 0; i < colors.length; ++i) {
      javaColors[i] = new Color(colors[i], true);
    }
    return javaColors;
  }

  Paint paint;

  private JavaGradient(Paint paint) {
    this.paint = paint;
  }
}
