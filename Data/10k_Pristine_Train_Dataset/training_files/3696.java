package net.peakgames.libgdx.stagebuilder.core.demo;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.peakgames.libgdx.stagebuilder.core.AbstractGame;
import net.peakgames.libgdx.stagebuilder.core.builder.StageBuilder;

public class ReplaceScreenTwo extends DemoScreen {

    public ReplaceScreenTwo(final AbstractGame game) {
        super(game);
        initializeListeners();
    }

    private void initializeListeners() {
        findButton("replaceScreen1").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ReplaceScreenOne screenOne = new ReplaceScreenOne(game);
                game.replaceTopScreen(screenOne);
            }
        });
    }


    @Override
    public void show() {
        super.show();
        StageBuilder.disableMultiTouch(this.stage);
    }
}
