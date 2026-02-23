package com.breivald.hardware.zilog

import com.breivald.hardware.Bus
import com.breivald.hardware.Pin
import com.breivald.hardware.Pin.Companion.inout
import com.breivald.hardware.Pin.Companion.input
import com.breivald.hardware.Pin.Companion.output
import com.breivald.hardware.Register

class Z80PIO {
  val D0 = inout()
  val D1 = inout()
  val D2 = inout()
  val D3 = inout()
  val D4 = inout()
  val D5 = inout()
  val D6 = inout()
  val D7 = inout()
  val PA0 = inout()
  val PA1 = inout()
  val PA2 = inout()
  val PA3 = inout()
  val PA4 = inout()
  val PA5 = inout()
  val PA6 = inout()
  val PA7 = inout()
  val PB0 = inout()
  val PB1 = inout()
  val PB2 = inout()
  val PB3 = inout()
  val PB4 = inout()
  val PB5 = inout()
  val PB6 = inout()
  val PB7 = inout()
  val CLK = input()

  val BA = input()

  val ARDY = output()
  val ASTB = input()

  val BRDY = output()
  val BSTB = input()

  val CD = input()
  val CE = input()

  val IEI = input()
  val IEO = input()
  val INT = input()
  val IORQ = input()

  val M1 = input()
  val RD = input()

  val modeControlRegister = Register(2)
  val maskControlRegister = Register(2)
  val maskRegister = Register(8)
  val ioSelectRegister = Register(8)
  val dataOutputRegister = Register(8)
  val dataInputRegister = Register(8)

  val ioBus = Bus(8)
}