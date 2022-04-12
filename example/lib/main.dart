import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:device_policy_manager/device_policy_manager.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(
    const MaterialApp(
      home: MyApp(),
    ),
  );
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: SizedBox(
          width: double.infinity,
          child: Center(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                TextButton(
                  onPressed: () async {
                    await DevicePolicyManager.requestPermession(
                        "Your app is requesting the Adminstration permission");
                  },
                  child: const Text("Enable administrative"),
                ),
                const SizedBox(height: 20.0),
                TextButton(
                  onPressed: () async {
                    await DevicePolicyManager.removeActiveAdmin();
                  },
                  child: const Text("Disable administrative"),
                ),
                const SizedBox(height: 20.0),
                TextButton(
                  onPressed: () async {
                    final res = await DevicePolicyManager.isPermissionGranted();
                    log("$res");
                  },
                  child: const Text("Check permission"),
                ),
                const SizedBox(height: 20.0),
                TextButton.icon(
                  onPressed: () async {
                    await DevicePolicyManager.lockNow();
                  },
                  icon: const Icon(Icons.lock),
                  label: const Text("Lock Screen"),
                ),
                const SizedBox(height: 20.0),
                TextButton(
                  onPressed: () async {
                    final res = await DevicePolicyManager.isCameraDisabled();
                    log("Is camera disabled: $res");
                  },
                  child: const Text("Check Camera is disabled ?"),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
