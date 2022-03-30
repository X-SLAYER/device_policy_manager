package device.policy.manager;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class DevicePolicyManagerPlugin implements FlutterPlugin, ActivityAware, MethodCallHandler {

    private MethodChannel channel;
    private Context appContext;
    private Activity mActivity;
    private final String CHANNEL_TAG = "x-slayer/device_policy_manager";
    final int RESULT_ENABLE = -11;
    DevicePolicyManager deviceManger;
    ComponentName compName;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        appContext = flutterPluginBinding.getApplicationContext();
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), CHANNEL_TAG);
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        compName = new ComponentName(appContext, DeviceAdmin.class);
        deviceManger = (DevicePolicyManager) appContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (call.method.equals("enablePermission")) {
            try {
                String message = call.argument("message").toString();
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, message);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appContext.startActivity(intent);
            } catch (Exception e) {
                Log.d("TAG", e.getMessage());
            }
        } else if (call.method.equals("removeActiveAdmin")) {
            deviceManger.removeActiveAdmin(compName);
        } else if (call.method.equals("isPermissionGranted")) {
            result.success(deviceManger.isAdminActive(compName));
        } else if (call.method.equals("lockScreen")) {
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                deviceManger.lockNow();
                result.success(true);
            } else {
                result.error("ERROR", "You need to enable the Admin Device Features", null);
            }
        }
    }


    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding activityPluginBinding) {
        this.mActivity = activityPluginBinding.getActivity();
        Log.i("TAG", "Attached to Activity");
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding activityPluginBinding) {
        onAttachedToActivity(activityPluginBinding);
    }

    @Override
    public void onDetachedFromActivity() {
    }
}
