package com.leagueofjire.core.offsets

object Offsets {
	
	const val GameTime = 0x30d2c58L
	const val ObjectManager = 0x183E1A0L
	const val LocalPlayer = 0x30DA914L
	const val UnderMouseObject = 0x180f208L
	const val ZoomClass = 0x30d1ac8L
	const val Chat = 0x30daf74L
	const val ViewProjMatrices = 0x30FF708L
	const val Renderer = 0x310257CL
	const val MinimapObject = 0x30f9a64L
	
	const val ObjIndex = 0x20L
	const val ObjTeam = 0x4CL
	const val ObjMissileName = 0x6CL
	const val ObjNetworkID = 0xCCL
	const val ObjPos = 0x1d8L
	const val ObjMissileSpellCast = 0x250L
	const val ObjVisibility = 0x270L
	const val ObjSpawnCount = 0x218L
	const val ObjSrcIndex = 0x290L
	const val ObjMana = 0x298L
	const val ObjMaxMana = 0x2A8L
	const val ObjRecallState = 0xD8CL
	const val ObjHealth = 0xD98L
	const val ObjMaxHealth = 0xDA8L
	const val ObjAbilityHaste = 0x10F4L
	const val ObjLethality = 0x11DCL
	const val ObjArmor = 0x12CCL
	const val ObjBonusArmor = 0x12C8L
	const val ObjMagicRes = 0x12D4L
	const val ObjBonusMagicRes = 0x12D4L
	const val ObjBaseAtk = 0x12A4L
	const val ObjBonusAtk = 0x121CL
	const val ObjMoveSpeed = 0x12E4L
	const val ObjSpellBook = 0x27C8L
	const val ObjTransformation = 0x3040L
	const val ObjName = 0x2BB4L
	const val ObjLvl = 0x3354L
	const val ObjExpiry = 0x298L
	const val ObjCrit = 0x12C8L
	const val ObjCritMulti = 0x12B4L
	const val ObjAbilityPower = 0x122CL
	const val ObjAtkSpeedMulti = 0x12A0L
	const val ObjAtkRange = 0x12ECL
	const val ObjTargetable = 0xD00L
	const val ObjInvulnerable = 0x3D0L
	const val ObjIsMoving = 0x3638L
	const val ObjDirection = 0x1B88L
	const val ObjItemList = 0x3430L
	const val ObjExpierience = 0x3344L
	const val ObjMagicPen = 0x11C0L
	const val ObjMagicPenMulti = 0x11C8L
	const val ObjAdditionalApMulti = 0x122CL
	const val ObjManaRegen = 0x1134L
	const val ObjHealthRegen = 0x12D8L
	
	const val MaxZoom = 0x20L
	
	const val ChatIsOpen = 0x0684L
	
	const val SpellBookActiveSpellCast = 0x20L
	const val SpellBookSpellSlots = 0x478L
	
	const val ObjBuffManager = 0x21A4L
	const val BuffManagerEntriesArray = 0x10L
	const val BuffEntryBuff = 0x8L
	const val BuffType = 0x4L
	const val BuffEntryBuffStartTime = 0xCL
	const val BuffEntryBuffEndTime = 0x10L
	const val BuffEntryBuffCount = 0x74L
	const val BuffEntryBuffCountAlt = 0x24L
	const val BuffEntryBuffCountAlt2 = 0x20L
	const val BuffName = 0x8L
	const val BuffEntryBuffNodeStart = 0x20L
	const val BuffEntryBuffNodeCurrent = 0x24L
	
	const val ItemListItem = 0xCL
	const val ItemInfo = 0x20L
	const val ItemInfoId = 0x68L
	
	const val CurrentDashSpeed = 0x1D0L
	const val IsDashing = 0x398L
	const val DashPos = 0x1FCL
	const val IsMoving = 0x198L
	const val NavBegin = 0x1BCL
	const val NavEnd = 0x1C0L
	
	const val RendererWidth = 0xCL
	const val RendererHeight = 0x10L
	
	const val SpellSlotLevel = 0x20L
	const val SpellSlotTime = 0x28L
	const val SpellSlotCharges = 0x58L
	const val SpellSlotTimeCharge = 0x78L
	const val SpellSlotDamage = 0x94L
	const val SpellSlotSpellInfo = 0x13CL
	const val SpellInfoSpellData = 0x44L
	const val SpellDataSpellName = 0x6CL
	const val SpellDataMissileName = 0x6CL
	const val SpellSlotSmiteTimer = 0x64L
	const val SpellSlotSmiteCharges = 0x58L
	
	const val ObjectMapCount = 0x2CL
	const val ObjectMapRoot = 0x28L
	const val ObjectMapNodeNetId = 0x10L
	const val ObjectMapNodeObject = 0x14L

	const val SpellCastSpellInfo = 0x8L
	const val SpellCastStartTime = 0x544L
	const val SpellCastStartTimeAlt = 0x534L
	const val SpellCastCastTime = 0x4C0L
	const val SpellCastStart = 0x80L
	const val SpellCastEnd = 0x8CL
	const val SpellCastSrcIdx = 0x68L
	const val SpellCastDestIdx = 0xC0L
	
	const val MissileSpellInfo = 0x258L
	const val MissileSrcIdx = 0x2B8L
	const val MissileDestIdx = 0x310L
	const val MissileStartPos = 0x2D0L
	const val MissileEndPos = 0x2DCL
	
	const val MinimapObjectHud = 0x14L
	const val MinimapHudPos = 0x44L
	const val MinimapHudSize = 0x4CL
	
	const val AiManagerStartPath = 0x1bcL
	const val AiManagerEndPath = 0x1c0L
	const val AiManagerTargetPosition = 0x10L
	const val AiManagerIsMoving = 0x198L
	const val AiManagerIsDashing = 0x398L
	const val AiManagerCurrentSegment = 0x19CL
	const val AiManagerDashSpeed = 0x1D0L
	
}