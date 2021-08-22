package com.leagueofjire.game

import com.leagueofjire.native.DebugPrivileges
import com.leagueofjire.native.Win32Exceptions
import com.sun.jna.ptr.IntByReference
import org.jire.kna.attach.Attach
import org.jire.kna.attach.AttachedModule
import org.jire.kna.attach.AttachedProcess
import org.jire.kna.attach.windows.WindowsAttachAccess
import org.jire.kna.attach.windows.WindowsAttachedProcess
import com.sun.jna.platform.win32.User32.INSTANCE as User32

data class LeagueOfLegendsHook(val process: AttachedProcess, val baseModule: AttachedModule) {
	
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
				set(WindowsAttachedProcess.KERNEL_32_READS, true)
			} as WindowsAttachedProcess
			
			val modules = process.modules(true)
			val baseModule = modules.byName(baseModuleName)!!
			
			return LeagueOfLegendsHook(process, baseModule)
		}
		
	}
	
}