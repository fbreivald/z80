package com.breivald.hardware

import com.breivald.hardware.PinType.*
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

open class Pin(val type: PinType,
               protected var onActivation: (() -> Unit)? = null,
               protected var onDeactivation: (() -> Unit)? = null
) {
  var active by observable(false) {
    _, oldValue, newValue ->
      if (oldValue != newValue) {
        if (newValue)
          onActivation?.invoke()
        else
          onDeactivation?.invoke()
      }
      if (type == OUT || type == INOUT) {
        propagate()
      }
  }

  operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
    return active
  }

  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
    this.active = active
  }

  protected val connectedPins: MutableSet<Pin> = HashSet()

  protected fun propagate() {
    connectedPins.forEach {
      it.active = active
    }
  }

  fun connect(pin: Pin): Pin {
    connectedPins.add(pin)
    pin.connectedPins.add(this)

    return this
  }

  companion object {
    fun input(onActivation: (() -> Unit)? = null, onDeactivation: (() -> Unit)? = null): Pin {
      return Pin(IN, onActivation, onDeactivation)
    }
    fun output(): Pin {
      return Pin(OUT)
    }
    fun inout(onActivation: (() -> Unit)? = null, onDeactivation: (() -> Unit)? = null): Pin {
      return Pin(INOUT, onActivation, onDeactivation)
    }
  }

  override fun toString(): String {
    return "Pin(${type})=${active}"
  }
}

interface InputPin {
  fun connect(pin: OutputPin): InputPin
}

interface OutputPin {
  fun connect(pin: InputPin): OutputPin
}

enum class PinType {
  IN, OUT, INOUT
}
