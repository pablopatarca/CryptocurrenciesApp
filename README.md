# Crypto App 🤑
App to track cryptocurrencies values

# Architecture

The architecture is built around
[Android Architecture Components](https://developer.android.com/topic/libraries/architecture/)
and follows the recommendations laid out in the
[Guide to App Architecture](https://developer.android.com/jetpack/docs/guide). 

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

## Data layer
The **Repository** follows the **Facate Structural Pattern** to handle data operations. 
Moust of the data is provided mainly from two **Data Sources**; stored with 
[Room](https://developer.android.com/jetpack/androidx/releases/room) and from the network using
Retrofit. The repository modules are responsible for handling all data operations 
and abstracting the data sources from the rest of the app.

## Domain layer
A lightweight domain layer sits between the data layer
and the presentation layer, and handles discrete pieces of business logic off
the UI thread. See the `.\*UseCase.kt` files under `/domain`.
