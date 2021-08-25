package com.leagueofjire.app.transparency

import com.leagueofjire.core.native.DirectNativeLib

object TransparencyUser32 : DirectNativeLib("user32") {
	
	external fun SetWindowCompositionAttribute(hwnd: Long, data: WindowCompositionAttributeData): Long
	
}