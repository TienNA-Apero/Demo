# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

DemoConductor is an Android application built with **Kotlin** and **Jetpack Compose**, following **MVI (Model-View-Intent)** pattern and **Clean Architecture** principles.

## Architecture Principles

### Clean Architecture Layers

```
app/src/main/java/com/example/democonductor/
├── domain/              # Business logic layer (pure Kotlin, no Android dependencies)
│   ├── model/          # Domain entities/models
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Use cases (business logic)
├── data/               # Data layer
│   ├── repository/     # Repository implementations
│   ├── local/          # Local data sources (Room, DataStore, SharedPreferences)
│   ├── remote/         # Remote data sources (Retrofit, API)
│   └── mapper/         # Data to Domain mappers
├── presentation/       # UI layer (MVI pattern)
│   ├── auth/           # Feature: Authentication
│   │   ├── login/
│   │   │   ├── LoginScreen.kt
│   │   │   ├── LoginViewModel.kt
│   │   │   ├── LoginIntent.kt
│   │   │   ├── LoginState.kt
│   │   │   └── LoginEvent.kt
│   │   └── logout/
│   └── common/         # Shared UI components
├── di/                 # Dependency Injection (Hilt/Koin)
└── ui/                 # Design System
    ├── theme/          # Theme, Colors, Typography
    └── components/     # Reusable UI components
```

### MVI Pattern Structure

For each feature (e.g., Login, Logout), implement:

1. **State** - Single immutable data class representing UI state
```kotlin
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)
```

2. **Intent** - User actions/intentions
```kotlin
sealed interface LoginIntent {
    data class EmailChanged(val email: String) : LoginIntent
    data class PasswordChanged(val password: String) : LoginIntent
    object LoginClicked : LoginIntent
}
```

3. **Event** - One-time events (navigation, toast messages)
```kotlin
sealed interface LoginEvent {
    object NavigateToHome : LoginEvent
    data class ShowError(val message: String) : LoginEvent
}
```

4. **ViewModel** - State management with MVI
```kotlin
class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _event = Channel<LoginEvent>()
    val event = _event.receiveAsFlow()

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> updateEmail(intent.email)
            is LoginIntent.PasswordChanged -> updatePassword(intent.password)
            LoginIntent.LoginClicked -> login()
        }
    }
}
```

5. **Screen (Composable)** - UI that observes state and emits intents
```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is LoginEvent.NavigateToHome -> navController.navigate("home")
                is LoginEvent.ShowError -> showSnackbar(event.message)
            }
        }
    }

    LoginContent(
        state = state,
        onIntent = viewModel::handleIntent
    )
}
```

## Tech Stack

### Core
- **Kotlin** - Primary language
- **Jetpack Compose** - Declarative UI
- **Material 3** - Design system
- **Kotlin Coroutines & Flow** - Asynchronous programming

### Architecture & DI
- **Hilt** (recommended) or **Koin** - Dependency injection
- **ViewModel** - State management
- **Navigation Compose** - Screen navigation

### Data & Storage
- **Room** - Local database
- **DataStore** - Key-value storage (for auth tokens, preferences)
- **Retrofit** - REST API client
- **Kotlinx Serialization** or **Moshi** - JSON parsing

### Testing
- **JUnit 4** - Unit testing
- **Mockk** - Mocking framework
- **Turbine** - Flow testing
- **Compose UI Test** - UI testing

## Login/Logout Feature Implementation

### Step 1: Domain Layer (Pure Kotlin)

**domain/model/User.kt**
```kotlin
data class User(
    val id: String,
    val email: String,
    val name: String
)
```

**domain/repository/AuthRepository.kt**
```kotlin
interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
    fun isLoggedIn(): Flow<Boolean>
    fun getCurrentUser(): Flow<User?>
}
```

**domain/usecase/LoginUseCase.kt**
```kotlin
class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        // Business logic validation
        if (email.isBlank()) return Result.failure(Exception("Email required"))
        if (password.length < 6) return Result.failure(Exception("Password too short"))
        return authRepository.login(email, password)
    }
}
```

**domain/usecase/LogoutUseCase.kt**
```kotlin
class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> = authRepository.logout()
}
```

### Step 2: Data Layer

**data/local/AuthLocalDataSource.kt**
```kotlin
class AuthLocalDataSource(private val dataStore: DataStore<Preferences>) {
    suspend fun saveAuthToken(token: String)
    suspend fun getAuthToken(): String?
    suspend fun clearAuthToken()
}
```

**data/remote/AuthApiService.kt**
```kotlin
interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/logout")
    suspend fun logout()
}
```

**data/repository/AuthRepositoryImpl.kt**
```kotlin
class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<User> {
        // API call, save token, map to domain model
    }

    override suspend fun logout(): Result<Unit> {
        // Clear token, call API
    }
}
```

### Step 3: Presentation Layer (MVI)

**presentation/auth/login/LoginIntent.kt**
```kotlin
sealed interface LoginIntent {
    data class EmailChanged(val email: String) : LoginIntent
    data class PasswordChanged(val password: String) : LoginIntent
    object TogglePasswordVisibility : LoginIntent
    object LoginClicked : LoginIntent
}
```

**presentation/auth/login/LoginState.kt**
```kotlin
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
)
```

**presentation/auth/login/LoginEvent.kt**
```kotlin
sealed interface LoginEvent {
    object NavigateToHome : LoginEvent
    data class ShowError(val message: String) : LoginEvent
}
```

**presentation/auth/login/LoginViewModel.kt**
```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _event = Channel<LoginEvent>(Channel.BUFFERED)
    val event: Flow<LoginEvent> = _event.receiveAsFlow()

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> updateState { copy(email = intent.email, emailError = null) }
            is LoginIntent.PasswordChanged -> updateState { copy(password = intent.password, passwordError = null) }
            is LoginIntent.TogglePasswordVisibility -> updateState { copy(isPasswordVisible = !isPasswordVisible) }
            is LoginIntent.LoginClicked -> login()
        }
    }

    private fun login() = viewModelScope.launch {
        updateState { copy(isLoading = true) }

        loginUseCase(state.value.email, state.value.password)
            .onSuccess {
                _event.send(LoginEvent.NavigateToHome)
            }
            .onFailure { error ->
                _event.send(LoginEvent.ShowError(error.message ?: "Login failed"))
            }

        updateState { copy(isLoading = false) }
    }

    private fun updateState(update: LoginState.() -> LoginState) {
        _state.update(update)
    }
}
```

**presentation/auth/login/LoginScreen.kt**
```kotlin
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is LoginEvent.NavigateToHome -> onNavigateToHome()
                is LoginEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    LoginContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::handleIntent
    )
}

@Composable
private fun LoginContent(
    state: LoginState,
    snackbarHostState: SnackbarHostState,
    onIntent: (LoginIntent) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Use Design System components
            DSTextField(
                value = state.email,
                onValueChange = { onIntent(LoginIntent.EmailChanged(it)) },
                label = "Email",
                error = state.emailError,
                enabled = !state.isLoading
            )

            DSPasswordField(
                value = state.password,
                onValueChange = { onIntent(LoginIntent.PasswordChanged(it)) },
                label = "Password",
                isVisible = state.isPasswordVisible,
                onVisibilityToggle = { onIntent(LoginIntent.TogglePasswordVisibility) },
                error = state.passwordError,
                enabled = !state.isLoading
            )

            DSButton(
                text = "Login",
                onClick = { onIntent(LoginIntent.LoginClicked) },
                isLoading = state.isLoading
            )
        }
    }
}
```

### Step 4: Design System

**ui/components/DSButton.kt**
```kotlin
@Composable
fun DSButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled && !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(text)
        }
    }
}
```

**ui/components/DSTextField.kt**
```kotlin
@Composable
fun DSTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        enabled = enabled,
        singleLine = true
    )
}
```

### Step 5: Navigation Setup

**presentation/navigation/NavGraph.kt**
```kotlin
@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
```

### Step 6: Dependency Injection (Hilt)

**di/AppModule.kt**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: AuthApiService,
        localDataSource: AuthLocalDataSource
    ): AuthRepository = AuthRepositoryImpl(apiService, localDataSource)

    @Provides
    fun provideLoginUseCase(
        repository: AuthRepository
    ): LoginUseCase = LoginUseCase(repository)

    @Provides
    fun provideLogoutUseCase(
        repository: AuthRepository
    ): LogoutUseCase = LogoutUseCase(repository)
}
```

**Application class**
```kotlin
@HiltAndroidApp
class DemoConductorApp : Application()
```

## Build Configuration

### Dependencies to Add (app/build.gradle.kts)

```kotlin
dependencies {
    // Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    // Moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Testing
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}
```

### Plugin Setup

```kotlin
plugins {
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

kapt {
    correctErrorTypes = true
}
```

## Common Commands

### Build & Run
```bash
./gradlew build
./gradlew assembleDebug
./gradlew installDebug
```

### Testing
```bash
./gradlew test                      # Run unit tests
./gradlew testDebugUnitTest         # Run debug unit tests
./gradlew connectedAndroidTest      # Run instrumented tests
```

### Code Quality
```bash
./gradlew clean
./gradlew lint
./gradlew lintDebug
```

## Best Practices

### MVI Pattern
1. **Single State** - One state object per screen
2. **Immutable State** - Use `data class` with `val` properties
3. **Sealed Intents** - All user actions as sealed interface/class
4. **One-way Data Flow** - User → Intent → ViewModel → State → UI
5. **Events vs State** - Use Channel/SharedFlow for one-time events (navigation, toasts)

### Clean Architecture
1. **Dependency Rule** - Domain layer has no dependencies; Data and Presentation depend on Domain
2. **Use Cases** - Single responsibility, one use case per business action
3. **Repository Pattern** - Abstract data sources behind interfaces
4. **Mappers** - Convert between data models and domain models
5. **No Android in Domain** - Domain layer uses pure Kotlin only

### Compose
1. **Stateless Composables** - Pass state and callbacks as parameters
2. **Single Source of Truth** - ViewModel holds the state
3. **Lifecycle Awareness** - Use `collectAsStateWithLifecycle()`
4. **Reusability** - Create design system components
5. **Preview Support** - Add `@Preview` for all composables

### Testing
1. **Unit Test ViewModels** - Test state transformations and business logic
2. **Test Use Cases** - Validate business rules independently
3. **Mock Repository** - Use Mockk to test without real data sources
4. **Flow Testing** - Use Turbine to test StateFlow/SharedFlow emissions
5. **UI Tests** - Use Compose testing APIs for integration tests

## Project Structure Best Practices

- Keep domain layer pure Kotlin (no Android dependencies)
- Use dependency injection for all layers
- Create feature-based modules for larger apps
- Follow single responsibility principle
- Use meaningful package names reflecting architecture layers
- Separate UI state from business logic
- Handle errors at appropriate layers (UI shows user-friendly messages)