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

package com.leagueofjire.core.offsets

object Offsets {
    //grassOffset = 8B 46 04 FF B0 ? ? ? ?
    //drawCircle = 81 EC 84 00 00 00 A1 ?? ?? ?? ?? 33 C4 89 84 24 80 00 00 00 F3
    //zoomCheatDetection = 38 86 7D 02 00 00
    //objIsNotMoving = 0x34E9
    //AtkRange = D9 83 ? ? ? ? 5F 5E

    /*
    0x314AFE0 = resolution width
0x314AFE0 + 0x4 = height
0x314AFE0 + 0x8 = true is focus, it works with imgui open, unlike the hudinstance one, no need to read bytes here. simple
     */
    /*
    For each missile in missile manager:

- At 0x14 you get a pointer to missile-data
- At 0x34 inside missilie-data u get the team (int16)
- At 0x54 a text8pointer to missile name (some don't have)
- At 0x260 in missile-data u get missile-data-info with more information about spell
- At 02E0 - start postion
- At 02EC - end postion 1
- At 02F8 - end postion 2
- At 02C4 you get the index of the source (int16) for example the player or a monster.
     */

    const val GameTime = 0x30d2c58L
    const val ObjectManager = 0x18ADAD8L
    const val LocalPlayer = 0x314A404L
    const val UnderMouseObject = 0x24FB364L
    const val ZoomClass = 0x3143104L
    const val Chat = 0x314B094L
    const val ViewProjMatrices = 0x03170CF8L
    const val Renderer = 0x31765C0L
    const val MinimapObject = 0x3143C88L

    const val Characterdata = 0x2D8CL

    const val ObjIndex = 0x8L
    const val ObjTeam = 0x34L
    const val ObjMissileName = 0x54L
    const val ObjNetworkID = 0xB4L
    const val ObjPos = 0x1DCL
    const val ObjMissileSpellCast = 0x250L
    const val ObjVisibility = 0x274L
    const val ObjSpawnCount = 0x288L
    const val ObjSrcIndex = 0x294L
    const val ObjMana = 0x029CL
    const val ObjMaxMana = 0x2ACL
    const val ObjRecallState = 0x0D90L
    const val ObjHealth = 0xE7CL
    const val ObjMaxHealth = 0xE8CL
    const val ObjAbilityHaste = 0x16A8L
    const val ObjLethality = 0x1294L
    const val ObjArmor = 0x1384L
    const val ObjBonusArmor = 0x1388L
    const val ObjMagicRes = 0x138CL
    const val ObjBonusMagicRes = 0x1390L
    const val ObjBaseAtk = 0x135CL
    const val ObjBonusAtk = 0x12D4L
    const val ObjMoveSpeed = 0x139CL
    const val ObjSpellBook = 0x29C8L
    const val ObjTransformation = 0x3070L
    const val ObjName = 0x2DB4L
    const val ObjLvl = 0x35A4L
    const val ObjExpiry = 0x298L
    const val ObjCrit = 0x1858L
    const val ObjCritMulti = 0x12B8L
    const val ObjAbilityPower = 0x1758L
    const val ObjAtkSpeedMulti = 0x1358L
    const val ObjAtkRange = 0x13A4L
    const val ObjTargetable = 0xD04L
    const val ObjInvulnerable = 0x3D4L
    const val ObjIsMoving = 0x34E9L
    const val ObjDirection = 0x1BE8L
    const val ObjItemList = 0x35F0L
    const val ObjExpierience = 0x337CL
    const val ObjMagicPen = 0x1278L
    const val ObjMagicPenMulti = 0x1280L
    const val ObjAdditionalApMulti = 0x12E4L
    const val ObjManaRegen = 0x11E0L
    const val ObjHealthRegen = 0x1390L
    const val OisDead = 0x218L

    const val MaxZoom = 0x20L

    const val ChatIsOpen = 0x6BCL

    const val SpellBookActiveSpellCast = 0x20L
    const val SpellBookSpellSlots = 0x478L

    const val ObjBuffManager = 0x2338L
    const val BuffManagerEntriesArray = 0x10L
    const val BuffEntryBuff = 0x8L
    const val BuffType = 0x4L
    const val BuffEntryBuffStartTime = 0xCL
    const val BuffEntryBuffEndTime = 0x10L
    const val BuffEntryBuffCount = 0x74
    const val BuffEntryBuffCountAlt = 0x24
    const val BuffName = 0x4
    const val BuffEntryBuffNodeStart = 0x20
    const val BuffEntryBuffNodeCurrent = 0x24

    const val ItemListItem = 0xCL
    const val ItemInfo = 0x20L
    const val ItemInfoId = 0x68L

    const val CurrentDashSpeed = 0x1D0L
    const val IsDashing = 0x398L
    const val DashPos = 0x1FCL
    const val IsMoving = 0x198L
    const val NavBegin = 0x1BCL
    const val NavEnd = 0x1D8

    const val RendererWidth = 0x8L
    const val RendererHeight = 0xCL

    const val SpellSlotLevel = 0x1CL
    const val SpellSlotTime = 0x24
    const val SpellSlotCharges = 0x54
    const val SpellSlotTimeCharge = 0x74
    const val SpellSlotDamage = 0x94
    const val SpellSlotSpellInfo = 0x120
    const val SpellInfoSpellData = 0x40
    const val SpellDataSpellName = 0x6C
    const val SpellDataMissileName = 0x6C
    const val SpellSlotSmiteTimer = 0x60
    const val SpellSlotSmiteCharges = 0x54

    const val ObjectMapCount = 0x2CL
    const val ObjectMapRoot = 0x28L
    const val ObjectMapNodeNetId = 0x10L
    const val ObjectMapNodeObject = 0x14L

    const val SpellCastSpellInfo = 0x8
    const val SpellCastStartTime = 0x41
    const val SpellCastStartTimeAlt = 0x534
    const val SpellCastCastTime = 0x4C0
    const val SpellCastStart = 0x84
    const val SpellCastEnd = 0x90
    const val SpellCastSrcIdx = 0x68
    const val SpellCastDestIdx = 0xC0

    const val MissileSpellInfo = 0x0260
    const val MissileSrcIdx = 0x2C4
    const val MissileDestIdx = 0x314
    const val MissileStartPos = 0x2E0
    const val MissileEndPos = 0x2EC

    const val MinimapObjectHud = 0x15CL
    const val MinimapHudPos = 0x3CL
    const val MinimapHudSize = 0x44L

    const val AiManager = 0x2E84L
    const val AiManagerStartPath = 0x1e4L
    const val AiManagerEndPath = 0x1e8L
    const val AiManagerTargetPosition = 0x10L
    const val AiManagerIsMoving = 0x1c0L
    const val AiManagerIsDashing = 0x3c0L
    const val AiManagerCurrentSegment = 0x1c4L
    const val AiManagerDashSpeed = 0x1f8L

    const val attackSpeedModTotal = 0x1358L
    const val attackDelayCastOffsetPercent = 0x280L
    const val attackSpeedMultiplier = 0x1334L
    const val baseAttackSpeed = 0x1D0L
    const val attackCastTime = 0x27CL

}
