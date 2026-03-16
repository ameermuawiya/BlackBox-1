![BlackBox Banner](assets/banner.png)

<div align="center">

# ⚙️ Unreal Engine BlackBox

> *"The only people who have anything to fear from free software are those whose products are worth even less."*

![Language](https://img.shields.io/badge/language-java-brightgreen.svg)
![Repository Size](https://img.shields.io/github/repo-size/MrCode403/BlackBox)
[![Total Downloads](https://img.shields.io/github/downloads/MrCode403/BlackBox/total)](https://github.com/MrCode403/BlackBox/releases)
![Commit Activity](https://img.shields.io/github/commit-activity/m/MrCode403/BlackBox)
<img src="https://img.shields.io/github/v/release/MrCode403/BlackBox?include_prereleases&label=latest%20release" alt="Latest Release">

**BlackBox** is a powerful virtual engine for Android that enables cloning and running virtual applications — no installation required. It provides complete control over virtualized app environments, giving developers full flexibility to customize behavior at runtime.

</div>

---

## 🆕 What's New

- 🔧 **Gradle 9 Migration** — Project has been fully migrated to Gradle 9 for improved build performance and modern toolchain support.
- 📦 **Libraries Upgraded** — All dependencies have been updated to their latest stable versions.
- 🛠️ **C Language & AIDL Fixes** — Numerous C language and AIDL-related errors that were incompatible with Gradle 9 have been identified and resolved.

---

## 📱 Compatibility

| Android Version | Status |
|---|---|
| Android 5.0 – 12.0 | ✅ Supported |
| Android 4.x | ❌ Not planned |
| Future versions | 🔄 Will be tracked |

> **💡 Tip:** For maximum compatibility, consider downgrading `targetSdkVersion` to **28 or below**.

> ⚠️ **Notice:** Stability has not been extensively tested. This project is intended **for learning and research purposes only**. Please do not use it for any other purpose.

---

## 🚀 Getting Started

### Step 1 — Initialize in Application

Add the following initialization code inside your `Application` class:

```java
@Override
protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    try {
        BlackBoxCore.get().doAttachBaseContext(base, new ClientConfiguration() {
            @Override
            public String getHostPackageName() {
                return base.getPackageName();
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
}

@Override
public void onCreate() {
    super.onCreate();
    BlackBoxCore.get().doCreate();
}
```

---

### Step 2 — Install an Application into BlackBox

```java
// Install by package name (for already-installed apps)
BlackBoxCore.get().installPackageAsUser("com.tencent.mm", userId);

// Install from APK file path (for uninstalled apps)
BlackBoxCore.get().installPackageAsUser(new File("/sdcard/com.tencent.mm.apk"), userId);
```

---

### Step 3 — Launch an Application inside BlackBox

```java
BlackBoxCore.get().launchApk("com.tencent.mm", userId);
```

---

### 🪟 Multi-Window Support

Run multiple applications simultaneously with full multi-window support:

<img src="assets/multiw.gif" width="50%">

---

## 🔌 API Reference

### Retrieve Installed Applications

```java
// Use the same flags as standard Android package queries
BlackBoxCore.get().getInstalledApplications(flags, userId);
BlackBoxCore.get().getInstalledPackages(flags, userId);
```

### Retrieve User Information

```java
List<BUserInfo> users = BlackBoxCore.get().getUsers();
```

> For a full list of available operations, explore the `BlackBoxCore` class — method names are descriptive and self-explanatory.

---

### 🧩 Xposed Framework Support

- ✅ Full support for Xposed modules
- 🛡️ Detection resistance — tools such as [Xposed Checker](https://www.coolapk.com/apk/190247) and [XposedDetector](https://github.com/vvb2060/XposedDetector) are unable to detect the framework

---

## 🏗️ Project Architecture

The project is organized into **two core modules**:

| Module | Responsibility |
|---|---|
| `app` | User interface and interaction layer |
| `Bcore` | Core engine — handles all virtual environment scheduling and management |

---

## 🤝 Contributing

Contributions are welcome! Feel free to submit a Pull Request directly.

**PR Guidelines:**
1. Explanations may be written in either **Chinese or English**, but must clearly describe the problem or change.
2. Please adhere to the existing **code style and project design conventions**.

📖 New to GitHub PRs? Check out this guide: [How to Submit Your First Pull Request on GitHub](https://chinese.freecodecamp.org/news/how-to-make-your-first-pull-request-on-github/)

---

## 🗺️ Roadmap

- [ ] 🔄 Virtualization of additional Service APIs *(many currently delegate to system APIs)*
- [ ] 🧰 Expanded developer interfaces *(virtual GPS, app injection, and more)*

---

## ❤️ Support the Project

BlackBox is free and open source. If it has been useful to you, consider supporting the author's work:

| Currency | Address |
|---|---|
| **BTC** | `3FCo9QtaSbGMhmZYzvL4XUoJUUxZeSdha4` |
| **USDT (TRC20)** | `TDzBj9eV1Cdmmj9xd5Y1YLsQqC8zVgi7yd` |

---

## 🙏 Acknowledgements

This project builds upon the outstanding work of the following open source projects:

- [VirtualApp](https://github.com/asLody/VirtualApp)
- [VirtualAPK](https://github.com/didi/VirtualAPK)
- [BlackReflection](https://github.com/CodingGay/BlackReflection)
- [FreeReflection](https://github.com/tiann/FreeReflection)
- [Pine](https://github.com/canyie/pine)

---

## 📄 License

```
Copyright 2022 BlackBox

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

<div align="center">
  <sub>Built with ❤️ by the BlackBox community</sub>
</div>
