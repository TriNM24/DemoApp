package android.com.demo.ui.login

import android.com.demo.R
import android.com.demo.utils.Constants
import android.com.demo.utils.SingleLiveEvent
import android.content.Context
import android.database.Observable
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var cryptographyManager: CryptographyManager

    val mDecryptedData : SingleLiveEvent<String> = SingleLiveEvent()
    val mEncryptData : SingleLiveEvent<String> = SingleLiveEvent()
    val mInputPass: MutableLiveData<String> = MutableLiveData("")
    init {
        Timber.d("Create LoginViewModel")
        cryptographyManager = CryptographyManager()
    }

    fun savePass(pass:String){
        //step 1 save password using keystore
        val encryptedData = cryptographyManager.encryptData(
            pass,
            cryptographyManager.getInitializedCipherForEncryption(context.getString(R.string.secret_key_name_case1))
        )
        //save encrypted data to local preference
        cryptographyManager.persistCiphertextWrapperToSharedPrefs(
            encryptedData,
            context,
            Constants.SHARED_PREFS_FILENAME,
            Context.MODE_PRIVATE,
            Constants.CIPHERTEXT_WRAPPER
        )

        mEncryptData.value = "Save pass '$pass' success"
    }

    fun getSavedPass(){
        //get saved encrypted data
        val encrytedData = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
            context,
            Constants.SHARED_PREFS_FILENAME,
            Context.MODE_PRIVATE,
            Constants.CIPHERTEXT_WRAPPER
        )
        encrytedData?.let {
            val cipherDecrypt =
                cryptographyManager.getInitializedCipherForDecryption(context.getString(R.string.secret_key_name_case1))
            val decryptedData =
                cryptographyManager.decryptData(it.ciphertext, cipherDecrypt)

            mDecryptedData.postValue(decryptedData)
        }?:run {
            mDecryptedData.postValue("have no data")
        }
    }

}