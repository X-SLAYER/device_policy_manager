import 'package:flutter/material.dart';
import 'package:device_policy_manager/device_policy_manager.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: SafeArea(
          child: SizedBox(
            height: MediaQuery.of(context).size.height,
            width: double.infinity,
            child: Center(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  TextButton(
                    onPressed: () async {
                      await DevicePolicyManager.requestPermession();
                    },
                    child: const Text("Enable administrative"),
                  ),
                  const SizedBox(height: 20.0),
                  TextButton(
                    onPressed: () async {
                      await DevicePolicyManager.requestPermession();
                    },
                    child: const Text("Disable administrative"),
                  ),
                  const SizedBox(height: 20.0),
                  TextButton.icon(
                    onPressed: () async {
                      await DevicePolicyManager.lockTask();
                    },
                    icon: const Icon(Icons.lock),
                    label: const Text("Lock Screen"),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
