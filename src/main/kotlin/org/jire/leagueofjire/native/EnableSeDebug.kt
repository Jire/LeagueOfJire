package org.jire.leagueofjire.native

import com.sun.jna.Native
import com.sun.jna.platform.win32.Advapi32
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.WinDef.DWORD
import com.sun.jna.platform.win32.WinNT.*

object EnableSeDebug {
	
	fun enableDebugPrivilege(): Boolean {
		val hToken = HANDLEByReference()
		var success = Advapi32.INSTANCE.OpenProcessToken(
			Kernel32.INSTANCE.GetCurrentProcess(),
			TOKEN_QUERY or TOKEN_ADJUST_PRIVILEGES, hToken
		)
		if (!success) {
			//LOG.error("OpenProcessToken failed. Error: {}", Native.getLastError())
			return false
		}
		try {
			val luid = LUID()
			success = Advapi32.INSTANCE.LookupPrivilegeValue(null, SE_DEBUG_NAME, luid)
			if (!success) {
				//LOG.error("LookupPrivilegeValue failed. Error: {}", Native.getLastError())
				return false
			}
			val tkp = TOKEN_PRIVILEGES(1)
			tkp.Privileges[0] = LUID_AND_ATTRIBUTES(luid, DWORD(SE_PRIVILEGE_ENABLED.toLong()))
			success = Advapi32.INSTANCE.AdjustTokenPrivileges(hToken.value, false, tkp, 0, null, null)
			val err: Int = Native.getLastError()
			if (!success) {
				//LOG.error("AdjustTokenPrivileges failed. Error: {}", err)
				return false
			} else if (err == ERROR_NOT_ALL_ASSIGNED) {
				//LOG.debug("Debug privileges not enabled.")
				return false
			}
		} finally {
			Kernel32.INSTANCE.CloseHandle(hToken.value)
		}
		return true
	}
	
}