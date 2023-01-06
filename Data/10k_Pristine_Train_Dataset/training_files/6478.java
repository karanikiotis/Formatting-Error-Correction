/**
 * Copyright (c) 2014 Richard Warburton (richard.warburton@gmail.com)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 **/
package com.insightfullogic.honest_profiler.ports.javafx.view.cell;

import static com.insightfullogic.honest_profiler.ports.javafx.view.Rendering.renderPercentage;
import static javafx.geometry.Pos.CENTER_RIGHT;
import static javafx.scene.text.TextAlignment.RIGHT;

import java.util.function.Function;

import javafx.scene.control.TableCell;

public class PercentageTableCell<T> extends TableCell<T, Number>
{
    private Function<Number, String> styleFunction;

    public PercentageTableCell(Function<Number, String> styleFunction)
    {
        super();

        setTextAlignment(RIGHT);
        setAlignment(CENTER_RIGHT);

        this.styleFunction = styleFunction;
    }

    @Override
    protected void updateItem(Number item, boolean isEmpty)
    {
        if (isEmpty || item == null)
        {
            setText(null);
            setStyle(null);
            return;
        }

        setText(renderPercentage(item.doubleValue()));
        setStyle(this.styleFunction == null ? null : this.styleFunction.apply(item));
    }
}
