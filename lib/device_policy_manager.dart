import 'dart:async';
import 'dart:developer';

import 'package:flutter/services.dart';

class DevicePolicyManager {
  static const MethodChannel _channel =
      MethodChannel('x-slayer/device_policy_manager');

  static Future<void> requestPermession() async {
    try {
      await _channel.invokeMethod('enablePermession');
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
