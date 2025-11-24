This is a Kotlin Multiplatform project targeting Android, iOS.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform
  applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the
      folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
      Similarly, if you want to edit the Desktop (JVM) specific part,
      the [jvmMain](./composeApp/src/jvmMain/kotlin)
      folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose
  Multiplatform, you need this entry point for your iOS app. This is also where you should add
  SwiftUI code for your project.

## Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run
widget in your IDE’s toolbar or build it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

## Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run
widget in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from
there.

## Add API keys

To add your own API keys to this project you have to do the following depending on the platform.

**Android**

1. Create a file named `secret.properties` in the root folder of the project.
2. Inside this file, define the key as a property -> WEATHER_API_KEY="YOUR_API_KEY_HERE"
3. A visual guide can be found [here](./androidApiKeys.png).

**iOS**

1. Create a file named `Secrets.xcconfig` in [this](./iosApp/Configuration) folder.
2. Inside this file, define the key as a property -> WEATHER_API_KEY="YOUR_API_KEY_HERE"
3. A visual guide can be found [here](./iOSApiKeys.png).

## Project Architecture

This document provides a high-level overview of the project's architecture and key design decisions.
For a more detailed, chronological log of the development process and the reasoning behind specific
changes, please refer to the Git commit history.

The decision was made to use Kotlin Multiplatform, as the benefit of maintaining a single codebase
for both Android and iOS outweighs the added architectural complexity.

It is important to note two specific trade-offs resulting from the multiplatform architecture and
the modularization of features:

- **UI Testing**: Testing the Compose Multiplatform UI is not currently implemented, as the
  configuration requires an androidTarget which the feature modules do not have.

- **Jetpack Compose Previews**: The standard implementation of Compose Previews via
  debugImplementation(compose.uiTooling) is not possible on a per-module basis.

An attempt was made to centralize previews in the composeApp module. While this was partially
successful, previews that rely on Res.string resources fail to render. Please, have a look at:
[Previews](./composeApp/src/commonMain/kotlin/com/something/volkswagentechtask/previews)

The application is structured around a central feature module that encapsulates all weather-related
functionality. This approach decouples the feature and is comprised of six submodules:

- [database](./feature-weather/database/src)
- [networking](./feature-weather/networking/src)
- [datastore](./feature-weather/datastore/src)
- [core](./feature-weather/core/src)
- [domain](./feature-weather/domain/src)
- [weatherApp](./feature-weather/weatherApp/src)

The primary purpose of structuring the project into feature modules is to support scalable
development in a large codebase. This approach allows different teams to work independently on their
respective modules, leading to significant benefits:

- **Faster Build Times**: Gradle can cache unchanged modules, speeding up the overall build process.

- **Reduced Merge Conflicts**: Teams working in isolated modules are less likely to create
  conflicting code changes.

- **Improved Modularity**: Each feature is self-contained, making the codebase easier to understand
  and maintain.

- **Simplified Testing**: Isolating features allows for more focused and straightforward unit and
  integration testing.

- **Decoupled Dependencies**: Changes to dependencies or API configurations within one module do not
  affect others, ensuring stability across the application.

### database (Android/iOS)

The database serves as the single source of truth for all weather data. It powers both the daily
forecast list and the detailed forecast views. It consists of a single table that stores an 8-day
weather forecast. The schema for each daily entry includes the following fields:

- id -> database identifier
- dt -> date of the forecast
- sunrise -> Sunrise time, Unix, UTC.
- sunset -> Sunset time, Unix, UTC.
- summary -> Human-readable description of the weather conditions for the day
- minTemp -> Min daily temperature
- maxTemp -> Max daily temperature
- windSpeed -> Wind speed, metre/sec
- windDeg -> Wind gust, metre/sec
- windGust -> Wind direction, degrees (meteorological)

### networking (Android/iOS)

The networking module is designed for both immediate functionality and future scalability. It
fetches and comprehensively parses the entire server payload, rather than just the currently used
fields. This design anticipates future feature requirements. A dedicated mapper then acts as an
intermediary, filtering the complete dataset down to the specific fields needed by the database,
creating a clean separation between the raw server data and the application's internal data model.

### datastore (Android/iOS)

This module implements a simple key-value storage mechanism using Jetpack DataStore. It is used to
persist a timestamp that tracks the age of the cached weather data, enabling the cache-first
mechanism described in the core module.

### core

The core module is the cornerstone of the data layer, responsible for implementing the application's
cache-first strategy. It intelligently combines the functionality of the database, networking, and
datastore modules to ensure that the UI always has access to the most relevant data. It manages the
data lifecycle by checking cache validity, triggering network fetches for expired data, and updating
the database. The reactive nature of Kotlin Flow ensures that these data updates are seamlessly and
automatically propagated to the presentation layer.

### domain

The domain module serves as the architectural core of the application, containing all business logic
and abstracting data operations. Following Clean Architecture principles, it inverts the flow of
dependencies, ensuring that the data layer (core) adapts to the contracts defined here.This design
enforces a clean separation of concerns, as the presentation layer (weatherApp) is completely
decoupled from the data implementation details, interacting only with the domain's public
interfaces. The module itself maintains maximum independence, depending only on the dependency
injection framework (Koin) and testing tools.

### weatherAPP

The weatherApp module is the final layer in this architecture, dedicated entirely to presentation.
It communicates with the domain layer to access all application functionality, effectively
decoupling the UI from the underlying data sources (core module). This module's scope is limited to
rendering the UI for the weather feature and applying view-specific data formatting. This separation
is further enforced by screen-level mappers, which adapt data models for display, ensuring the UI
remains a simple and direct representation of the application's state.

The presentation layer is structured using the Model-View-Intent (MVI) pattern. This choice was made
to leverage MVI's strengths in creating predictable and maintainable UI code. Its core benefit is a
unidirectional data flow, where state changes occur in a strict, cyclical process. This creates a
single, immutable source of truth for the UI's state, which significantly simplifies debugging,
enhances testability, and prevents common state-related bugs like race conditions.

The main differences between MVI and MVVM are the following:

1. State → MVI uses a Single Immutable State object representing the entire screen
   whereas MVVM can have partial and immutable state objects with multiple LiveData
   or StateFlows for various UI elements
2. Data Flow → MVI has an unidirectional data flow Intent → Model → State → View
   whereas MVVM can have bidirectional, View observes ViewModel and ViewModel
   can expose callbacks for View updates.
3. Events → MVI has a finite number of events defined, and as such, can exhaustively
   be processed and are easy to see all of them in one place. MVVM handles events via
   functions or LiveData events in ViewModel.
4. Reducer → MVI has a “reducer” function that receives an Intent from the UI and,
   based on the event received and the current UiState, generates an new UiState
   state = reducer(intent, state)
5. Error Handling → MVI usually models errors explicitly in

### composeAPP

The composeApp module acts as the primary application shell and the integration point for all
feature modules. While individual features like weatherApp contain their own UI and presentation
logic, this module is responsible for higher-level application concerns:

- Navigation: It defines the navigation graph, managing how the user moves between different screens
  and features within the application.
- Feature Integration: It serves as the place where multiple, otherwise independent, feature modules
  are brought together to form a cohesive application.
- Shared Application Logic: It can contain app-wide logic or UI components (like a main toolbar or
  bottom navigation bar) that are common across all features.

In essence, it is the module that transforms a collection of features into a complete, navigable
application.

### di-qualifiers

The di-qualifiers module serves a very specific but important purpose: it contains the dependency
injection qualifiers used throughout the project.

In a Koin-based dependency injection setup, qualifiers are used to distinguish between different
implementations of the same interface. For example, if you had multiple String providers (like an
API key and a base URL), you would use qualifiers to tell Koin which one to inject.

By placing these qualifiers in their own dedicated module, they can be easily shared and accessed by
any other module in the project that needs them, without creating unwanted dependencies or circular
references. This keeps the DI setup clean, organized, and scalable.

## Libraries

### Ktor VS OkHttp

Given that this is a Kotlin Multiplatform project, Ktor was the clear choice. It allows us to define
the networking logic once and share it across both Android and iOS, which aligns perfectly with the
project's goal of maximizing shared code. Ktor can even use OkHttp as its underlying engine on
Android, allowing us to leverage OkHttp's reliability while still writing platform-agnostic code.

Ktor is a networking framework from JetBrains, designed from the ground up for Kotlin and Kotlin
Multiplatform. Its key advantages are:

- Multiplatform by Design: It allows sharing the exact same networking code across Android, iOS, and
  other platforms, which is its main advantage in a KMP context.
- Coroutine-First: It is built on Kotlin coroutines, offering a clean, modern API for handling
  asynchronous operations.
- Modular and Flexible: Ktor uses a plugin system, allowing you to include only the features you
  need, such as serialization, logging, or authentication.

### Koin VS Dagger-Hilt

Dagger-Hilt is Google's recommended DI library for Android. Its main strength lies in compile-time
safety—it generates code during the build process, which catches dependency errors before the app
even runs. This makes it robust and highly performant at runtime. However, its significant
limitation is that it's built on Java annotation processing and is fundamentally incompatible with
Kotlin Multiplatform projects.

Koin is a lightweight, pragmatic DI framework written purely in Kotlin. Its key features are its
simplicity and a gentle learning curve, using a clear Domain-Specific Language (DSL) to declare
dependencies. Unlike Hilt, Koin resolves dependencies at runtime, which means errors might only be
discovered when a specific screen or feature is accessed. While this is a trade-off, its most
significant advantage is its first-class support for Kotlin Multiplatform.

Due to the Multiplatform nature of this project, the ability to define a single, shared dependency
graph in the common code for both Android and iOS is essential for a true KMP architecture.

## Future Enhancements

While the current implementation provides a solid architectural foundation, the following items are
key priorities for the continued development of this project:

- **UI Theming and Dimensions**: Refine and standardize dimensions, colors, and typography across
  the application to create a more polished user interface. Sorry for not handling dimensions and
  theming properly but I prioritized other aspects of this technical task.
- **Graceful Error Handling**: Improve the user experience during network failures. Instead of
  showing an error screen, the plan is to continue displaying stale data from the cache while
  notifying the user with a non-intrusive snackbar that the data may not be up-to-date.
- **Pull-to-Refresh**: Implement a pull-to-refresh mechanism to give users manual control over data
  fetching, allowing them to force an update even if the cache is still valid.
- **Expanded Unit Test Coverage**: Increase test coverage by adding comprehensive unit tests for all
  mappers and data formatters throughout the application.