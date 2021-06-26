package io.github.kaczmarek.ipcalculator.utils.manager

import kotlin.math.pow

class IpManager(private val octets: List<Int>, private val cidrPrefix: Int) {
    init {
        require(isValidIpAddress()) { "IP address not valid" }
    }

    fun getFormattedCurrentIp(): String {
        val bintrayIp = convertOctetsToBinaryString(octets)
        return convertBinaryIpToOctets(bintrayIp.toLong())
    }

    fun getSubnetMask(): String {
        return convertBinaryIpToOctets(toIntMask(cidrPrefix).toLong())
    }

    fun getWildcardMask(): String {
        return convertBinaryIpToOctets(toIntMask(cidrPrefix).inv().toLong())
    }

    fun getNetworkIpAddress(): String {
        val majorIpAddress = getMajorIpAddress()
        return convertBinaryIpToOctets(majorIpAddress.toLong())
    }

    fun getBroadcastIpAddress(): String {
        val majorIpAddress = getMajorIpAddress()
        val countUsableHosts = getCountUsableHosts()
        return convertBinaryIpToOctets(majorIpAddress + countUsableHosts + 1)
    }

    fun getFirstUsableHost(): String {
        return when {
            cidrPrefix > 30 -> "В заданной сети нет адресов для рабочих хостов"
            else -> {
                val majorIpAddress = getMajorIpAddress()
                convertBinaryIpToOctets(majorIpAddress.toLong() + 1)
            }
        }
    }

    fun getLastUsableHost(): String {
        return when {
            cidrPrefix > 30 -> "В заданной сети нет адресов для рабочих хостов"
            else -> {
                val majorIpAddress = getMajorIpAddress()
                val countUsableHosts = getCountUsableHosts()
                convertBinaryIpToOctets(majorIpAddress + countUsableHosts)
            }
        }
    }

    fun getCountUsableHosts(): Long {
        val count = 2.0.pow((Integer.SIZE - cidrPrefix).toDouble()).toLong() - 2
        return if(count < 0) 0 else count
    }

    fun getCountMaxPossibleHosts(): Long {
        val count = 2.0.pow((Integer.SIZE - cidrPrefix).toDouble()).toLong()
        return if(count <= 1) 0 else count
    }

    private fun getMajorIpAddress(): Int {
        val offset = Integer.SIZE - cidrPrefix
        val majorAddress = if (cidrPrefix == 0) {
            val zeroOctets = listOf(0, 0, 0, 0)
            convertOctetsToBinaryString(zeroOctets)
        } else {
            convertOctetsToBinaryString(octets)
        }

        return majorAddress shr offset shl offset
    }

    private fun isValidIpAddress(): Boolean {
        if (octets.size != 4) return false

        octets.forEach {
            if (it < 0 || it > 255) return false
        }

        return cidrPrefix in 0..32
    }

    private fun convertOctetsToBinaryString(octets: List<Int>): Int {
        val firstOctet = octets[0]
        val secondOctet = octets[1]
        val thirdOctet = octets[2]
        val fourthOctet = octets[3]
        var output = firstOctet
        output = (output shl 8) + secondOctet
        output = (output shl 8) + thirdOctet
        output = (output shl 8) + fourthOctet
        return output
    }

    private fun convertBinaryIpToOctets(ipAddress: Long): String {
        val octet1 = ipAddress shr 24 and 255
        val octet2 = ipAddress shr 16 and 255
        val octet3 = ipAddress shr 8 and 255
        val octet4 = ipAddress and 255
        return "$octet1.$octet2.$octet3.$octet4"
    }

    private fun toIntMask(mask: Int): Int {
        if (mask == 0) return 0
        val allOne = -1
        return allOne shl Integer.SIZE - mask
    }
}