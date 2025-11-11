package com.student.Compass_Abroad.encrytion

import android.util.Log
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun encryptData(data: String, encryptionKeyHexString: String, ivHexString: String): String? {

    try {
        Log.e("datta","$data");
        val encryptionKeyData = encryptionKeyHexString.hexToByteArray
        val ivData = ivHexString.hexToByteArray

        val secretKey: SecretKey = SecretKeySpec(encryptionKeyData, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivParameterSpec = IvParameterSpec(ivData)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)

        val encryptedData = cipher.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedData)
    } catch (e: Exception) {

        e.printStackTrace()

        return null
    }

    }
    fun String.hexToByteArray(): ByteArray {

    return ByteArray(this.length / 2) { this.substring(it * 2, it * 2 + 2).toInt(16).toByte() }

        }