/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.data.processor.data.entity;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutablePassengerData;
import org.spongepowered.api.data.manipulator.mutable.entity.PassengerData;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.data.value.immutable.ImmutableListValue;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.World;
import org.spongepowered.common.data.manipulator.mutable.entity.SpongePassengerData;
import org.spongepowered.common.data.processor.common.AbstractEntitySingleDataProcessor;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeListValue;
import org.spongepowered.common.data.value.mutable.SpongeListValue;
import org.spongepowered.common.entity.EntityUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class PassengerDataProcessor extends AbstractEntitySingleDataProcessor<net.minecraft.entity.Entity, List<UUID>, ListValue<UUID>, PassengerData, ImmutablePassengerData> {

    public PassengerDataProcessor() {
        super(net.minecraft.entity.Entity.class, Keys.PASSENGERS);
    }

    @Override
    protected boolean set(net.minecraft.entity.Entity entity, List<UUID> uuids) {
        final List<net.minecraft.entity.Entity> passengers = uuids.stream()
                .map((uuid -> ((World) entity.getEntityWorld()).getEntity(uuid)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(EntityUtil::toNative)
                .collect(Collectors.toList());

        for (net.minecraft.entity.Entity passenger : passengers) {
            if (!entity.isPassenger(passenger)) {
                entity.getPassengers().add(passenger);
            }
        }
        return true;
    }

    @Override
    protected Optional<List<UUID>> getVal(net.minecraft.entity.Entity dataHolder) {
        if (dataHolder.getPassengers().isEmpty()) {
            return Optional.empty();
        }
        final List<UUID> passengers = dataHolder.getPassengers()
                .stream()
                .map(EntityUtil::fromNative)
                .map(Entity::getUniqueId)
                .collect(Collectors.toList());
        return Optional.of(passengers);
    }

    @Override
    protected ImmutableListValue<UUID> constructImmutableValue(List<UUID> value) {
        return new ImmutableSpongeListValue<>(Keys.PASSENGERS, ImmutableList.copyOf(value));
    }

    @Override
    protected ListValue<UUID> constructValue(List<UUID> actualValue) {
        return new SpongeListValue<>(Keys.PASSENGERS, actualValue);
    }

    @Override
    public DataTransactionResult removeFrom(ValueContainer<?> container) {
        if (this.supports(container)) {
            net.minecraft.entity.Entity entity = ((net.minecraft.entity.Entity) container);
            if (entity.getPassengers().isEmpty()) {
                final ImmutableList<UUID> passengers = entity.getPassengers()
                        .stream()
                        .map(EntityUtil::fromNative)
                        .map(Entity::getUniqueId)
                        .collect(ImmutableList.toImmutableList());
                entity.removePassengers();
                return DataTransactionResult.builder().result(DataTransactionResult.Type.SUCCESS).replace(constructImmutableValue(passengers)).build();
            }
            return DataTransactionResult.successNoData();
        }
        return DataTransactionResult.failNoData();
    }

    @Override
    public Optional<PassengerData> fill(DataContainer container, PassengerData passengerData) {
        passengerData.set(Keys.PASSENGERS, container.getObjectList(Keys.PASSENGERS.getQuery(), UUID.class).get());
        return Optional.of(passengerData);
    }

    @Override
    protected PassengerData createManipulator() {
        return new SpongePassengerData();
    }
}
