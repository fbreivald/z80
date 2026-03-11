import com.breivald.hardware.Bus
import com.breivald.z80.Clock
import com.breivald.z80.Z80

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    val clock = Clock()
    val dataBus = Bus(8)
    val addressBus = Bus(16)
    val cpu = Z80(clock, dataBus, addressBus)
}