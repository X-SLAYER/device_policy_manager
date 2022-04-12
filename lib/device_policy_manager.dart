import 'dart:async';
import 'dart:developer';

import 'package:flutter/services.dart';

class DevicePolicyManager {
  static const MethodChannel _channel =
      MethodChannel('x-slayer/device_policy_manager');

  /// Return `true` if the given administrator component is currently active (enabled) in the system.
  /// https://developer.android.com/reference/android/app/admin/DevicePolicyManager#isAdminActive(android.content.ComponentName)
  static Future<bool> isPermissionGranted() async {
    try {
      return await _channel.invokeMethod<bool>('isPermissionGranted') ?? false;
    } on PlatformException catch (error) {
      log("$error");
      return Future.value(false);
    }
  }

  /// Determine whether or not the device's cameras have been disabled for this user.
  /// https://developer.android.com/reference/android/app/admin/DevicePolicyManager#getCameraDisabled(android.content.ComponentName)
  static Future<bool> isCameraDisabled() async {
    try {
      return await _channel.invokeMethod<bool>('isCameraDisabled') ?? false;
    } on PlatformException catch (error) {
      log("$error");
      return Future.value(false);
    }
  }

  /// request administrator permission
  /// it will open the adminstartor permission page and return `true` once the permission granted.
  /// An optional message providing additional explanation for why the admin is being added.
  static Future<bool> requestPermession(
      [String message =
          "This app is requesting adminstrator permission"]) async {
    try {
      return await _channel
          .invokeMethod('enablePermission', {"message": message});
    } on PlatformException catch (error) {
      log("Error: $error");
      rethrow;
    }
  }

  /// Remove a current administration component.
  /// This can only be called by the application that owns the administration component.
  /// https://developer.android.com/reference/android/app/admin/DevicePolicyManager#removeActiveAdmin(android.content.ComponentName)
  static Future<void> removeActiveAdmin() async {
    try {
      await _channel.invokeMethod('removeActiveAdmin');
    } on PlatformException catch (error) {
      log("$error");
      rethrow;
    }
  }

  /// Make the device lock immediately, as if the lock screen timeout has expired at the point of this call.
  ///
  /// This method secures the device in response to an urgent situation, such as a lost or stolen device.
  /// After this method is called, the device must be unlocked using strong authentication (PIN, pattern, or password).
  /// This API is intended for use only by device admins
  /// https://developer.android.com/reference/android/app/admin/DevicePolicyManager#lockNow()
  static Future<void> lockNow() async {
    try {
      await _channel.invokeMethod('lockScreen');
    } on PlatformException catch (error) {
      log("$error");
      rethrow;
    }
  }
}
