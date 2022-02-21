package com.silverforge.controls;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class LoaderTextAnimation extends Animation {
    private BusyIndicator busyIndicator;

    public LoaderTextAnimation(BusyIndicator busyIndicator) {
        this.busyIndicator = busyIndicator;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        int alphaModifier = 127;

        int newAlpha = (int) (alphaModifier * interpolatedTime);
        busyIndicator.setTextAlpha(newAlpha - alphaModifier);
        busyIndicator.setTextAlpha(alphaModifier + newAlpha);

        busyIndicator.requestLayout();
    }
}
