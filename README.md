# Hilt Plus
[![](https://img.shields.io/badge/ksp-2.1.10--1.0.31-important?logo=kotlin)](https://github.com/google/ksp)
[![](https://img.shields.io/badge/hilt-2.56.2-important?logo=android)](https://developer.android.com/training/dependency-injection/hilt-android?hl=zh-cn)
[![](https://img.shields.io/badge/hilt--plus-0.1.3-blueviolet?logo=android)](https://github.com/cyclops-top/hilt-plus)
> A lightweight extension library based on Hilt, providing **Room multi-module aggregation** and **automated injection for dynamic proxy interfaces**, solving 80% of boilerplate code with 20% of the code.

---
[中文](README_CN.md)
## Installation
```kotlin
// build.gradle.kts
plugins {
    id("com.google.devtools.ksp") version "2.1.10-1.0.31" // KSP required
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.56.2")
    ksp("com.google.dagger:hilt-compiler:2.56.2")
    implementation("top.cyclops:hilt-plus:0.1.3")
    ksp("top.cyclops:hilt-plus-compiler:0.1.3")
}
```

---

## Feature 1: Automated Injection of Dynamic Proxy Interfaces
### Core Principle
Mark interfaces with `@HiltApi` to automatically generate Hilt `Provider` modules. Developers only need to provide `ApiCreator` implementations (e.g., Retrofit instances).

### 3-Step Integration
#### 1. Define Interface (Auto-generate Provider)
```kotlin
@HiltApi
@ServerA  // Optional: Use qualifier to differentiate services
interface GitHubApi {
    @GET("/users/{name}")
    suspend fun getUser(@Path("name") name: String): User
}
```

#### 2. Provide ApiCreator Implementation
```kotlin
@dagger.Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @ServerA  // Match interface qualifier
    fun provideApiCreator(retrofit: Retrofit): ApiCreator = retrofit.create(clazz) 
}
```

#### 3. Direct Injection & Usage
```kotlin
@HiltViewModel
class UserViewModel @Inject constructor(
    @ServerA private val gitHubApi: GitHubApi // Auto-injected
) : ViewModel() {
    fun loadUser(name: String) = viewModelScope.context {
        val user = gitHubApi.getUser(name) // Direct call
    }
}
```

---

## Feature 2: Room Multi-module Aggregation
### Pain Points Solved
- 🚫 **Traditional Issue**: Manual aggregation of all Entities and Daos in `@Database` for multi-module projects
- ✅ **Hilt Plus Solution**: Automatic aggregation via `@HiltDao` + **Module Nodes**

### Single Module Configuration
#### 1. Define Dao (Auto-register to Database)
```kotlin
@HiltDao(entities = [User::class]) // Declare associated Entity
@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE id = :id")
    fun getById(id: Long): Flow<User?>
}
```

#### 2. Define Database (Auto-aggregation)
```kotlin
@HiltRoom(name = "app", version = 1)
@TypeConverters(LocalDateConverter::class)
interface AppDatabase : DatabaseTransaction // Must inherit transaction interface
```

#### 🔑 Injectable Types
```kotlin
@AndroidEntryPoint
class UserRepository @Inject constructor(
    private val userDao: UserDao,           // Direct Dao injection
    private val dbTransaction: DatabaseTransaction // Transaction management
) {
    fun safeUpdate(user: User) = dbTransaction.runInTransaction {
        userDao.update(user)
    }
}
```

---

### Multi-module Configuration (Large Projects)
#### 1. Define Module Nodes
```kotlin
// module_user module
@Qualifier
annotation class UserModuleNode  // User module node

// module_music module
@Qualifier
annotation class MusicModuleNode // Music module node
```

#### 2. Declare Daos per Module
```kotlin
// In module_user
@HiltDao(
    entities = [User::class], 
    node = UserModuleNode::class // Bind to user module node
)
@Dao
interface UserDao { /* ... */ }

// In module_music
@HiltDao(
    entities = [Song::class], 
    node = MusicModuleNode::class // Bind to music module node
)
@Dao
interface SongDao { /* ... */ }
```

#### 3. Aggregate in Main Module Database
```kotlin
@HiltRoom(
    name = "main",
    version = 1,
    nodes = [UserModuleNode::class, MusicModuleNode::class] // Aggregate modules
)
interface MainDatabase : DatabaseTransaction
```

#### 🔑 Cross-module Injection
```kotlin
@AndroidEntryPoint
class MusicService @Inject constructor(
    @MusicModuleNode  // Qualifier specifies module node
    private val transaction: DatabaseTransaction
) {
    fun importSongs(songs: List<Song>) = transaction.withTransction {
        // Use music module's transaction context
    }
}
```

---

## Technical Advantages
| Feature               | Traditional Approach               | Hilt Plus                    |
|-----------------------|------------------------------------|------------------------------|
| **Retrofit Injection** | Manual `@Provides` modules         | Auto-generated via `@HiltApi` |
| **Room Multi-module**  | Manual Dao registration in main module | Auto-aggregation via `@HiltDao` |
| **Transaction Mgmt**  | Via `@Database` instance            | Direct `DatabaseTransaction` injection |

---

## Notes
1. **KSP Version Alignment**  
   Ensure `ksp` plugin version matches Kotlin version.

2. **Module Visibility**  
   In multi-module projects, `@HiltDao` interfaces must be visible to main module (avoid `internal`).

3. **ProGuard Rules**  
   Add rules to retain generated `_Database` and `_Impl` classes if using obfuscation.

---

## Complete Examples
Visit [GitHub Repository](https://github.com/cyclops-top/hilt-plus) for:
- 🛠️ Single-module Demo
- 🧩 Multi-module Aggregation Example