package com.breivald.z80

import com.breivald.hardware.*
import com.breivald.z80.InterruptMode.MODE_0
import com.breivald.unsigned.shr

class Z80(clock: Clock, val dataBus: Bus, val addressBus: Bus) {
  // Pins
  val A0 = Pin.inout()
  val A1 = Pin.inout()
  val A2 = Pin.inout()
  val A3 = Pin.inout()
  val A4 = Pin.inout()
  val A5 = Pin.inout()
  val A6 = Pin.inout()
  val A7 = Pin.inout()
  val A8 = Pin.inout()
  val A9 = Pin.inout()
  val A10 = Pin.inout()
  val A11 = Pin.inout()
  val A12 = Pin.inout()
  val A13 = Pin.inout()
  val A14 = Pin.inout()
  val A15 = Pin.inout()
  val addressPins = arrayOf(A0, A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15)
  val D0 = Pin.inout()
  val D1 = Pin.inout()
  val D2 = Pin.inout()
  val D3 = Pin.inout()
  val D4 = Pin.inout()
  val D5 = Pin.inout()
  val D6 = Pin.inout()
  val D7 = Pin.inout()
  val dataPins = arrayOf(D0, D1, D2, D3, D4, D5, D6, D7)
  val BUSACK = Pin.output()
  val BUSREQ = Pin.input()
  val HALT = Pin.output()
  val INT = Pin.input()
  val IORQ = Pin.output()
  val M1 = Pin.output()
  val MREQ = Pin.output()
  val NMI = Pin.input()
  val RD = Pin.output()
  val RESET = Pin.input()
  val RFSH = Pin.output()
  val WAIT = Pin.input()
  val WR = Pin.output()
  val CLK = Pin.input(
    onActivation = {
      endTCycle()
      T++
      startTCycle()
    },
    onDeactivation = {
      midTCycle()
    }
  )

  // Registers
  val A = Register(8)
  val F = Register(8)
  val B = Register(8)
  val C = Register(8)
  val D = Register(8)
  val E = Register(8)
  val H = Register(8)
  val L = Register(8)
  val A_ = Register(8)
  val F_ = Register(8)
  val B_ = Register(8)
  val C_ = Register(8)
  val D_ = Register(8)
  val E_ = Register(8)
  val H_ = Register(8)
  val L_ = Register(8)
  val AF = RegisterPair(A, F)
  val BC = RegisterPair(B, C)
  val DE = RegisterPair(D, E)
  val HL = RegisterPair(H, L)
  val AF_ = RegisterPair(A_, F_)
  val BC_ = RegisterPair(B_, C_)
  val DE_ = RegisterPair(D_, E_)
  val HL_ = RegisterPair(H_, L_)
  val IX = Register(16)
  val IY = Register(16)
  val SP = Register(16)
  val PC = Register(16)
  val I = Register(8)
  val R = Register(8)
  val IR = Register(8)
  val TMP = Register(8)

  var T = 0
  var M = 1

  var IFF1 = false
  var IFF2 = false

  var interruptMode = MODE_0

  var opcode: String? = null

  var cycle: MachineCycle? = null

  init {
    CLK.connect(clock.pin)
    dataBus.connect(*dataPins)
    addressBus.connect(*addressPins)
  }

  private fun initialize() {
    interruptMode = MODE_0
    IFF1 = false
    IFF2 = false
    I.value = 0U
    R.value = 0U
    PC.value = 0U
  }

  private fun startTCycle() {
    if (T == 1 && M == 1) {
      cycle = FetchInstruction()
    }

    cycle?.startTCycle(T, this)
  }

  private fun midTCycle() {

  }

  private fun endTCycle() {

  }
}

open class Clock {
  val pin = Pin.output();

  fun tick() {
    pin.active = true
    pin.active = false
  }
}

interface MachineCycle {
  fun startTCycle(t: Int, cpu: Z80)
  fun midTCycle(t: Int, cpu: Z80)
  fun endTCycle(t: Int, cpu:Z80)
}

open class FetchInstruction: MachineCycle {
//  val instruction = Instruction()

  override fun startTCycle(t: Int, cpu: Z80) {
    when (t) {
      1 -> {
        cpu.M1.active = true
        setPins(cpu.PC, *cpu.addressPins)
        cpu.PC.inc()
      }
      2 -> {

      }
      3 -> {
        cpu.MREQ.active = false
        cpu.RD.active = false
        cpu.M1.active = false
        setPins(cpu.R, *cpu.addressPins)
        cpu.RFSH.active = true
      }
      4 -> {

      }
    }
  }

  override fun midTCycle(t: Int, cpu: Z80) {
    when (t) {
      1 -> {
        cpu.MREQ.active = true
        cpu.RD.active = true
      }
      2 -> {
      }
    }
  }

  override fun endTCycle(t: Int, cpu: Z80) {
    when(t) {
      2 -> {
        cpu.IR.value = cpu.dataBus.toUInt()
      }
    }
  }
}

interface Instruction {
  var mnemonic: String
  var cycles: List<MachineCycle>
  var prefix: UByte?
  var opcode: UByte
  var source: Register
  var destination: Register
  var sourceAddressingMode: AddressingMode?
  var destinationAddressingMode: AddressingMode?
}

class Load: Instruction {
  override var mnemonic: String = "LD"
  override var cycles: List<MachineCycle> = emptyList()
  override var prefix: UByte? = null
  override var opcode: UByte = 0U
  override lateinit var source: Register
  override lateinit var destination: Register
  override var sourceAddressingMode: AddressingMode? = null
  override var destinationAddressingMode: AddressingMode? = null
}

open class MemoryReadCycle (
  val sourceAddressingMode: AddressingMode,
  val targetAddressingNode: AddressingMode
): MachineCycle {

  override fun startTCycle(t: Int, cpu: Z80) {

  }

  override fun midTCycle(t: Int, cpu: Z80) {
    TODO("Not yet implemented")
  }

  override fun endTCycle(t: Int, cpu: Z80) {
    TODO("Not yet implemented")
  }
}

private fun decode(opcode: UByte, instruction: Instruction) {
  when (opcode.toUInt()) {
    0xCD_U, 0xDD_U, 0xED_U, 0xFD_U -> {
      instruction.prefix = opcode
    }
    else -> {
      val (x, y, z) = parse(opcode)
      when (x.toUInt()) {
        0U -> {
          when (z.toUInt()) {
            0U -> {
              when (y.toUInt()) {
                0U -> {
                  instruction.mnemonic = "NOP"
                }
                1U -> {
                  with(instruction) {
                    mnemonic = "EX"
                    sourceAddressingMode = AddressingMode.REGISTER
                    destinationAddressingMode = AddressingMode.REGISTER
                    sourceAddressingMode
                  }
                }
              }
            }
            1U -> {}
            2U -> {}
            3U -> {}
            4U -> {}
            5U -> {}
            6U -> {}
            7U -> {}
          }
        }
        1U -> {}
        2U -> {}
        3U -> {}
      }
    }
  }
}

private fun parse(opcode: UByte): Triple<UByte, UByte, UByte> {
  val x = (opcode and 0b11000000_U) shr 6
  val y = (opcode and 0b00111000_U) shr 3
  val z = (opcode and 0b00000111_U)

  return Triple(x, y, z)
}

private fun setPins(reg: IRegister, vararg pins: Pin) {
  var i = 0
  for (bit in reg) {
    pins[i++].active = bit
  }
}

class ByteLoadInstruction(
  val addressingMode: AddressingMode
) {
  var sourceReg: Register? = null
  var destinationReg: Register? = null
}

enum class AddressingMode {
  IMMEDIATE,
  IMMEDIATE_EXTENDED,
  MODIFIED_PAGE_ZERO,
  RELATIVE,
  EXTENDED,
  INDEXED,
  REGISTER,
  IMPLIED,
  REGISTER_INDIRECT,
  BIT,
  NONE,
}

enum class InterruptMode {
  MODE_0,
  MODE_1,
  MODE_2,
}

enum class CpuState {
  HALT,
  WAIT,
  RUN,
}
