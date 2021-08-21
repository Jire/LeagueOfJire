package org.jire.leagueofjire.native

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.*
import com.sun.jna.ptr.IntByReference
import org.jire.kna.attach.Attach
import org.jire.kna.attach.AttachAccess
import org.jire.kna.attach.windows.WindowsAttachAccess
import org.jire.kna.attach.windows.WindowsAttachedModule
import org.jire.kna.attach.windows.WindowsAttachedProcess

object LeagueOfLegendsHook {
	
	fun hook(): LeagueOfLegends {
		if (!EnableSeDebug.enableDebugPrivilege())
			throw Win32Exception(Native.getLastError())
		
		val hWindow = User32.INSTANCE.FindWindow("RiotWindowClass", null)
			?: throw Win32Exception(Native.getLastError())
		
		val pidPointer = IntByReference()
		User32.INSTANCE.GetWindowThreadProcessId(hWindow, pidPointer)
		val pid = pidPointer.value
		
		/*val hProcess = Kernel32.INSTANCE.OpenProcess(WinNT.PROCESS_ALL_ACCESS, false, pid)
			?: throw Win32Exception(Native.getLastError())*/
		val process = Attach.byID(pid, WindowsAttachAccess.All) {
			set(WindowsAttachedProcess.KERNEL_32_READS, true)
			set(WindowsAttachedProcess.KERNEL_32_WRITES, true)
		} as WindowsAttachedProcess
		
		val hMods = arrayOfNulls<WinDef.HMODULE>(1024)
		val cbNeeded = IntByReference()
		if (!Psapi.INSTANCE.EnumProcessModules(process.handle, hMods, hMods.size, cbNeeded))
			throw Win32Exception(Native.getLastError())
		
		val baseModule = hMods[0]!!
		val baseAddress = Pointer.nativeValue(baseModule.pointer)
		
		return LeagueOfLegends(process, baseModule, baseAddress)
	}
	
}