package android.com.demo.ui.login

import android.com.demo.R
import android.com.demo.databinding.FragmentLoginBinding
import android.com.demo.ui.base.BaseFragment
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(),
    LoginActions {
    override val resourceLayoutId: Int
        get() = R.layout.fragment_login

    lateinit var mNavController: NavController

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private lateinit var cryptographyManager: CryptographyManager

    override val mIsShowActionBar: Boolean
        get() = false

    override fun onInitView(root: View?) {

        cryptographyManager = CryptographyManager()

        mNavController = findNavController()
        executor = ContextCompat.getMainExecutor(this.requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        requireContext(),
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        requireContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        requireContext(), "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            //.setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()
    }

    override fun subscribeUi(viewModel: LoginViewModel) {
        binding?.apply {
            setViewModel(viewModel)
            actions = this@LoginFragment
        }
    }

    override fun onClickLoginButton() {
        biometricPrompt.authenticate(promptInfo)
    }

    override fun onClickLoginBiomectric() {
        val canAuthenticate = BiometricManager.from(requireContext()).canAuthenticate(
            BIOMETRIC_STRONG
        )
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            val secretKeyName = getString(R.string.secret_key_name)
            val biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(this) {
                    Toast.makeText(
                        requireContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()

                    val cipher =
                        cryptographyManager.getInitializedCipherForEncryption(secretKeyName)
                    val cipherDecrypt =
                        cryptographyManager.getInitializedCipherForDecryption(secretKeyName)

                    val data = cryptographyManager.encryptData("test", cipher)
                    Log.d("testt", "data: $data")

                    val plaintext = cryptographyManager.decryptData(data.ciphertext, cipherDecrypt)
                    Log.d("testt", "plaintext: $plaintext")


                    val dataSign = cryptographyManager.signData("test", secretKeyName)
                    Log.d("testt", "dataSign: $dataSign")
                    val isVerify =
                        cryptographyManager.verifySignature(dataSign, "test", secretKeyName)
                    Log.d("testt", "isVerify: $isVerify")

                    Toast.makeText(requireContext(), "isVerify: $isVerify", Toast.LENGTH_SHORT)
                        .show()
                }
            val promptInfo = BiometricPromptUtils.createPromptInfo(requireContext())
            biometricPrompt.authenticate(promptInfo)
            //biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipherDecrypt))

        }
    }
}