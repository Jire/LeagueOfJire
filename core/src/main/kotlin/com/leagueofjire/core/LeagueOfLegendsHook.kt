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

package com.leagueofjire.core

import com.leagueofjire.core.native.DebugPrivileges
import com.leagueofjire.core.native.Win32Exceptions
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.ptr.IntByReference
import org.jire.kna.attach.Attach
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.attach.windows.WindowsAttachAccess
import org.jire.kna.attach.windows.WindowsAttachedProcess
import com.sun.jna.platform.win32.User32.INSTANCE as User32

data class LeagueOfLegendsHook(
	val window: WinDef.HWND,
	val process: AttachedProcess,
	val baseModule: AttachedModule
) {
	
	companion object {
		
		private const val WINDOW_NAME = "RiotWindowClass"
		private const val BASE_MODULE_NAME = "League of Legends.exe"
		
		fun hook(windowName: String = WINDOW_NAME, baseModuleName: String = BASE_MODULE_NAME): LeagueOfLegendsHook {
			DebugPrivileges.enableDebugPrivilege()
			
			val window = User32.FindWindow(windowName, null) ?: Win32Exceptions.throwLastError()
			
			val pidPointer = IntByReference()
			User32.GetWindowThreadProcessId(window, pidPointer)
			val pid = pidPointer.value
			
			val process = Attach.byID(pid, WindowsAttachAccess.All) {
				set(WindowsAttachedProcess.KERNEL_32_READS, false)
				set(WindowsAttachedProcess.KERNEL_32_WRITES, false)
			} as WindowsAttachedProcess
			
			val modules = process.modules(true)
			val baseModule = modules.byName(baseModuleName)!!
			
			return LeagueOfLegendsHook(window, process, baseModule)
		}
		
	}
	
}