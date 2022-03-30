import 'dart:async';
import 'dart:developer';

import 'package:flutter/services.dart';

class DevicePolicyManager {
  static const MethodChannel _channel =
      MethodChannel('x-slayer/device_policy_manager');

  static Future<bool> isPermissionGranted() async {
    try {
      return await _channel.invokeMethod<bool>('isPermissionGranted') ?? false;
    } on PlatformException catch (error) {
      log("$error");
      return Future.value(false);
    }
  }

  static Future<void> requestPermession(
      [String message = "You need to enable this"]) async {
    try {
      await _channel.invokeMethod('enablePermission', {"message": message});
    } on PlatformException catch (error) {
      log("$error");
    }
  }

  static Future<void> removeActiveAdmin() async {
    try {
      await _channel.invokeMethod('removeActiveAdmin');
    } on PlatformException catch (error) {
      log("$error");
    }
  }

  static Future<void> lockTask() async {
    try {
      await _channel.invokeMethod('lockScreen');
    } on PlatformException catch (error) {
      log("$error");
    }
  }
}
