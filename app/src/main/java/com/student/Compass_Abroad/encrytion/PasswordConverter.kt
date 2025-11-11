package com.student.Compass_Abroad.encrytion

import java.math.BigInteger
import java.security.MessageDigest

class PasswordConverter {

    private fun sha1(input: String): String {

        val digest = MessageDigest.getInstance("SHA-1")
        val result = digest.digest(input.toByteArray())
        val sha1Hex = BigInteger(1, result).toString(16).padStart(40, '0')
        return sha1Hex

    }

    private fun md5(input: String): String {

        val digest = MessageDigest.getInstance("MD5")
        val result = digest.digest(input.toByteArray())
        val md5Hex = BigInteger(1, result).toString(16).padStart(32, '0')
        return md5Hex

    }

    fun convertPasswordToMD5(inputPassword: String): String {
        val sha1Hash = sha1(inputPassword)
        val md5Hash = md5(sha1Hash)
        return md5Hash
    }
}