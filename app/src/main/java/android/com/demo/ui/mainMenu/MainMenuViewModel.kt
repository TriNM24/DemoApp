package android.com.demo.ui.mainMenu

import android.com.demo.data.repository.DataRepository
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainMenuViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: DataRepository
) : ViewModel() {
    init {
        Timber.d("Create MainMenuViewModel")
    }
}