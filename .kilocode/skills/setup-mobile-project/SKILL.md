---
name: setup-mobile-project
description: Set up a new mobile project with proper architecture, dependencies, and CI configuration.
---

# Setup Mobile Project

## When to Use
- When starting a new Android, iOS, or cross-platform mobile project
- When setting up the initial architecture and project structure

## Steps

### 1. Choose Architecture
- Determine the target platform(s): Android, iOS, or cross-platform
- Choose the architecture pattern (MVVM, MVI, Clean Architecture)
- Select the UI framework:
  - **Android**: Jetpack Compose (preferred) or XML Views
  - **iOS**: SwiftUI (preferred) or UIKit
  - **Cross-platform**: Flutter, React Native, or Kotlin Multiplatform

### 2. Project Structure

#### Android (Kotlin):
```
app/
├── src/main/
│   ├── java/com/example/app/
│   │   ├── data/           # Data layer (repos, data sources, models)
│   │   │   ├── local/      # Room DB, SharedPreferences
│   │   │   ├── remote/     # Retrofit services, API models
│   │   │   └── repository/ # Repository implementations
│   │   ├── domain/         # Domain layer (use cases, domain models)
│   │   │   ├── model/      # Domain entities
│   │   │   └── usecase/    # Business logic use cases
│   │   ├── presentation/   # UI layer
│   │   │   ├── screen/     # Screen composables/activities
│   │   │   ├── component/  # Reusable UI components
│   │   │   ├── navigation/ # Navigation graph
│   │   │   └── theme/      # Theme, colors, typography
│   │   └── di/             # Dependency injection (Hilt modules)
│   └── res/
├── build.gradle.kts
└── proguard-rules.pro
```

#### Flutter:
```
lib/
├── core/               # Shared utilities, theme, constants
├── data/               # Data layer (repos, data sources)
├── domain/             # Domain layer (entities, use cases)
├── presentation/       # UI layer (pages, widgets, state)
└── main.dart
```

### 3. Core Dependencies

#### Android:
- **DI**: Hilt (Dagger) for dependency injection
- **Networking**: Retrofit + OkHttp + Kotlinx Serialization
- **Database**: Room for local persistence
- **Navigation**: Navigation Compose or Navigation Component
- **Async**: Kotlin Coroutines + Flow
- **Testing**: JUnit5, MockK, Turbine (Flow testing), Espresso/Compose Testing

#### Flutter:
- **State**: BLoC or Riverpod
- **Networking**: Dio or HTTP package
- **Database**: Drift or Hive
- **Navigation**: GoRouter
- **Testing**: flutter_test, mockito, integration_test

### 4. Configuration
- Set up build variants (debug, release, staging)
- Configure signing for release builds
- Set up ProGuard/R8 rules (Android)
- Configure code quality tools (ktlint, detekt for Kotlin; dart analyze for Flutter)
- Set minimum SDK/API versions

### 5. CI/CD Setup
- Configure build pipeline (GitHub Actions / GitLab CI)
- Add automated test execution
- Add static analysis checks
- Configure APK/IPA artifact generation

## Quality Checklist
- [ ] Architecture pattern clearly defined and documented
- [ ] Dependency injection configured
- [ ] Network layer with error handling set up
- [ ] Local database configured (if needed)
- [ ] Navigation framework in place
- [ ] Theme/design system setup
- [ ] Debug and release build variants working
- [ ] Basic CI pipeline configured
- [ ] README updated with setup instructions
