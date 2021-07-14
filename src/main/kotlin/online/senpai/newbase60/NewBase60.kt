package online.senpai.newbase60

import java.text.ParseException

const val CHARACTERS = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ_abcdefghijkmnopqrstuvwxyz"

/**
 * Converts a positive decimal number into its NewBase60 representation.
 * @param number the decimal number to convert.
 * @return the NewBase60 representation of the decimal number.
 * @throws IllegalArgumentException if the number is less than zero.
 */
fun numberToSexagesimal(number: Long): String {
    require(number >= 0L) { "The number must be non-negative" }
    if (number == 0L) return "0"
    val sb = StringBuilder()
    var n: Long = number
    while (n > 0) {
        sb.append(CHARACTERS[(n % 60).toInt()])
        n /= 60
    }
    return sb.reverse().toString()
}

/**
 * Converts a NewBase60-encoded number into a decimal number. Will try to correct the most obvious typos.
 * @param sexagesimal the NewBase60-encoded number to convert.
 * @return the decimal number. Empty sexagesimals will evaluate to 0.
 * @throws ParseException if the sexagesimal contains invalid characters.
 */
@Throws(ParseException::class)
fun sexagesimalToNumber(sexagesimal: String): Long =
    sexagesimal.foldIndexed(initial = 0L) { index: Int, number: Long, char: Char ->
        var charCode: Int = char.toInt()
        when (charCode) {
            in 48..57 -> charCode -= 48   // 0-9
            in 65..72 -> charCode -= 55   // A-H
            73, 108 -> charCode = 1       // I, l -> 1
            in 74..78 -> charCode -= 56   // J-N
            79 -> charCode = 0            // O -> 0
            in 80..90 -> charCode -= 57   // P-Z
            95, 45 -> charCode = 34       // _, - -> _
            in 97..107 -> charCode -= 62  // a-k
            in 109..122 -> charCode -= 63 // m-z
            else -> throw ParseException("Couldn't parse the character \"$char\"", index)
        }
        number * 60 + charCode
    }
