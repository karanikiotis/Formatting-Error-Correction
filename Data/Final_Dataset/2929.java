/*
package pl.edu.amu.wmi.daut.re;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
*/
//public class TestGrammarUtils extends TestCase {
/**
 * Test sprawdzający działanie metody isChomsky() issue #560.
 */
/*  public final void testIsChomsky() {

        GrammarNonterminalSymbol startSymbol = new GrammarNonterminalSymbol('S');
        GrammarNonterminalSymbol symbolA = new GrammarNonterminalSymbol('A');
        GrammarNonterminalSymbol symbolB = new GrammarNonterminalSymbol('B');
        GrammarNonterminalSymbol symbolC = new GrammarNonterminalSymbol('C');

        GrammarTerminalSymbol symbola = new GrammarTerminalSymbol('a');
        GrammarTerminalSymbol symbolb = new GrammarTerminalSymbol('b');
        GrammarTerminalSymbol symbolc = new GrammarTerminalSymbol('c');

        Grammar grammar = new Grammar(startSymbol);

        List<GrammarSymbol> tmp = new ArrayList<GrammarSymbol>();
        tmp.add(symbolA);
        tmp.add(symbolB);
        tmp.add(symbolC);

        grammar.addRule(new GrammarRule(startSymbol, tmp));
        grammar.addRule(new GrammarRule(symbolA, symbola));
        grammar.addRule(new GrammarRule(symbolB, symbolb));
        grammar.addRule(new GrammarRule(symbolC, symbolc));

        assertFalse(GrammarUtils.isChomsky(grammar));

        GrammarNonterminalSymbol startSymbol2 = new GrammarNonterminalSymbol('S');
        GrammarNonterminalSymbol symbolA2 = new GrammarNonterminalSymbol('A');
        GrammarNonterminalSymbol symbolB2 = new GrammarNonterminalSymbol('B');

        GrammarTerminalSymbol symbola2 = new GrammarTerminalSymbol('a');
        GrammarTerminalSymbol symbolb2 = new GrammarTerminalSymbol('b');

        Grammar grammar2 = new Grammar(startSymbol2);

        List<GrammarSymbol> tmp2 = new ArrayList<GrammarSymbol>();
        tmp.add(symbolA2);
        tmp.add(symbolB2);

        grammar2.addRule(new GrammarRule(startSymbol2, tmp2));
        grammar2.addRule(new GrammarRule(symbolA2, symbola2));
        grammar2.addRule(new GrammarRule(symbolB2, symbolb2));

        assertTrue(GrammarUtils.isChomsky(grammar2));
    }
}*/
/**
 * Test sprawdzający działanie metody isGreibach() issue #561.
 */
  /*public final void testIsGreibach() {

        GrammarNonterminalSymbol startSymbol = new GrammarNonterminalSymbol('S');
        GrammarNonterminalSymbol symbolA = new GrammarNonterminalSymbol('A');

        GrammarTerminalSymbol symbola = new GrammarTerminalSymbol('a');

        Grammar grammar = new Grammar(startSymbol);

        List<GrammarSymbol> tmp = new ArrayList<GrammarSymbol>();
        tmp.add(symbola);
        tmp.add(symbolA);

        grammar.addRule(new GrammarRule(startSymbol, tmp));

        assertTrue(GrammarUtils.isGreibach(grammar));

        GrammarNonterminalSymbol startSymbol2 = new GrammarNonterminalSymbol('S');
        GrammarNonterminalSymbol symbolA2 = new GrammarNonterminalSymbol('A');

        GrammarTerminalSymbol symbola2 = new GrammarTerminalSymbol('a');
        GrammarTerminalSymbol symbolb2 = new GrammarTerminalSymbol('b');

        Grammar grammar2 = new Grammar(startSymbol2);

        List<GrammarSymbol> tmp2 = new ArrayList<GrammarSymbol>();
        tmp.add(symbola2);
        tmp.add(symbolb2);
        tmp.add(symbolA2);

        grammar2.addRule(new GrammarRule(startSymbol2, tmp2));

        assertFalse(GrammarUtils.isGreeibach(grammar2));
    }
}*/
