package org.jire.leagueofjire.native

import com.sun.jna.platform.win32.WinNT
import org.jire.kna.attach.windows.WindowsAttachedProcess

class LeagueOfLegends(val process: WindowsAttachedProcess, val baseModule: WinNT.HANDLE, val baseAddress: Long)