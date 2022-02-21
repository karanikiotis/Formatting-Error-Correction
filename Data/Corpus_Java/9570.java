package org.javers.core;

import org.javers.core.changelog.ChangeProcessor;
import org.javers.core.commit.Commit;
import org.javers.core.commit.CommitMetadata;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.PropertyChange;
import org.javers.core.json.JsonConverter;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.core.metamodel.object.GlobalId;
import org.javers.core.metamodel.property.Property;
import org.javers.core.metamodel.type.JaversType;
import org.javers.repository.jql.GlobalIdDTO;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.javers.repository.jql.ShadowScope;
import org.javers.shadow.Shadow;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Facade to JaVers instance.<br>
 * Should be constructed by {@link JaversBuilder} provided with your domain model configuration.
 * <br/><br/>
 *
 * For example, to deeply compare two objects
 * or two arbitrary complex graphs of objects, call:
 * <pre>
 * Javers javers = JaversBuilder.javers().build();
 * Diff diff = javers.compare(oldVersion, currentVersion);
 * </pre>
 *
 * @see <a href="http://javers.org/documentation"/>http://javers.org/documentation</a>
 * @author bartosz walacik
 */
public interface Javers {

    /**
     * Persists a current state of a given domain object graph
     * in JaVers repository.
     * <br/><br/>
     *
     * JaVers applies commit() to given object and all objects navigable from it.
     * You can capture a state of an arbitrary complex object graph with a single commit() call.
     *
     * @see <a href="http://javers.org/documentation/repository-examples/">http://javers.org/documentation/repository-examples</a>
     * @param author current user
     * @param currentVersion standalone object or handle to an object graph
     */
    Commit commit(String author, Object currentVersion);

    /**
     * Variant of {@link #commit(String, Object)} with commitProperties.
     * <br/>
     * You can pass arbitrary commit properties and
     * use them in JQL to search for snapshots or changes.
     *
     * @see QueryBuilder#withCommitProperty(String, String)
     * @param commitProperties for example ["channel":"web", "locale":"pl-PL"]
     */
    Commit commit(String author, Object currentVersion, Map<String, String> commitProperties);

    /**
     * Marks given object as deleted.
     * <br/><br/>
     *
     * Unlike {@link Javers#commit(String, Object)}, this method is shallow
     * and affects only given object.
     * <br/><br/>
     *
     * This method doesn't delete anything from JaVers repository.
     * It just persists 'terminal snapshot' of a given object.
     *
     * @param deleted object to be marked as deleted
     */
    Commit commitShallowDelete(String author, Object deleted);

    /**
     * Variant of {@link #commitShallowDelete(String, Object)} with commitProperties.
     * <br/>
     *
     * See {@link #commit(String, Object, Map)} for commitProperties description.
     */
    Commit commitShallowDelete(String author, Object deleted, Map<String, String> commitProperties);

    /**
     * The same like {@link #commitShallowDelete(String,Object)}
     * but deleted object is selected using globalId
     */
    Commit commitShallowDeleteById(String author, GlobalIdDTO globalId);

    /**
     * Variant of {@link #commitShallowDeleteById(String, GlobalIdDTO)} with commitProperties.
     * <br/>
     *
     * See {@link #commit(String, Object, Map)} for commitProperties description.
     */
    Commit commitShallowDeleteById(String author, GlobalIdDTO globalId, Map<String, String> commitProperties);

    /**
     * <h2>Deep compare</h2>
     *
     * JaVers core function,
     * deeply compares two arbitrary complex object graphs.
     *
     * <p>
     * To calculate a diff, just provide two versions of the
     * same Domain Object (Entity or ValueObject)
     * or handles to two versions of the same object graph.
     * <br/>
     * The handle could be a root of an Aggregate, tree root
     * or any node in an Domain Object graph from where all other nodes are navigable.
     * </p>
     *
     * <h2>Flat collection compare</h2>
     * You can also pass object collections here (List, Sets or Maps),
     * but in this case, JaVers calculates flat collection diff only.
     * Because it's impossible to determine type of raw collection items, JaVers maps them as Values
     * and compares using {@link Object#equals(Object)}. <br/>
     * So if you need to deep compare, wrap collections in some Value Objects.
     *
     * <h2>Misc</h2>
     * <code>compare()</code> function is used for ad-hoc objects comparing.
     * In order to use data auditing feature, call {@link #commit(String, Object)}.
     *
     * <p>
     * Diffs can be converted to JSON with {@link JsonConverter#toJson(Object)} or pretty-printed with {@link Diff#prettyPrint()}
     * </p>
     *
     * @see <a href="http://javers.org/documentation/diff-examples/">
     *     http://javers.org/documentation/diff-examples</a>
     */
    Diff compare(Object oldVersion, Object currentVersion);

    /**
     * Deeply compares two top-level collections.
     * <br/><br/>
     *
     * Introduced due to the lack of possibility to statically
     * determine type of collection items when two top-level collections are passed as references to
     * {@link #compare(Object, Object)}.
     * <br/><br/>
     *
     * Usage example:
     * <pre>
     * List&lt;Person&gt; oldList = ...
     * List&lt;Person&gt; newList = ...
     * Diff diff = javers.compareCollections(oldList, newList, Person.class);
     * </pre>
     *
     * @see <a href="http://javers.org/documentation/diff-examples/#compare-collections">
     *     Compare top-level collections example</a>
     */
    <T> Diff compareCollections(Collection<T> oldVersion, Collection<T> currentVersion, Class<T> itemClass);

    /**
     * Initial diff is a kind of snapshot of given domain object graph.
     * Use it alongside with {@link #compare(Object, Object)}
     */
    Diff initial(Object newDomainObject);

    /**
     * Queries JaversRepository for object Shadows. <br/>
     * Shadow is a historical version of a domain object restored from a snapshot.
     * <br/><br/>
     *
     * For example, to get latest Shadows of "bob" Person, call:
     * <pre>
     * List<Shadow> shadows = javers.findShadows( QueryBuilder.byInstanceId("bob", Person.class).limit(5).build() );
     * </pre>
     *
     * Since Shadows are instances of your domain classes,
     * you can use them directly in your application:
     *
     * <pre>
     * assert shadows.get(0).get() instanceof Person.class;
     * </pre>
     *
     * Choose between shallow or deep shadows using {@link QueryBuilder#withShadowScope(ShadowScope)},
     * default is {@link ShadowScope#SHALLOW}
     * <br/><br/>
     *
     * For more query examples, see {@link #findChanges(JqlQuery)} method.
     *
     * @return A list ordered in reverse chronological order. Empty if nothing found.
     * @param <T> type of a domain object
     * @see <a href="http://javers.org/documentation/jql-examples/">http://javers.org/documentation/jql-examples</a>
     * @since 3.2
     */
    <T> List<Shadow<T>> findShadows(JqlQuery query);

    /**
     * Queries JaversRepository for changes history (diff sequence) of a given class, object or property.<br/>
     * There are various types of changes. See {@link Change} class hierarchy.
     * <br/><br/>
     *
     * <b>Querying for Entity changes by instance Id</b><br/><br/>
     *
     * For example, to get change history of last 5 versions of "bob" Person, call:
     * <pre>
     * javers.findChanges( QueryBuilder.byInstanceId("bob", Person.class).limit(5).build() );
     * </pre>
     *
     * Last "salary" changes of "bob" Person:
     * <pre>
     * javers.findChanges( QueryBuilder.byInstanceId("bob", Person.class).andProperty("salary").build() );
     * </pre>
     *
     * <b>Querying for ValueObject changes</b><br/><br/>
     *
     * Last changes on Address ValueObject owned by "bob" Person:
     * <pre>
     * javers.findChanges( QueryBuilder.byValueObjectId("bob", Person.class, "address").build() );
     * </pre>
     *
     * Last changes on Address ValueObject owned by any Person:
     * <pre>
     * javers.findChanges( QueryBuilder.byValueObject(Person.class, "address").build() );
     * </pre>
     *
     * Last changes on nested ValueObject
     * (when Address is a ValueObject nested in PersonDetails ValueObject):
     * <pre>
     * javers.findChanges( QueryBuilder.byValueObject(Person.class, "personDetails/address").build() );
     * </pre>
     *
     * <b>Querying for any object changes by its class</b><br/><br/>
     *
     * Last changes on any object of MyClass.class:
     * <pre>
     * javers.findChanges( QueryBuilder.byClass(MyClass.class).build() );
     * </pre>
     *
     * Last "myProperty" changes on any object of MyClass.class:
     * <pre>
     * javers.findChanges( QueryBuilder.byClass(Person.class).andProperty("myProperty").build() );
     * </pre>
     *
     * @return A list ordered in reverse chronological order. Empty if nothing found.
     * @see <a href="http://javers.org/documentation/jql-examples/">http://javers.org/documentation/jql-examples</a>
     */
    List<Change> findChanges(JqlQuery query);

    /**
     * Queries JaversRepository for object Snapshots. <br/>
     * Snapshot is a historical state of a domain object captured as the property->value Map.
     * <br/><br/>
     *
     * For example, to get latest Snapshots of "bob" Person, call:
     * <pre>
     * javers.findSnapshots( QueryBuilder.byInstanceId("bob", Person.class).limit(5).build() );
     * </pre>
     *
     * For more query examples, see {@link #findChanges(JqlQuery)} method.
     * <br/>
     * Use the same JqlQuery to get changes, snapshots and shadows views.
     *
     * @return A list ordered in reverse chronological order. Empty if nothing found.
     * @see <a href="http://javers.org/documentation/jql-examples/">http://javers.org/documentation/jql-examples</a>
     */
    List<CdoSnapshot> findSnapshots(JqlQuery query);

    /**
     * Latest snapshot of given entity instance
     * or Optional#EMPTY if instance is not versioned.
     * <br/><br/>
     *
     * For example, to get last snapshot of "bob" Person, call:
     * <pre>
     * javers.getLatestSnapshot("bob", Person.class));
     * </pre>
     */
    Optional<CdoSnapshot> getLatestSnapshot(Object localId, Class entityClass);

    /**
     * If you are serializing JaVers objects like
     * {@link Commit}, {@link Change}, {@link Diff} or {@link CdoSnapshot} to JSON, use this JsonConverter.
     * <br/><br/>
     *
     * For example:
     * <pre>
     * javers.getJsonConverter().toJson(changes);
     * </pre>
     */
    JsonConverter getJsonConverter();

    /**
     * Generic purpose method for processing a changes list.
     * After iterating over given list, returns data computed by
     * {@link org.javers.core.changelog.ChangeProcessor#result()}.
     * <br/>
     * It's more convenient than iterating over changes on your own.
     * ChangeProcessor frees you from <tt>if + inctanceof</tt> boilerplate.
     *
     * <br/><br/>
     * Additional features: <br/>
     *  - when several changes in a row refers to the same Commit, {@link ChangeProcessor#onCommit(CommitMetadata)}
     *  is called only for first occurrence <br/>
     *  - similarly, when several changes in a row affects the same object, {@link ChangeProcessor#onAffectedObject(GlobalId)}
     *  is called only for first occurrence
     *
     * <br/><br/>
     * For example, to get pretty change log, call:
     * <pre>
     * List&lt;Change&gt; changes = javers.calculateDiffs(...);
     * String changeLog = javers.processChangeList(changes, new SimpleTextChangeLog());
     * System.out.println( changeLog );
     * </pre>
     *
     * @see org.javers.core.changelog.SimpleTextChangeLog
     */
    <T> T processChangeList(List<Change> changes, ChangeProcessor<T> changeProcessor);

    /**
     * Use JaversTypes, if you want to: <br/>
     * - describe your class in the context of JaVers domain model mapping, <br/>
     * - use JaVers Reflection API to conveniently access your object properties
     *  (instead of awkward java.lang.reflect API).
     *
     * <br/><br/>
     *
     * <b>Class describe example</b>.
     * You can pretty-print JaversType of your class and
     * check if mapping is correct.
     * <pre>
     * class Person {
     *     &#64;Id int id;
     *     &#64;Transient String notImportantField;
     *     String name;
     * }
     * </pre>
     *
     * Calling
     * <pre>
     * System.out.println( javers.getTypeMapping(Person.class).prettyPrint() );
     * </pre>
     *
     * prints:
     * <pre>
     * EntityType{
     *   baseType: org.javers.core.examples.Person
     *   managedProperties:
     *      Field int id; //declared in: Person
     *      Field String name; //declared in: Person
     *   idProperty: login
     * }
     * </pre>
     *
     * <b>Property access example</b>.
     * You can list object property values using {@link Property} abstraction.
     * <pre>
     * Javers javers = JaversBuilder.javers().build();
     * ManagedType jType = javers.getTypeMapping(Person.class);
     * Person person = new Person("bob", "Uncle Bob");
     *
     * System.out.println("Bob's properties:");
     * for (Property p : jType.getPropertyNames()){
     *     Object value = p.get(person);
     *     System.out.println( "property:" + p.getName() + ", value:" + value );
     * }
     * </pre>
     *
     * prints:
     * <pre>
     * Bob's properties:
     * property:login, value:bob
     * property:name, value:Uncle Bob
     * </pre>
     */
    <T extends JaversType> T getTypeMapping(Type userType);

    /**
     * Returns {@link Property} which underlies given {@link PropertyChange}
     *
     * @since 1.4.1
     */
    Property getProperty(PropertyChange propertyChange);
}