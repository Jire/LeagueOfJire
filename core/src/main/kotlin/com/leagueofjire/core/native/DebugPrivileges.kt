/*
 * LeagueOfJire: Free & Open-Source External Scripting Platform
 * Copyright (C) 2021  Thomas G. P. Nappo (Jire)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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