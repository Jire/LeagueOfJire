package com.leagueofjire.native

import com.sun.jna.Native
import com.sun.jna.NativeLibrary

abstract class DirectNativeLib(final override val libraryName: String) : NativeLib {
	
	init {
		Native.register(javaClass, NativeLibrary.getInstance(libraryName))
	}
	
}