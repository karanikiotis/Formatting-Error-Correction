package jetbrains.mps.debugger.java.customViewers.plugin.plugin;

/*Generated by MPS */

import jetbrains.mps.debugger.java.api.state.proxy.ValueWrapperFactory;
import jetbrains.mps.debugger.java.api.state.proxy.ValueWrapper;
import jetbrains.mps.debugger.java.api.evaluation.proxies.IValueProxy;
import com.sun.jdi.ThreadReference;
import org.jetbrains.annotations.NotNull;
import jetbrains.mps.debugger.java.api.evaluation.EvaluationUtils;
import jetbrains.mps.debugger.java.api.evaluation.EvaluationException;
import com.sun.jdi.Value;
import java.util.List;
import jetbrains.mps.debugger.java.api.state.watchables.CustomJavaWatchable;
import jetbrains.mps.debugger.java.api.evaluation.proxies.IObjectValueProxy;
import java.util.Collections;
import java.util.ArrayList;
import jetbrains.mps.debugger.java.api.state.customViewers.CustomViewersManager;
import jetbrains.mps.debugger.java.api.evaluation.proxies.ProxyEqualsUtil;

public class MapEntry_WrapperFactory extends ValueWrapperFactory {
  public MapEntry_WrapperFactory() {
  }
  public ValueWrapper createValueWrapper(IValueProxy value, ThreadReference threadReference) {
    return new MapEntry_WrapperFactory.MapEntryWrapper(value, threadReference);
  }
  @Override
  public boolean canWrapValue(@NotNull final IValueProxy proxy) {
    return EvaluationUtils.consumeEvaluationException(new EvaluationUtils.EvaluationInvocatable<Boolean>() {
      public Boolean invoke() throws EvaluationException {
        Value value = proxy.getJDIValue();
        if (value == null) {
          return false;
        }
        if (!(EvaluationUtils.getInstance().instanceOf(value.type(), "Ljava/util/Map$Entry;", value.virtualMachine()))) {
          return false;
        }
        return true;
      }
    }, false);
  }
  @Override
  public String getWrappedType() {
    return "Ljava/util/Map$Entry;";
  }
  public static class MapEntryWrapper extends ValueWrapper {
    private final String myPresentation;
    public MapEntryWrapper(IValueProxy value, ThreadReference threadReference) {
      super(value, threadReference);
      myPresentation = getValuePresentationImpl();
    }
    protected List<CustomJavaWatchable> getSubvaluesImpl() {
      return EvaluationUtils.consumeEvaluationException(new EvaluationUtils.EvaluationInvocatable<List<CustomJavaWatchable>>() {
        public List<CustomJavaWatchable> invoke() throws EvaluationException {
          return getSubvaluesImpl((IObjectValueProxy) myValue);
        }
      }, Collections.<CustomJavaWatchable>emptyList());
    }
    protected List<CustomJavaWatchable> getSubvaluesImpl(IObjectValueProxy value) throws EvaluationException {
      List<CustomJavaWatchable> result = new ArrayList<CustomJavaWatchable>();
      IObjectValueProxy key = ((IObjectValueProxy) value.invokeMethod("getKey", "()Ljava/lang/Object;", getThreadReference()));
      IObjectValueProxy entryValue = ((IObjectValueProxy) value.invokeMethod("getValue", "()Ljava/lang/Object;", getThreadReference()));
      result.add(new jetbrains.mps.debugger.java.customViewers.plugin.plugin.Collections.MyWatchable_key(CustomViewersManager.getInstance().fromJdi(key.getJDIValue(), getThreadReference()), "key"));
      result.add(new jetbrains.mps.debugger.java.customViewers.plugin.plugin.Collections.MyWatchable_value(CustomViewersManager.getInstance().fromJdi(entryValue.getJDIValue(), getThreadReference()), "value"));
      return result;
    }
    private String getValuePresentationImpl() {
      return EvaluationUtils.consumeEvaluationException(new EvaluationUtils.EvaluationInvocatable<String>() {
        public String invoke() throws EvaluationException {
          return getValuePresentation((IObjectValueProxy) myValue);
        }
      }, super.getValuePresentation());
    }
    public String getValuePresentation() {
      return myPresentation;
    }
    protected String getValuePresentation(IObjectValueProxy value) throws EvaluationException {
      IObjectValueProxy key = ((IObjectValueProxy) value.invokeMethod("getKey", "()Ljava/lang/Object;", getThreadReference()));
      IObjectValueProxy entryValue = ((IObjectValueProxy) value.invokeMethod("getValue", "()Ljava/lang/Object;", getThreadReference()));
      return "[" + ((ProxyEqualsUtil.javaEquals(key, null) ? "null" : (String) (((IObjectValueProxy) key.invokeMethod("toString", "()Ljava/lang/String;", getThreadReference()))).getJavaValue())) + "] = " + ((ProxyEqualsUtil.javaEquals(entryValue, null) ? "null" : (String) (((IObjectValueProxy) entryValue.invokeMethod("toString", "()Ljava/lang/String;", getThreadReference()))).getJavaValue()));
    }
  }

  @Override
  public String getName() {
    return "MapEntry";
  }
}