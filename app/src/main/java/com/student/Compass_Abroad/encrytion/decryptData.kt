package com.student.Compass_Abroad.encrytion


import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun decryptData(encryptedData: String, encryptionKeyHexString: String, ivHexString: String): String? {

    try {
        val encryptedDataBytes = Base64.getDecoder().decode(encryptedData)
        val encryptionKeyData = encryptionKeyHexString.hexToByteArray
        val ivData = ivHexString.hexToByteArray
        val secretKey: SecretKey = SecretKeySpec(encryptionKeyData, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivParameterSpec = IvParameterSpec(ivData)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)
        val decryptedData = cipher.doFinal(encryptedDataBytes)
        return String(decryptedData)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

}
    val String.hexToByteArray: ByteArray

        get() {

            return ByteArray(this.length / 2) { this.substring(it * 2, it * 2 + 2).toInt(16).toByte() }
        }