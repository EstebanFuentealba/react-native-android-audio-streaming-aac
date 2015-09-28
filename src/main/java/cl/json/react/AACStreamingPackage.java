package cl.json.react;

import java.util.*;
import android.app.Activity;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

public class AACStreamingPackage implements ReactPackage {

  private Class<?> clsActivity;
  public AACStreamingPackage(Class<?> activity) {
    super();
    this.clsActivity = activity;
  }
  @Override
  public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
    List<NativeModule> modules = new ArrayList<>();
    modules.add(new AACStreamingModule(reactContext, this.clsActivity));
    return modules;
  }
  @Override
  public List<Class<? extends JavaScriptModule>> createJSModules() {
    return Collections.emptyList();
  }

  @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
    return Arrays.<ViewManager>asList();
  }
}
