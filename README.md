# device_policy_manager

A flutter plugin to query Android device Administrator (DevicePolicyManager) particulary Locking screen

for more info check [DevicePolicyManager](https://developer.android.com/reference/android/app/admin/DevicePolicyManager)

### Installation and usage ###

Add package to your pubspec:

```yaml
dependencies:
  device_policy_manager: any # or the latest version on Pub
```

Inside AndroidManifest add this to bind deviceAdmin receiver with your application

```
    ...
  <receiver android:name="device.policy.manager.DeviceAdmin" android:permission="android.permission.BIND_DEVICE_ADMIN">
      <meta-data android:name="android.app.device_admin" android:resource="@xml/policies" />
      <intent-filter android:exported="true">
          <action android:name="android.app.action.DEVICE_ADMIN_ENABLED" />
          <action android:name="android.app.action.ACTION_DEVICE_ADMIN_DISABLE_REQUESTED" />
          <action android:name="android.app.action.ACTION_DEVICE_ADMIN_DISABLED" />
      </intent-filter>
  </receiver>

<activity
  .
  .
  .
</application>

```

Create `policies.xml` inside `res/xml` and add the following code inside it:

```
<?xml version="1.0" encoding="utf-8"?>
<device-admin>
    <uses-policies>
        <force-lock />
    </uses-policies>
</device-admin>

```


### USAGE


```dart
/// Return `true` if the given administrator component is currently active (enabled) in the system.
final status = await DevicePolicyManager.isPermissionGranted();

/// request administrator permission
/// it will open the adminstartor permission page and return `true` once the permission granted.
/// An optional message providing additional explanation for why the admin is being added.
await DevicePolicyManager.requestPermession("Your app is requesting the Adminstration permission");

/// Remove administration permission from the current app.
await DevicePolicyManager.removeActiveAdmin();

/// Make the device lock immediately, as if the lock screen timeout has expired at the point of this call.
/// After this method is called, the device must be unlocked using strong authentication (PIN, pattern, or password).
await DevicePolicyManager.lockNow();

/// Determine whether or not the device's cameras have been disabled for this user.
final status = await DevicePolicyManager.isCameraDisabled();

```
