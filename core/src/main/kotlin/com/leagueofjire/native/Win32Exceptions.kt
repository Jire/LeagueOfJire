package com.leagueofjire.native

import com.sun.jna.Native
import com.sun.jna.platform.win32.Win32Exception

object Win32Exceptions {
	
	fun throwLastError(): Nothing = throw Win32Exception(Native.getLastError())
	
}