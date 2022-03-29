package device.policy.manager;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;


public class DevicePolicyManagerPlugin implements FlutterPlugin, MethodCallHandler {

    private MethodChannel channel;
    private static Context appContext;
    private static final String CHANNEL_TAG = "x.slayer/device_policy_manager";
    static final int RESULT_ENABLE = -11;
    DevicePolicyManager deviceManger;
    ComponentName compName;


    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "device_policy_manager");
        channel.setMethodCallHandler(this);
        context = flutterPluginBinding.getApplicationContext();
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        compName = new ComponentName(appContext, DeviceAdmin.class);
        deviceManger = (DevicePolicyManager) appContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (call.method.equals("enable")) {
            String message = call.argument("package_name").toString();
            enableAccess(message);
        } else if (call.method.equals("disable")) {
            deviceManger.removeActiveAdmin(compName);
        } else if (call.method.equals("lockScreen")) {
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                deviceManger.lockNow();
                result.success(true);
            } else {
                result.error("-1", "You need to enable the Admin Device Features", null);
            }
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == RESULT_ENABLE) {
                    Log.d("PERMISSION", "You have enabled the Admin Device features");
                } else {
                    Log.d("PERMISSION", "Problem to enable the Admin Device features");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void enableAccess(String message) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, message);
        startActivityForResult(intent, RESULT_ENABLE);
    }
}
