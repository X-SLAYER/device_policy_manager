import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:device_policy_manager/device_policy_manager.dart';

void main() {
  const MethodChannel channel = MethodChannel('device_policy_manager');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await DevicePolicyManager.platformVersion, '42');
  });
}
