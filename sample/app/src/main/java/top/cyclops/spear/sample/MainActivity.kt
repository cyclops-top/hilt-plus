@file:Suppress("unused")

package top.cyclops.spear.sample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.cyclops.spear.module1.data.model.User
import top.cyclops.spear.module1.data.repo.UserRepository
import top.cyclops.spear.module2.data.repo.MusicRepository
import top.cyclops.spear.sample.data.local.TestDao
import top.cyclops.spear.sample.databinding.ActivityMainBinding
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lifecycleScope.apply {
            launch {
                viewModel.user.collectLatest {
                    binding.id.text = it?.id?.toString() ?: "id is null"
                    binding.name.text = it?.name ?: "name is null"
                }
            }
        }
        binding.idInput.editText?.doAfterTextChanged {
            val id = it?.toString()?.toLongOrNull() ?: return@doAfterTextChanged
            viewModel.setId(id)
        }
        binding.save.setOnClickListener {
            val name = binding.nameInput.editText?.text?.toString() ?: return@setOnClickListener
            lifecycleScope.launch {
                viewModel.setName(
                    name
                )
            }
        }
    }
}


@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val musicRepository: MusicRepository,
    private val testDao: TestDao,
) : ViewModel() {
    private val userId = MutableStateFlow<Long?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val user = userId.filterNotNull()
        .flatMapLatest {
            userRepository.user(it)
        }

    fun setId(id: Long) {
        userId.value = id
    }

    suspend fun setName(name: String) = withContext(viewModelScope.coroutineContext) {
        val id = userId.value ?: return@withContext
        val user = userRepository.findUserById(id)
        if (user == null) {
            userRepository.insert(User(id, name, Date()))
        } else {
            userRepository.update(user.copy(name = name))
        }
    }
}