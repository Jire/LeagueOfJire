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

package com.leagueofjire.core.input

import com.leagueofjire.input.KeyboardContext
import org.jire.kna.nativelib.windows.User32
import java.awt.Robot

class IKeyboardContext(val robot: Robot) : KeyboardContext {
	
	override fun state(keyCode: Int) = User32.GetKeyState(keyCode).toInt()
	
	override fun pressed(keyCode: Int) = state(keyCode) < 0
	
	override fun released(keyCode: Int) = !pressed(keyCode)
	
	override fun press(keyCode: Int) = robot.keyPress(keyCode)
	
	override fun release(keyCode: Int) = robot.keyRelease(keyCode)
	
}