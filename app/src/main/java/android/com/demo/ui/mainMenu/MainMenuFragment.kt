package android.com.demo.ui.mainMenu

import android.com.demo.R
import android.com.demo.databinding.FragmentMainMenuBinding
import android.com.demo.ui.base.BaseFragment
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.amplifyframework.analytics.AnalyticsEvent
import com.amplifyframework.core.Amplify
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.File
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory


@AndroidEntryPoint
class MainMenuFragment : BaseFragment<FragmentMainMenuBinding, MainMenuViewModel>(),
    MainMenuActions {
    override val resourceLayoutId: Int
        get() = R.layout.fragment_main_menu

    lateinit var mNavController: NavController

    private val USER_CUSTOM_VIEW = "user_custom_view"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //config remote config
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("testt", "Config params updated: $updated")
                    Toast.makeText(
                        requireContext(),
                        "Fetch and activate succeeded",
                        Toast.LENGTH_SHORT,
                    ).show()

                    val userName = remoteConfig.getString("user_name")
                    val pass = remoteConfig.getString("pass")
                    Log.d("testt","user: $userName, pass:$pass")
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Fetch failed",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    override fun onInitView(root: View?) {
        mNavController = findNavController()
    }

    override fun subscribeUi(viewModel: MainMenuViewModel) {
        binding?.viewModel = viewModel
        binding?.actions = this
    }

    override fun onClickCallButton() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM){
            param(FirebaseAnalytics.Param.ITEM_NAME, "onClickCallButton")
            param(FirebaseAnalytics.Param.SCREEN_NAME, "onClickCallButton screen")
        }
        //send custom log
        val paramBuilder = ParametersBuilder()
        paramBuilder.param("user_custom_view_name", "onClickCallButton")
        paramBuilder.param("user_custom_view_id", "onClickCallButtonID")

        firebaseAnalytics.logEvent(USER_CUSTOM_VIEW, paramBuilder.bundle)

        if(mNavController.currentDestination?.id == R.id.nav_home) {
            mNavController.navigate(R.id.action_nav_home_to_callListFragment)
        }
    }

    override fun onClickBuyButton() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM){
            param(FirebaseAnalytics.Param.ITEM_NAME, "onClickBuyButton")
            param(FirebaseAnalytics.Param.SCREEN_NAME, "onClickBuyButton screen")
        }

        //send custom log
        val paramBuilder = ParametersBuilder()
        paramBuilder.param("user_custom_view_name", "onClickBuyButton")
        paramBuilder.param("user_custom_view_id", "onClickBuyButtonID")
        firebaseAnalytics.logEvent(USER_CUSTOM_VIEW, paramBuilder.bundle)

        if(mNavController.currentDestination?.id == R.id.nav_home) {
            mNavController.navigate(R.id.action_nav_home_to_buyListFragment)
        }
    }

    override fun onClickSellButton() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM){
            param(FirebaseAnalytics.Param.ITEM_NAME, "onClickSellButton")
            param(FirebaseAnalytics.Param.SCREEN_NAME, "onClickSellButton screen")
        }
        //send custom log
        val paramBuilder = ParametersBuilder()
        paramBuilder.param("user_custom_view_name", "onClickSellButton")
        paramBuilder.param("user_custom_view_id", "onClickSellButtonID")
        firebaseAnalytics.logEvent(USER_CUSTOM_VIEW, paramBuilder.bundle)

        if(mNavController.currentDestination?.id == R.id.nav_home) {
            mNavController.navigate(R.id.action_nav_home_to_sellListFragment)
        }
    }

    override fun onClickCrashButton() {
        /*firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM){
            param(FirebaseAnalytics.Param.ITEM_NAME, "onClickCrashButton")
            param(FirebaseAnalytics.Param.SCREEN_NAME, "onClickCrashButton screen")
        }
        //send custom log
        val paramBuilder = ParametersBuilder()
        paramBuilder.param("user_custom_view_name", "onClickCrashButton")
        paramBuilder.param("user_custom_view_id", "onClickCrashButtonID")
        firebaseAnalytics.logEvent(USER_CUSTOM_VIEW, paramBuilder.bundle)
        val temp = 1/0*/
        //testt

        FirebaseAuth.getInstance().signInWithEmailAndPassword("test@gmail.com","123456").addOnCompleteListener {
            if(it.isSuccessful){
                var storageRef = FirebaseStorage.getInstance().reference
                val pathReference = storageRef.child("cert/champion.cer")
                val rootPath = File(requireContext().filesDir,"cer")
                if (!rootPath.exists()) {
                    rootPath.mkdirs()
                }
                val localFile = File(rootPath, "champion.cer")
                pathReference.getFile(localFile).addOnSuccessListener {
                    Timber.tag("testt").d("file download successed")
                    readCert()
                }.addOnFailureListener{ ex ->
                    Timber.tag("testt").d("Download error: ${ex.message}")
                }
            }else{
                Toast.makeText(requireActivity(), "login fail", Toast.LENGTH_SHORT).show()
                Log.d("testt",it.result.toString())
            }
        }
    }

    override fun onClickSendLogButton() {
        val event = AnalyticsEvent.builder()
            .name("PasswordReset")
            .addProperty("Channel", "SMS")
            .addProperty("Successful", true)
            .addProperty("ProcessDuration", 792)
            .addProperty("UserAge", 120.3)
            .build()

        Amplify.Analytics.recordEvent(event)
    }

    private fun readCert(){
        val CERT_FILE_NAME = "champion_221026.cer" // 3.vib.com.vn.cer
        val CERT_ALIAS = "ca"
        val X_509 = "X.509"
        val TLS = "TLS"

        val context = SSLContext.getInstance(TLS)
        val trustManagerFactory: TrustManagerFactory

        val cf = CertificateFactory.getInstance(X_509)
        val cert: Certificate

        val fileCert: InputStream = BufferedInputStream(
            File(requireContext().filesDir,"cer/champion.cer").inputStream()
        )
        try {
            cert = cf.generateCertificate(fileCert)
            Log.e("KeyPinStore", "ca = " + (cert as X509Certificate).subjectDN)
        } finally {
            fileCert.close()
        }
        // Create a KeyStore containing our trusted CAs
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry(CERT_ALIAS, cert)

        // Create a TrustManager that trusts the CAs in our KeyStore
        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm)
        trustManagerFactory.init(keyStore)

        // Create an SSLContext that uses our TrustManager
        // SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, trustManagerFactory.trustManagers, null)
    }
}