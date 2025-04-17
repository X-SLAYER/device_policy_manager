import 'package:device_policy_manager/device_policy_manager.dart';
import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  const MethodChannel channel = MethodChannel('x-slayer/device_policy_manager');
  final List<MethodCall> log = [];

  setUp(() {
    TestWidgetsFlutterBinding.ensureInitialized();

    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      log.add(methodCall);
      switch (methodCall.method) {
        case 'isPermissionGranted':
          return true;
        case 'isCameraDisabled':
          return false;
        case 'enablePermission':
          return true;
        case 'removeActiveAdmin':
        case 'lockScreen':
          return null;
        default:
          throw PlatformException(code: '404', message: 'Method not found');
      }
    });

    log.clear();
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, null);
  });

  test('isPermissionGranted returns true', () async {
    final result = await DevicePolicyManager.isPermissionGranted();
    expect(result, isTrue);
  });

  test('isCameraDisabled returns false', () async {
    final result = await DevicePolicyManager.isCameraDisabled();
    expect(result, isFalse);
  });

  test('requestPermession returns true', () async {
    final result = await DevicePolicyManager.requestPermession();
    expect(result, isTrue);
  });

  test('removeActiveAdmin completes', () async {
    await expectLater(DevicePolicyManager.removeActiveAdmin(), completes);
  });

  test('lockNow completes', () async {
    await expectLater(DevicePolicyManager.lockNow(), completes);
  });

  test('isPermissionGranted handles PlatformException gracefully', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (methodCall) async {
      throw PlatformException(code: 'error', message: 'Simulated error');
    });

    final result = await DevicePolicyManager.isPermissionGranted();
    expect(result, isFalse);
  });
}
