# Hilt Plus
[![](https://img.shields.io/badge/ksp-2.1.10--1.0.31-important?logo=kotlin)](https://github.com/google/ksp)
[![](https://img.shields.io/badge/hilt-2.56.2-important?logo=android)](https://github.com/google/ksp)
[![](https://img.shields.io/badge/hilt--plus-0.1.3-blueviolet?logo=android)](https://github.com/cyclops-top/hilt-plus)
> 基于 Hilt 的轻量级扩展库，提供 **Room 多模块聚合**和**动态代理接口自动化注入**能力，用 20% 的代码解决 80% 的样板代码问题。

---

## 安装
```kotlin
// build.gradle.kts
plugins {
    id("com.google.devtools.ksp") version "2.1.10-1.0.31" // 需启用 KSP
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.56.2")
    ksp("com.google.dagger:hilt-compiler:2.56.2")
    implementation("top.cyclops:hilt-plus:0.1.3")
    ksp("top.cyclops:hilt-plus-compiler:0.1.3")
}
```

---

## 功能 1：动态代理接口自动化注入
### 核心原理
通过 `@HiltApi` 标记接口，自动生成 Hilt 的 `Provider` 模块，开发者只需提供 `ApiCreator` 实现类（如 Retrofit 实例）。

### 三步接入
#### 1. 定义接口（自动生成 Provider）
```kotlin
@HiltApi
@ServerA  // 可选：配合限定符区分不同服务
interface GitHubApi {
    @GET("/users/{name}")
    suspend fun getUser(@Path("name") name: String): User
}
```

#### 2. 提供 ApiCreator 实现
```kotlin
@dagger.Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @ServerA  // 匹配接口的限定符
    fun provideApiCreator(retrofit: Retrofit): ApiCreator = retrofit.create(clazz) 
}
```

#### 3. 直接注入使用
```kotlin
@HiltViewModel
class UserViewModel @Inject constructor(
    @ServerA private val gitHubApi: GitHubApi // 自动注入
) : ViewModel() {
    fun loadUser(name: String) = viewModelScope.context {
        val user = gitHubApi.getUser(name) // 直接调用
    }
}
```

---

## 功能 2：Room 多模块聚合
### 痛点解决
- 🚫 **传统问题**：多模块需在 `@Database` 中手动聚合所有 Entity 和 Dao
- ✅ **Hilt Plus 方案**：通过 `@HiltDao` + **模块节点** 自动聚合

### 单模块配置
#### 1. 定义 Dao（自动注册到 Database）
```kotlin
@HiltDao(entities = [User::class]) // 声明关联的 Entity
@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE id = :id")
    fun getById(id: Long): Flow<User?>
}
```

#### 2. 定义 Database（自动聚合）
```kotlin
@HiltRoom(name = "app", version = 1)
@TypeConverters(LocalDateConverter::class)
interface AppDatabase : DatabaseTransaction // 必须继承事务接口
```

#### 🔑 可注入类型
```kotlin
@AndroidEntryPoint
class UserRepository @Inject constructor(
    private val userDao: UserDao,           // 直接注入 Dao
    private val dbTransaction: DatabaseTransaction // 注入事务管理
) {
    fun safeUpdate(user: User) = dbTransaction.runInTransaction {
        userDao.update(user)
    }
}
```

---

### 多模块配置（大型项目）
#### 1. 定义模块节点
```kotlin
// module_user 模块
@Qualifier
annotation class UserModuleNode  // 用户模块节点

// module_music 模块
@Qualifier
annotation class MusicModuleNode // 音乐模块节点
```

#### 2. 分模块声明 Dao
```kotlin
// module_user 中
@HiltDao(
    entities = [User::class], 
    node = UserModuleNode::class // 绑定到用户模块节点
)
@Dao
interface UserDao { /* ... */ }

// module_music 中
@HiltDao(
    entities = [Song::class], 
    node = MusicModuleNode::class // 绑定到音乐模块节点
)
@Dao
interface SongDao { /* ... */ }
```

#### 3. 主模块聚合 Database
```kotlin
@HiltRoom(
    name = "main",
    version = 1,
    nodes = [UserModuleNode::class, MusicModuleNode::class] // 聚合多模块
)
interface MainDatabase : DatabaseTransaction
```

#### 🔑 跨模块注入
```kotlin
@AndroidEntryPoint
class MusicService @Inject constructor(
    @MusicModuleNode  // 限定符指定模块节点
    private val transaction: DatabaseTransaction
) {
    fun importSongs(songs: List<Song>) = transaction.withTransction {
        // 使用音乐模块的事务上下文
    }
}
```

---

## 技术优势
| 功能                | 传统方案                          | Hilt Plus                     |
|---------------------|-----------------------------------|-------------------------------|
| **Retrofit 接口注入** | 手动编写 `@Provides` 模块          | 通过 `@HiltApi` 自动生成       |
| **Room 多模块**      | 需在主模块手动注册所有 Dao         | `@HiltDao` 分模块声明自动聚合  |
| **事务管理**         | 需通过 `@Database` 实例获取         | 直接注入 `DatabaseTransaction` |

---

## 注意事项
1. **KSP 版本对齐**  
   需保证 `ksp` 插件版本与 Kotlin 版本匹配。

2. **模块可见性**  
   多模块场景中，`@HiltDao` 接口需对主模块可见（避免使用 `internal`）。

3. **ProGuard 规则**  
   若启用混淆，需添加规则保留生成的 `_Database` 和 `_Impl` 类。

---

## 完整示例
访问 [GitHub 仓库](https://github.com/cyclops-top/hilt-plus) 获取：
- 🛠️ 单模块完整 Demo
- 🧩 多模块聚合示例