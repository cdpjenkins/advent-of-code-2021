package day24

fun doOneCell(input: Int, p1: Int, p2: Int, p3: Int, z: Int): Int =
    if (z % 26 + p2 != input) {
        z / p1 * 26 + input + p3
    } else {
        z / p1
    }

fun makeCell(p1: Int, p2: Int, p3: Int) = { input: Int, z: Int -> doOneCell(input, p1, p2, p3, z) }

val cells = listOf(
    makeCell(1, 10, 12),
    makeCell(1, 10, 10),
    makeCell(1, 12, 8),
    makeCell(1, 11, 6),
    makeCell(26, 0, 3),
    makeCell(1, 13, 6),
    makeCell(26, -12, 13),
    makeCell(26, -15, 8),
    makeCell(26, -15, 1),
    makeCell(26, -4, 7),
    makeCell(1, 10, 6),
    makeCell(26, -5, 9),
    makeCell(26, -12, 9),
)

fun calculateTheThing(modelNumber: String): Int {
    var result = 0

    for ((i, cell) in cells.withIndex()) {
        val input = modelNumber[i].toString().toInt()
        result = cell(input, result)
    }

    return result
}

fun main() {
    val startTime = System.currentTimeMillis()

    for (i in 0..9999999999) {
        val digits = i.toString().padStart(10, '0')
        val modelNumber = "999999${digits}"
        if (modelNumber.contains("0")) {
            continue
        }
        val result = calculateTheThing(modelNumber)


        if (result == 0) {
            println("$modelNumber $result")
            break
        }
    }

    val timeTaken = System.currentTimeMillis() - startTime
    println("Time taken: $timeTaken")
}
