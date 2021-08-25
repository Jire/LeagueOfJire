package com.leagueofjire.core.native

import com.sun.jna.platform.win32.Advapi32.INSTANCE as Advapi32
import com.sun.jna.platform.win32.Kernel32.INSTANCE as Kernel32
import com.sun.jna.platform.win32.WinDef.DWORD
import com.sun.jna.platform.win32.WinNT.*

object DebugPrivileges {
	
	fun enableDebugPrivilege(): Boolean {
		val token = HANDLEByReference()
		if (!Advapi32.OpenProcessToken(Kernel32.GetCurrentProcess(), TOKEN_QUERY or TOKEN_ADJUST_PRIVILEGES, token))
			return false
		try {
			val luid = LUID()
			if (!Advapi32.LookupPrivilegeValue(null, SE_DEBUG_NAME, luid)) return false
			
			val privileges = TOKEN_PRIVILEGES(1).apply {
				Privileges[0] = LUID_AND_ATTRIBUTES(luid, DWORD(SE_PRIVILEGE_ENABLED.toLong()))
			}
			if (!Advapi32.AdjustTokenPrivileges(token.value, false, privileges, 0, null, null))
				return false
		} finally {
			Kernel32.CloseHandle(token.value)
		}
		return true
	}
	
}