/*
 * Copyright 2003-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrains.mps.lang.typesystem.runtime;

import jetbrains.mps.newTypesystem.rules.DoubleTermRules;
import jetbrains.mps.languageScope.LanguageScope;
import jetbrains.mps.util.Pair;
import org.jetbrains.mps.openapi.language.SAbstractConcept;
import org.jetbrains.mps.openapi.model.SNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 *   Synchronized.
 */
public class DoubleRuleSet<T extends IApplicableTo2Concepts> {
  private static final String TYPESYSTEM_SUFFIX = ".typesystem";
  ConcurrentMap<Pair<SAbstractConcept, SAbstractConcept>, Set<T>> myRules = new ConcurrentHashMap<Pair<SAbstractConcept, SAbstractConcept>, /* synchronized */ Set<T>>();

  private DoubleTermRules<T> myDoubleTermRules = new DoubleTermRules<T>() {

    @Override
    protected Iterable<T> allForConceptPair(SAbstractConcept leftConcept, SAbstractConcept rightConcept, LanguageScope langScope) {
      return getAllApplicableTo(leftConcept, rightConcept, langScope);
    }
  };

  public void addRuleSetItem(Set<T> rules) {
    for (T rule : rules) {
      SAbstractConcept concept1 = rule.getApplicableConcept1();
      SAbstractConcept concept2 = rule.getApplicableConcept2();
      Pair<SAbstractConcept, SAbstractConcept> pair = new Pair<SAbstractConcept, SAbstractConcept>(concept1, concept2);
      Set<T> existingRules = myRules.get(pair);
      while (existingRules == null) {
        myRules.putIfAbsent(pair, Collections.synchronizedSet(new HashSet<T>(1)));
        existingRules = myRules.get(pair);
      }
      existingRules.add(rule);
    }
    myDoubleTermRules.purgeCache();
  }

  public Set<T> getRules(SNode leftTerm, SNode righTerm) {
    return myDoubleTermRules.lookupRules(leftTerm, righTerm);
  }

  private Iterable<T> getAllApplicableTo(SAbstractConcept leftConcept, SAbstractConcept rightConcept, LanguageScope scope) {
    Pair<SAbstractConcept, SAbstractConcept> conceptPair = new Pair<SAbstractConcept, SAbstractConcept>(leftConcept, rightConcept);
    if (!myRules.containsKey(conceptPair)) return Collections.emptyList();

    List<T> result = new ArrayList<T>(4);
    Set<T> rules = myRules.get(conceptPair);
    synchronized (rules) {
      for (T rule : rules) {
        if (scope.containsNamespace(getNamespace(rule))) {
          result.add(rule);
        }
      }
    }
    return Collections.unmodifiableList(result);
  }

  private String getNamespace(T rule) {
    String pkg = rule.getClass().getPackage().getName();
    if (pkg.endsWith(TYPESYSTEM_SUFFIX)) {
      return pkg.substring(0, pkg.length() - TYPESYSTEM_SUFFIX.length());
    }
    return pkg;
  }

  public void clear() {
    myRules.clear();
    myDoubleTermRules.purgeCache();
  }
}
