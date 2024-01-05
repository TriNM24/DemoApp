/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.com.demo.ui.login

import android.com.demo.R
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import timber.log.Timber

// Since we are using the same methods in more than one Activity, better give them their own file.
object BiometricPromptUtils {
    private const val TAG = "BiometricPromptUtils"
    fun createBiometricPrompt(
        fragment: Fragment,
        processSuccess: (BiometricPrompt.AuthenticationResult) -> Unit
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(fragment.requireContext())

        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errCode, errString)
                Timber.tag(TAG).d("errCode is  $errCode and errString is: $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Timber.tag(TAG).d("User biometric rejected.")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Timber.tag(TAG).d("Authentication was successful")
                processSuccess(result)
            }
        }
        return BiometricPrompt(fragment, executor, callback)
    }

    fun createPromptInfo(context: Context): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(context.getString(R.string.prompt_info_title))
            setSubtitle(context.getString(R.string.prompt_info_subtitle))
            setDescription(context.getString(R.string.prompt_info_description))
            setConfirmationRequired(false)
            setNegativeButtonText(context.getString(R.string.prompt_info_use_app_password))
        }.build()
}