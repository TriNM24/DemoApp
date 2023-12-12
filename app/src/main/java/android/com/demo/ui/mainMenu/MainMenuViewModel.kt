package android.com.demo.ui.mainMenu

import android.com.demo.data.api.Resource
import android.com.demo.data.repository.DataRepository
import android.com.demo.data.room.entity.SellEntity
import android.com.demo.utils.SingleLiveEvent
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.core.Amplify
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainMenuViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: DataRepository
) : ViewModel() {

    val mProcessData: SingleLiveEvent<Resource<String>> = SingleLiveEvent()

    init {
        Timber.d("Create MainMenuViewModel")
    }

    fun loginCognito(userName: String, pass: String) {
        viewModelScope.launch {
            mProcessData.value = Resource.loading(null)
            Amplify.Auth.signIn(userName, pass,
                { result ->
                    if (result.isSignedIn) {
                        mProcessData.postValue(Resource.success("Sign in succeeded"))
                    } else {
                        mProcessData.postValue(Resource.error("Sign in not complete", null))
                    }
                },
                {
                    mProcessData.postValue(Resource.error("Sign in failed", null))
                }
            )
        }
    }

    fun logoutCognito() {
        viewModelScope.launch {
            mProcessData.value = Resource.loading(null)
            Amplify.Auth.signOut { signOutResult ->
                when(signOutResult) {
                    is AWSCognitoAuthSignOutResult.CompleteSignOut -> {
                        // Sign Out completed fully and without errors.
                        mProcessData.postValue(Resource.success("Sign out succeeded"))
                    }
                    is AWSCognitoAuthSignOutResult.PartialSignOut -> {
                        // Sign Out completed with some errors. User is signed out of the device.
                        signOutResult.hostedUIError?.let {
                            mProcessData.postValue(Resource.error("HostedUI Error", null))
                            // Optional: Re-launch it.url in a Custom tab to clear Cognito web session.

                        }
                        signOutResult.globalSignOutError?.let {
                            mProcessData.postValue(Resource.error("GlobalSignOut Error", null))
                            // Optional: Use escape hatch to retry revocation of it.accessToken.
                        }
                        signOutResult.revokeTokenError?.let {
                            mProcessData.postValue(Resource.error("RevokeToken Error", null))
                            // Optional: Use escape hatch to retry revocation of it.refreshToken.
                        }
                    }
                    is AWSCognitoAuthSignOutResult.FailedSignOut -> {
                        mProcessData.value = Resource.error("Sign out failed", null)
                        // Sign Out failed with an exception, leaving the user signed in.
                    }
                }
            }
        }
    }
}