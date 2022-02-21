package com.netflix.raigad.scheduler;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.junit.Test;

public class TestGuiceSingleton
{
    public static class GModules extends AbstractModule
    {
        @Override
        protected void configure()
        {
            bind(EmptyInterface.class).to(GuiceSingleton.class).asEagerSingleton();
        }

    }

    public interface EmptyInterface
    {
        public String print();
    }

    @Singleton
    public static class GuiceSingleton implements EmptyInterface
    {

        public String print()
        {
            System.out.println(this.toString());
            return this.toString();
        }
    }

    @Test
    public void testSingleton()
    {
        Injector injector = Guice.createInjector(new GModules());
        injector.getInstance(EmptyInterface.class).print();
        injector.getInstance(EmptyInterface.class).print();
        injector.getInstance(EmptyInterface.class).print();
        printInjected();
        printInjected();
        printInjected();
        printInjected();
    }

    public void printInjected()
    {
        Injector injector = Guice.createInjector(new GModules());
        injector.getInstance(EmptyInterface.class).print();
    }

}
