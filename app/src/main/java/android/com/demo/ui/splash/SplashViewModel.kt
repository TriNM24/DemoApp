package android.com.demo.ui.splash

import android.com.demo.utils.SingleLiveEvent
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    val resultChecking: SingleLiveEvent<ResultSplash> = SingleLiveEvent()

    init {
        Timber.d("Create SplashViewModel")
    }

    fun processChecking() {
        viewModelScope.launch {
            delay(1000)
            //check is already login
            resultChecking.postValue(ResultSplash.MAIN)
        }
    }
}

enum class ResultSplash {
    LOGIN,
    MAIN
}