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

val toggleKey = KeyEvent.VK_BACK_SPACE

var lastToggle = -1F
var enabled = false

render {
	val time = time.seconds
	if (time - lastToggle >= 0.3F && keyboard.pressed(toggleKey)) {
		lastToggle = time
		enabled = !enabled
	}
	
	val color = if (enabled) Color.GREEN else Color.RED
	shapes.color = color
	val y = renderer.height / 2
	shapes.rect(2F, y - 2F, 72F, 18F)
	font.color = color
	font.text("AutoSmite", 5, y)
}

eachUnit {
	if (!me.isAlive) return@eachUnit
	
	val smite = me.spell(SMITE) ?: return@eachUnit
	if (!enabled || !isVisible || !isAlive || !isImportantJungle) return@eachUnit
	
	if (health > smite.value || !smite.canCast(time)/* || !withinDistance(player, smite.info.castRange)*/) return@eachUnit
	
	mouse.move(renderer[this]) {
		keyboard.pressAndRelease(KeyEvent.VK_F)
	}
}