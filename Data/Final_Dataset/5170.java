// **********************************************************************
//
// Copyright (c) 2003-2017 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

import Demo.*;

public class HelloI implements Hello
{
    @Override
    public void sayHello(com.zeroc.Ice.Current current)
    {
        System.out.println("Hello World!");
    }
}
