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

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import java.nio.charset.Charset
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Handles encryption and decryption
 */
interface CryptographyManager {

    fun getInitializedCipherForEncryption(keyName: String): Cipher

    fun getInitializedCipherForDecryption(keyName: String): Cipher

    /**
     * The Cipher created with [getInitializedCipherForEncryption] is used here
     */
    fun encryptData(plaintext: String, cipher: Cipher): CiphertextWrapper

    /**
     * The Cipher created with [getInitializedCipherForDecryption] is used here
     */
    fun decryptData(ciphertext: ByteArray, cipher: Cipher): String

    fun persistCiphertextWrapperToSharedPrefs(
        ciphertextWrapper: CiphertextWrapper,
        context: Context,
        filename: String,
        mode: Int,
        prefKey: String
    )

    fun getCiphertextWrapperFromSharedPrefs(
        context: Context,
        filename: String,
        mode: Int,
        prefKey: String
    ): CiphertextWrapper?

    fun signData(data: String, keyName: String): String

    fun verifySignature(signature: String, data: String, keyName: String): Boolean

}

fun CryptographyManager(): CryptographyManager = CryptographyManagerImpl()

/**
 * To get an instance of this private CryptographyManagerImpl class, use the top-level function
 * fun CryptographyManager(): CryptographyManager = CryptographyManagerImpl()
 */
private class CryptographyManagerImpl : CryptographyManager {

    private val KEY_SIZE = 2048
    private val ANDROID_KEYSTORE = "AndroidKeyStore"
    private val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_ECB
    private val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1
    private val SIGNATURE_PADDING = KeyProperties.SIGNATURE_PADDING_RSA_PKCS1
    private val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_RSA

    override fun getInitializedCipherForEncryption(keyName: String): Cipher {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keyName).public
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher
    }

    override fun getInitializedCipherForDecryption(
        keyName: String
    ): Cipher {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keyName).private
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return cipher
    }

    override fun encryptData(plaintext: String, cipher: Cipher): CiphertextWrapper {
        val ciphertext = cipher.doFinal(plaintext.toByteArray(Charset.forName("UTF-8")))
        val textToServer = String(ciphertext, Charset.forName("UTF-8"))
        Log.d("testt", "textToServer: $textToServer")
        return CiphertextWrapper(ciphertext, cipher.iv)
    }

    override fun decryptData(ciphertext: ByteArray, cipher: Cipher): String {
        val textFromLocal = String(ciphertext, Charset.forName("UTF-8"))
        Log.d("testt", "textFromLocal: $textFromLocal")
        val plaintext = cipher.doFinal(ciphertext)
        return String(plaintext, Charset.forName("UTF-8"))
    }

    private fun getCipher(): Cipher {
        //val transformation = "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"
        val transformation = "RSA/ECB/PKCS1Padding"
        return Cipher.getInstance(transformation)
    }

    private fun getOrCreateSecretKey(keyName: String): KeyPair {

        // Check if the RSA key pair already exists
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        if (!keyStore.containsAlias(keyName)) {
            // Generate a new RSA key pair
            val keyPairGenerator =
                KeyPairGenerator.getInstance(ENCRYPTION_ALGORITHM, ANDROID_KEYSTORE)
            val spec = KeyGenParameterSpec.Builder(
                keyName,
                KeyProperties.PURPOSE_SIGN
                        or KeyProperties.PURPOSE_VERIFY
                        or KeyProperties.PURPOSE_ENCRYPT
                        or KeyProperties.PURPOSE_DECRYPT
            )
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                .setDigests(KeyProperties.DIGEST_SHA256)
                .setKeySize(2048)
                .setUserAuthenticationRequired(true)

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
                spec.setUserAuthenticationValidityDurationSeconds(10)
            }else{
                 spec.setUserAuthenticationParameters(10, KeyProperties.AUTH_BIOMETRIC_STRONG)
            }


            keyPairGenerator.initialize(spec.build())
            return keyPairGenerator.generateKeyPair()
        } else {
            // Load the existing RSA key pair
            val privateKey = keyStore.getKey(keyName, null) as PrivateKey
            val publicKey = keyStore.getCertificate(keyName).publicKey
            return KeyPair(publicKey, privateKey)
        }

    }

    override fun persistCiphertextWrapperToSharedPrefs(
        ciphertextWrapper: CiphertextWrapper,
        context: Context,
        filename: String,
        mode: Int,
        prefKey: String
    ) {
        val json = Gson().toJson(ciphertextWrapper)
        context.getSharedPreferences(filename, mode).edit().putString(prefKey, json).apply()
    }

    override fun getCiphertextWrapperFromSharedPrefs(
        context: Context,
        filename: String,
        mode: Int,
        prefKey: String
    ): CiphertextWrapper? {
        val json = context.getSharedPreferences(filename, mode).getString(prefKey, null)
        return Gson().fromJson(json, CiphertextWrapper::class.java)
    }

    override fun signData(data: String, keyName: String): String {
        val privateKey = getOrCreateSecretKey(keyName).private
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initSign(privateKey)
        signature.update(data.toByteArray(Charset.forName("UTF-8")))
        //String(Base64.getEncoder().encode(signature.sign())
        return android.util.Base64.encodeToString(signature.sign(), android.util.Base64.DEFAULT)
    }

    override fun verifySignature(signature: String, data: String, keyName: String): Boolean {
        val publicKeyOld = getOrCreateSecretKey(keyName).public

        //test convert public key to string
        val publicKeyString = android.util.Base64.encodeToString(publicKeyOld.encoded, android.util.Base64.DEFAULT)

        //convert publicKeyString to public key
        val publicKeyByte = android.util.Base64.decode(publicKeyString, android.util.Base64.DEFAULT)
        val spec = X509EncodedKeySpec(publicKeyByte)
        val keyFactory = KeyFactory.getInstance(ENCRYPTION_ALGORITHM)
        val publicKey = keyFactory.generatePublic(spec)

        val signatureToVerify = Signature.getInstance("SHA256withRSA")
        signatureToVerify.initVerify(publicKey)
        signatureToVerify.update(data.toByteArray(Charset.forName("UTF-8")))
        val signatureByte = android.util.Base64.decode(signature, android.util.Base64.DEFAULT)
        return signatureToVerify.verify(signatureByte)
    }
}


data class CiphertextWrapper(val ciphertext: ByteArray, val initializationVector: ByteArray?)