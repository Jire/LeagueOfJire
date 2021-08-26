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

package com.leagueofjire.core.game.unit

enum class UnitTag(val id: Int) {
	
	Unit_(1),
	Unit_Champion(2),
	Unit_Champion_Clone(3),
	Unit_IsolationNonImpacting(4),
	Unit_KingPoro(5),
	Unit_Minion(6),
	Unit_Minion_Lane(7),
	Unit_Minion_Lane_Melee(8),
	Unit_Minion_Lane_Ranged(9),
	Unit_Minion_Lane_Siege(10),
	Unit_Minion_Lane_Super(11),
	Unit_Minion_Summon(12),
	Unit_Minion_SummonName_game_character_displayname_ZyraSeed(13),
	Unit_Minion_Summon_Large(14),
	Unit_Monster(15),
	Unit_Monster_Blue(16),
	Unit_Monster_Buff(17),
	Unit_Monster_Camp(18),
	Unit_Monster_Crab(19),
	Unit_Monster_Dragon(20),
	Unit_Monster_Epic(21),
	Unit_Monster_Gromp(22),
	Unit_Monster_Krug(23),
	Unit_Monster_Large(24),
	Unit_Monster_Medium(25),
	Unit_Monster_Raptor(26),
	Unit_Monster_Red(27),
	Unit_Monster_Wolf(28),
	Unit_Plant(29),
	Unit_Special(30),
	Unit_Special_AzirR(31),
	Unit_Special_AzirW(32),
	Unit_Special_CorkiBomb(33),
	Unit_Special_EpicMonsterIgnores(34),
	Unit_Special_KPMinion(35),
	Unit_Special_MonsterIgnores(36),
	Unit_Special_Peaceful(37),
	Unit_Special_SyndraSphere(38),
	Unit_Special_TeleportTarget(39),
	Unit_Special_Trap(40),
	Unit_Special_Tunnel(41),
	Unit_Special_TurretIgnores(42),
	Unit_Special_UntargetableBySpells(43),
	Unit_Special_Void(44),
	Unit_Special_YorickW(45),
	Unit_Structure(46),
	Unit_Structure_Inhibitor(47),
	Unit_Structure_Nexus(48),
	Unit_Structure_Turret(49),
	Unit_Structure_Turret_Inhib(50),
	Unit_Structure_Turret_Inner(51),
	Unit_Structure_Turret_Nexus(52),
	Unit_Structure_Turret_Outer(53),
	Unit_Structure_Turret_Shrine(54),
	Unit_Ward(55);
	
}