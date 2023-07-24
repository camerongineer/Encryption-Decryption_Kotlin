package encryptdecrypt

import java.io.File

fun main(args: Array<String>) {
    val mode = getArg(args, "-mode", "enc")
    val alg = getArg(args, "-alg", "shift")
    var data = getArg(args, "-data", "")
    val fileIn = getArg(args, "-in", "")
    if (data.isBlank() and fileIn.isNotBlank()) data = readFile(fileIn)
    val key = getArg(args, "-key", "0").toInt()
    val fileCrypt = if (alg == "unicode") algUnicode(data, key, mode) else algShift(data, key, mode)
    val fileOut = getArg(args, "-out", "")
    if (fileOut.isBlank()) println(fileCrypt) else saveFile(fileOut, fileCrypt)
}

fun algUnicode(data: String, key: Int, mode: String): String {
    val cipherMap = HashMap<Char, Char>()
    for (c in data)  {
        if (mode == "enc") cipherMap[c] = c + key else cipherMap[c] = c - key
    }
    return data.map { return@map if (it in cipherMap.keys) cipherMap[it] else it }.joinToString("")
}

fun algShift(data: String, key: Int, mode: String): String {
    var alphabet = ('a'..'z').joinToString("")
    if (mode == "dec") alphabet = alphabet.reversed()
    return data.map { return@map (
            if (it.isUpperCase()) alphabet[(alphabet.indexOf(it.lowercase()) + key) % 26].uppercaseChar()
            else if (it.isLowerCase()) alphabet[(alphabet.indexOf(it) + key) % 26]
            else it) }
        .joinToString("")
}

fun getArg(args: Array<String>, argument: String, default: String) : String {
    return if (args.contains(argument)) args[args.indexOf(argument) + 1] else default
}

fun readFile(fileName: String) : String {
    return try {
        val file = File(fileName)
        file.readText()
    } catch (_: Exception) {
        println("Read Error")
        ""
    }
}

fun saveFile(fileName: String, data: String) {
    try {
        val file = File(fileName)
        file.writeText(data)
    } catch (_: Exception) {
        println("Write Error")
    }
}