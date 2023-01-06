/*
 * Copyright (c) 2006 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.testng;

import java.util.*;

import org.testng.annotations.*;

import mockit.*;
import mockit.integration.*;

// These tests are expected to fail, so they are kept inactive.
@Test(enabled = false)
public final class TestNGViolatedExpectationsTest
{
   // Tests that fail with a "missing invocation" error ///////////////////////////////////////////////////////////////

   @Test
   public void expectInvocationWhichDoesNotOccurInTestedCodeThatThrowsAnException_strict1(@Injectable Collaborator mock)
   {
      new CollaboratorStrictExpectations(mock);
   }

   @Test
   public void expectInvocationWhichDoesNotOccurInTestedCodeThatThrowsAnException_nonStrict1(@Mocked Collaborator mock)
   {
      new CollaboratorExpectations(mock);
   }

   // Tests that fail with the exception thrown by tested code ////////////////////////////////////////////////////////

   @Test
   public void expectInvocationWhichDoesNotOccurInTestedCodeThatThrowsAnException_strict2(@Injectable Collaborator mock)
   {
      new CollaboratorStrictExpectations(mock);

      mock.doSomething();
   }

   @Test
   public void expectInvocationWhichDoesNotOccurInTestedCodeThatThrowsAnException_nonStrict2(@Mocked Collaborator mock)
   {
      new CollaboratorExpectations(mock);

      mock.doSomething();
   }

   // Tests that fail with an "unexpected invocation" error ///////////////////////////////////////////////////////////

   @Test
   public void expectInvocationWhichDoesNotOccurInTestedCodeThatThrowsAnException_strict3(@Injectable Collaborator mock)
   {
      new CollaboratorStrictExpectations(mock);

      new Collaborator();
   }

   @Test
   public void expectInvocationWhichDoesNotOccurInTestedCodeThatThrowsAnException_nonStrict3(@Mocked Collaborator mock)
   {
      new CollaboratorExpectations(mock);

      new Collaborator();
      new Collaborator();
   }

   // Tests that fail with a "missing invocation" error after the exception thrown by tested code /////////////////////

   @Test(expectedExceptions = IllegalFormatCodePointException.class)
   public void expectInvocationWhichDoesNotOccurInTestedCodeThatThrowsAnException_strict4(@Injectable Collaborator mock)
   {
      new CollaboratorStrictExpectations(mock);

      mock.doSomething();
   }

   @Test(expectedExceptions = IllegalFormatCodePointException.class)
   public void expectInvocationWhichDoesNotOccurInTestedCodeThatThrowsAnException_nonStrict4(@Mocked Collaborator mock)
   {
      new CollaboratorExpectations(mock);

      mock.doSomething();
   }

   // Tests that fail with a different exception than expected ////////////////////////////////////////////////////////

   @Test(expectedExceptions = AssertionError.class)
   public void expectInvocationWhichDoesNotOccurInTestedCodeThatThrowsAnException_strict5(@Injectable Collaborator mock)
   {
      new CollaboratorStrictExpectations(mock);

      mock.doSomething();
   }

   @Test(expectedExceptions = AssertionError.class)
   public void expectInvocationWhichDoesNotOccurInTestedCodeThatThrowsAnException_nonStrict5(@Mocked Collaborator mock)
   {
      new CollaboratorExpectations(mock);

      mock.doSomething();
   }

   // Tests that fail without the expected exception being thrown /////////////////////////////////////////////////////

   @Test(expectedExceptions = AssertionError.class)
   public void expectInvocationWhichDoesNotOccurInTestedCodeThatThrowsAnException_strict6(@Injectable Collaborator mock)
   {
      new CollaboratorStrictExpectations(mock);
   }

   @Test(expectedExceptions = AssertionError.class)
   public void expectInvocationWhichDoesNotOccurInTestedCodeThatThrowsAnException_nonStrict6(@Mocked Collaborator mock)
   {
      new CollaboratorExpectations(mock);
   }
}
