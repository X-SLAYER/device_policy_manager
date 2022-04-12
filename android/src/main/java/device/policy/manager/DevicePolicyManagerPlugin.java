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
import io.flutter.plugin.common.PluginRegistry;

public class DevicePolicyManagerPlugin
        implements FlutterPlugin, ActivityAware, MethodCallHandler, PluginRegistry.ActivityResultListener {

    private final String CHANNEL_TAG = "x-slayer/device_policy_manager";

    private MethodChannel channel;
    private Context appContext;
    private Activity mActivity;
    private Result pendingResult;
    final int REQUEST_CODE_FOR_DEVICE_POLICY_MANAGER = 2999;
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
        pendingResult = result;

        if (call.method.equals("enablePermission")) {
            String message = call.argument("message").toString();
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, message);
            mActivity.startActivityForResult(intent, REQUEST_CODE_FOR_DEVICE_POLICY_MANAGER);
        } else if (call.method.equals("removeActiveAdmin")) {
            deviceManger.removeActiveAdmin(compName);
        } else if (call.method.equals("isPermissionGranted")) {
            result.success(deviceManger.isAdminActive(compName));
        } else if (call.method.equals("isCameraDisabled")) {
            result.success(deviceManger.getCameraDisabled(compName));
        } else if (call.method.equals("lockScreen")) {
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                deviceManger.lockNow();
                result.success(null);
            } else {
                result.error("ERROR", "You need to enable the Admin Device Features", null);
            }
        } else if (call.method.equals("enableCamera")) {
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                deviceManger.setCameraDisabled(compName, false);
                result.success(null);
            } else {
                result.error("ERROR", "You need to enable the Admin Device Features", null);
            }
        } else if (call.method.equals("disableCamera")) {
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                deviceManger.setCameraDisabled(compName, true);
                result.success(null);
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
        activityPluginBinding.addActivityResultListener(this);
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding activityPluginBinding) {
        onAttachedToActivity(activityPluginBinding);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        this.mActivity = null;
    }

    @Override
    public void onDetachedFromActivity() {
        this.mActivity = null;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("A9WA", resultCode + " : RequestCode : " + requestCode);
        if (requestCode == REQUEST_CODE_FOR_DEVICE_POLICY_MANAGER) {
            if (resultCode == Activity.RESULT_OK) {
                pendingResult.success(true);
            } else {
                pendingResult.success(false);
            }
            return true;
        }
        return false;
    }
}
