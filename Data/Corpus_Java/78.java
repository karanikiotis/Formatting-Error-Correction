package pl.edu.amu.wmi.daut.re;

import pl.edu.amu.wmi.daut.base.AutomataOperations;
import pl.edu.amu.wmi.daut.base.AutomatonSpecification;
import java.util.List;

/**
 * Klasa reprezentująca niejawny, dwuargumentowy operator konkatenacji.
 */
public class ConcatenationOperator extends BinaryRegexpOperator {

    /**
     * Konstruktor domyslny.
     */
    public ConcatenationOperator() { }

    @Override
    public final AutomatonSpecification createAutomatonFromTwoAutomata(
            AutomatonSpecification leftSubautomaton,
            AutomatonSpecification rightSubautomaton) {
        return AutomataOperations.concatenation(leftSubautomaton, rightSubautomaton);
    }

    /**
     * Fabryka operatora.
     */
    public static class Factory extends BinaryRegexpOperatorFactory {

        @Override
        public int numberOfParams() {
            return 0;
        }

        protected RegexpOperator doCreateOperator(List<String> params) {
            return new ConcatenationOperator();
        }
    }

    /**
     * Metoda toString().
     */
    @Override
    public String toString() {
        return "CONCATENATION";
    }
}
