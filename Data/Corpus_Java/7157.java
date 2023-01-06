/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tools.sherpa.drawing.decorator;

import com.android.tools.sherpa.drawing.ViewTransform;
import android.support.constraint.solver.widgets.ConstraintWidget;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * Switch Widget decorator
 */
public class SwitchWidget extends TextWidget {
    private static final int sSwitchWidth = 40;
    private static final int sSwitchHeight = 28;

    /**
     * Base constructor
     *
     * @param widget the widget we are decorating
     * @param text   the text content
     */
    public SwitchWidget(ConstraintWidget widget, String text) {
        super(widget, text);
        mAlignmentY = TEXT_ALIGNMENT_CENTER;
        mWidget.setMinWidth(28);
        mWidget.setMinHeight(28);
    }

    @Override
    protected void wrapContent() {
        if (mWidget == null) {
            return;
        }
        if (!TextWidget.DO_WRAP) {
            return;
        }
        super.wrapContent();
        int extra = sSwitchWidth + 2 * mHorizontalPadding;
        mWidget.setMinWidth(mWidget.getMinWidth() + extra);
        mWidget.setMinHeight(Math.max(mWidget.getMinHeight(), sSwitchHeight));
        mWidget.setDimension(0, 0);
    }

    @Override
    public void onPaintBackground(ViewTransform transform, Graphics2D g) {
        super.onPaintBackground(transform, g);
        if (mColorSet.drawBackground()) {
            int originalSize = mFont.getSize();
            int scaleSize = transform.getSwingDimension(originalSize);
            g.setFont(mFont.deriveFont((float) scaleSize));
            FontMetrics fontMetrics = g.getFontMetrics();

            int x = transform.getSwingX(mWidget.getDrawX());
            int y = transform.getSwingY(mWidget.getDrawY());
            int h = transform.getSwingDimension(mWidget.getDrawHeight());
            int w = transform.getSwingDimension(sSwitchWidth);
            g.setColor(Color.WHITE);
            x += mHorizontalMargin + mHorizontalPadding + fontMetrics.stringWidth(getText());
            int barHeight = transform.getSwingDimension(mWidget.getDrawHeight() / 3);
            g.drawRoundRect(x + 2, y + h / 2 - barHeight / 2, w - 4, barHeight, h / 4, h / 4);
            g.fillArc(x + mHorizontalPadding, y + h / 6, 2 * h / 3, 2 * h / 3, 0, 360);
        }
    }
}
