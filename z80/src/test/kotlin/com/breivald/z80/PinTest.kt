package com.breivald.z80

import com.breivald.hardware.Pin
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

class PinTest {
  @Test
  fun active_noActions() {
    val pin = Pin.input()

    assertFalse(pin.active)

    pin.active = true
    assertTrue(pin.active)

    pin.active = false
    assertFalse(pin.active)
  }

  @Test
  fun active_triggerOnRisingAction() {
    val i = AtomicInteger(0)
    val pin = Pin.input(onActivation = { i.incrementAndGet() })

    assertEquals(0, i.get())

    pin.active = true

    assertEquals(1, i.get())
  }

  @Test
  fun active_dontTriggerOnRisingActionIfNoChange() {
    val i = AtomicInteger(0)
    val pin = Pin.input(onActivation = { i.incrementAndGet() })
    pin.active = true

    assertEquals(1, i.get())

    pin.active = true

    assertEquals(1, i.get())
  }

  @Test
  fun active_triggerOnFallingAction() {
    val i = AtomicInteger(0)
    val pin = Pin.input(onDeactivation = { i.incrementAndGet() })
    pin.active = true

    assertEquals(0, i.get())

    pin.active = false

    assertEquals(1, i.get())
  }

  @Test
  fun active_dontTriggerOnFallingActionIfNoChange() {
    val i = AtomicInteger(0)
    var pin by Pin.input(onDeactivation = { i.incrementAndGet() })

    assertEquals(0, i.get())

    pin = false

    assertEquals(0, i.get())
  }
}

class InputOutputPinsTest {

  @Test
  fun connectedPinsPropagateSignal() {
    val falling = AtomicInteger(0)
    val rising = AtomicInteger(0)
    var input = Pin.input(
      onDeactivation = { falling.incrementAndGet() },
      onActivation = { rising.incrementAndGet() })
    val output = Pin.output()

    input.connect(output)

    output.active = true
    assertEquals(1, rising.get())
    assertEquals(0, falling.get())

    output.active = false
    assertEquals(1, falling.get())
    assertEquals(1, rising.get())
  }

  @Test
  fun allConnectedPinsGetTheSignal() {
    val pin1Count = AtomicInteger(0)
    val pin2Count = AtomicInteger(0)
    val input1 = Pin.input(onActivation = { pin1Count.incrementAndGet() })
    val input2 = Pin.input(onActivation = { pin2Count.incrementAndGet() })
    val output = Pin.output()
      .connect(input1)
      .connect(input2)

    output.active = true
    assertEquals(1, pin1Count.get())
    assertEquals(1, pin2Count.get())
  }

  @Test
  fun inout() {
    val i = AtomicInteger(0)
    val io = AtomicInteger(0)
    val input = Pin.input (onActivation = { i.incrementAndGet() })
    val output = Pin.output()
    val inout = Pin.inout (onActivation = { io.incrementAndGet() })

    input.connect(inout)
    output.connect(input)
    inout.connect(output)

    output.active = true
    assertTrue(input.active)
    assertTrue(inout.active)
    assertEquals(1, i.get())
    assertEquals(1, io.get())

    inout.active = false
    inout.active = true
    assertTrue(input.active)
    assertTrue(inout.active)
    assertEquals(2, i.get())
    assertEquals(2, io.get())
  }
}