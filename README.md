# Crypto App 🤑
Crypto is an Android app to track **crypto assets values** made with **MVVM** Arch pattern, following Clean Architecture and SOLID principles, Oriented Objects and Reactive Programming paradigms and supported by using the following libraries:
- Jetpack Compose, 
- Dagger Hilt, 
- Room,
- Retrofit,
- Kotlin Flow,
- AnyChart,
- Coil,
and more...

<p align="center">
<img src="https://github.com/pablopatarca/CryptocurrenciesApp/blob/main/media_sources/demo_1.gif" width="300"/>
</p>
<p align="center">Crypto app.</p>

# Architecture

The architecture is built around
[Android Architecture Components](https://developer.android.com/topic/libraries/architecture/)
and follows the **MVVM** pattern and the recommendations laid out in the
[Guide to App Architecture](https://developer.android.com/jetpack/docs/guide).

<p align="center">
<img src="https://developer.android.com/topic/libraries/architecture/images/mad-arch-overview.png" width="300"/>
</p>
<p align="center">Diagram of a typical app architecture.</p>

## Presentation layer
Logic is kept away from Activities and Fragments and moved to
[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel).
Data is observed using
[Kotlin Flows](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
and the [Data Binding Library](https://developer.android.com/topic/libraries/data-binding/)
binds UI components in layouts to the app's data sources.
The [Navigation component](https://developer.android.com/guide/navigation) is used
to implement navigation in the app, handling Composable Screens or Fragment transactions 
and providing a consistent user experience.

## Domain layer
A lightweight domain layer sits between the data layer
and the presentation layer, and handles discrete pieces of business logic off
the UI thread. See the `.\*UseCase.kt` files under `/domain`.

## Data layer
The **Repository** follows the **Facate Structural Pattern** to handle data operations. 
Moust of the data is provided mainly from two **Data Sources**; stored with 
[Room](https://developer.android.com/jetpack/androidx/releases/room) and from the network using
Retrofit. The repository modules are responsible for handling all data operations 
and abstracting the data sources from the rest of the app.

## Data Sources
All the information is provided from [coincap.io](https://api.coincap.io/) API 
as well as the [logo images](https://assets.coincap.io/assets/icons/btc@2x.png).
