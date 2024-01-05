package android.com.demo.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    init {
        Timber.d("Create LoginViewModel")
    }
}