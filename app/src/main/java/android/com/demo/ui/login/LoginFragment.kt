package android.com.demo.ui.login

import android.com.demo.R
import android.com.demo.databinding.FragmentLoginBinding
import android.com.demo.ui.base.BaseFragment
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedReader
import java.io.InputStreamReader
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.concurrent.Executor
import javax.crypto.Cipher


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
                    viewModel.getSavedPass()
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
            .setTitle("Biometric login for my app aaaaaaa")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            //.setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()
    }


    override fun subscribeUi(viewModel: LoginViewModel) {

        Handler(Looper.getMainLooper()).postDelayed({ viewModel.mInputPass.value = "default" }, 500)

        binding?.apply {
            setViewModel(viewModel)
            lifecycleOwner = this@LoginFragment
            actions = this@LoginFragment
        }
        viewModel.mDecryptedData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Decrypted data: $it", Toast.LENGTH_SHORT).show()
        }
        viewModel.mEncryptData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
        }
        viewModel.mInputPass.observe(viewLifecycleOwner) {
            Log.d("testt", "inputed data:$it")
        }
    }

    override fun onClickLoginButton() {
        viewModel.mInputPass.value?.let { viewModel.savePass(it) }
    }

    /**
     * Encrypt data using public key
     * default public key is public_key_1.pem
     */
    private fun encryptDataUsingPublicKey(data: String, publicKey: String): String {
        val assetManager = context?.assets
        val inputStream = assetManager?.open(publicKey)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val pemContent = reader.use { it.readText() }

        val publicKeyStr = pemContent.replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\n", "")

        val publicKeyBytes = Base64.decode(publicKeyStr, Base64.DEFAULT)

        val keySpec = X509EncodedKeySpec(publicKeyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")  // Adjust algorithm if needed
        val publicKey = keyFactory.generatePublic(keySpec)

        val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)

        val encryptedBytes = cipher.doFinal(data.toByteArray(Charsets.UTF_8))

        val encryptedStr =
            Base64.encodeToString(encryptedBytes, Base64.DEFAULT)

        Log.d("testt", encryptedStr)
        return encryptedStr
    }

    override fun onClickGetSavedPass() {

        //executor = ContextCompat.getMainExecutor(this.requireContext())
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
                    viewModel.getSavedPass()
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

        biometricPrompt.authenticate(promptInfo)
    }

    override fun onClickLoginBiomectric() {

        val canAuthenticate = BiometricManager.from(requireContext()).canAuthenticate(
            BIOMETRIC_STRONG
        )
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            val secretKeyName = getString(R.string.secret_key_name)
            val biometricPrompt2 =
                BiometricPromptUtils().createBiometricPrompt(this) {
                    Toast.makeText(
                        requireContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()

                    val assetManager = context?.assets
                    val inputStream = assetManager?.open("public_key_1.pem")
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val pemContent = reader.use { it.readText() }

                    val publicKeyStr = pemContent.replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replace("\n", "")

                    val publicKeyBytes = Base64.decode(publicKeyStr, Base64.DEFAULT)

                    val keySpec = X509EncodedKeySpec(publicKeyBytes)
                    val keyFactory = KeyFactory.getInstance("RSA")  // Adjust algorithm if needed
                    val publicKey = keyFactory.generatePublic(keySpec)

                    val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
                    cipher.init(Cipher.ENCRYPT_MODE, publicKey)

                    val encryptedBytes = cipher.doFinal("test".toByteArray(Charsets.UTF_8))

                    val encryptedStr =
                        Base64.encodeToString(encryptedBytes, Base64.DEFAULT)

                    Log.d("testt", encryptedStr)

                    //Old code
                    /*val cipher =
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
                        .show()*/
                }
            val promptInfo2 = BiometricPromptUtils().createPromptInfo(requireContext())
            biometricPrompt2.authenticate(promptInfo2)
            //biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cryptographyManager.getInitializedCipherForEncryption(secretKeyName)))
        }
    }
}