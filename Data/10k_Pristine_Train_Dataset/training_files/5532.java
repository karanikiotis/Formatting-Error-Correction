/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.a;

import mage.MageInt;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.common.BeginningOfEndStepTriggeredAbility;
import mage.abilities.dynamicvalue.DynamicValue;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.abilities.keyword.FlyingAbility;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;
import mage.counters.CounterType;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.ZoneChangeEvent;
import mage.watchers.Watcher;

import java.util.UUID;

/**
 * @author Plopman
 */
public class AsmiraHolyAvenger extends CardImpl {

    public AsmiraHolyAvenger(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{2}{G}{W}");
        addSuperType(SuperType.LEGENDARY);
        this.subtype.add("Human");
        this.subtype.add("Cleric");

        this.power = new MageInt(2);
        this.toughness = new MageInt(3);

        // Flying
        this.addAbility(FlyingAbility.getInstance());
        // At the beginning of each end step, put a +1/+1 counter on Asmira, Holy Avenger for each creature put into your graveyard from the battlefield this turn.
        this.addAbility(new BeginningOfEndStepTriggeredAbility(new AddCountersSourceEffect(CounterType.P1P1.createInstance(0), new AsmiraHolyAvengerDynamicValue(), true), TargetController.ANY, false), new AsmiraHolyAvengerWatcher());
    }

    public AsmiraHolyAvenger(final AsmiraHolyAvenger card) {
        super(card);
    }

    @Override
    public AsmiraHolyAvenger copy() {
        return new AsmiraHolyAvenger(this);
    }
}


class AsmiraHolyAvengerWatcher extends Watcher {

    private int creaturesCount = 0;

    public AsmiraHolyAvengerWatcher() {
        super(AsmiraHolyAvengerWatcher.class.getSimpleName(), WatcherScope.PLAYER);
        condition = true;
    }

    public AsmiraHolyAvengerWatcher(final AsmiraHolyAvengerWatcher watcher) {
        super(watcher);
        this.creaturesCount = watcher.creaturesCount;
    }

    @Override
    public AsmiraHolyAvengerWatcher copy() {
        return new AsmiraHolyAvengerWatcher(this);
    }

    public int getCreaturesCount() {
        return creaturesCount;
    }

    @Override
    public void watch(GameEvent event, Game game) {
        if (event.getType() == GameEvent.EventType.ZONE_CHANGE && ((ZoneChangeEvent) event).isDiesEvent()) {
            MageObject card = game.getLastKnownInformation(event.getTargetId(), Zone.BATTLEFIELD);
            if (card != null && ((Card) card).getOwnerId().equals(this.controllerId) && card.isCreature()) {
                creaturesCount++;
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        creaturesCount = 0;
    }
}

class AsmiraHolyAvengerDynamicValue implements DynamicValue {

    @Override
    public int calculate(Game game, Ability sourceAbility, Effect effect) {
        AsmiraHolyAvengerWatcher watcher = (AsmiraHolyAvengerWatcher) game.getState().getWatchers().get(AsmiraHolyAvengerWatcher.class.getSimpleName(), sourceAbility.getControllerId());
        if (watcher != null) {
            return watcher.getCreaturesCount();
        }
        return 0;
    }

    @Override
    public AsmiraHolyAvengerDynamicValue copy() {
        return new AsmiraHolyAvengerDynamicValue();
    }

    @Override
    public String toString() {
        return "1";
    }

    @Override
    public String getMessage() {
        return "creature put into your graveyard from the battlefield this turn";
    }
}