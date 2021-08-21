package org.jire.leagueofjire

data class UnitData(
	val name: String, val healthBarHeight: Float,
	val baseMoveSpeed: Float,
	val attackRange: Float,
	val attackSpeed: Float,
	val attackSpeedRatio: Float,
	val acquisitionRange: Float,
	val selectionRadius: Float,
	val pathingRadius: Float,
	val gameplayRadius: Float,
	val basicAtkMissileSpeed: Float,
	val basicAtkWindup: Float,
	val tags: Array<String>
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		
		other as UnitData
		
		if (name != other.name) return false
		if (healthBarHeight != other.healthBarHeight) return false
		if (baseMoveSpeed != other.baseMoveSpeed) return false
		if (attackRange != other.attackRange) return false
		if (attackSpeed != other.attackSpeed) return false
		if (attackSpeedRatio != other.attackSpeedRatio) return false
		if (acquisitionRange != other.acquisitionRange) return false
		if (selectionRadius != other.selectionRadius) return false
		if (pathingRadius != other.pathingRadius) return false
		if (gameplayRadius != other.gameplayRadius) return false
		if (basicAtkMissileSpeed != other.basicAtkMissileSpeed) return false
		if (basicAtkWindup != other.basicAtkWindup) return false
		if (!tags.contentEquals(other.tags)) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = name.hashCode()
		result = 31 * result + healthBarHeight.hashCode()
		result = 31 * result + baseMoveSpeed.hashCode()
		result = 31 * result + attackRange.hashCode()
		result = 31 * result + attackSpeed.hashCode()
		result = 31 * result + attackSpeedRatio.hashCode()
		result = 31 * result + acquisitionRange.hashCode()
		result = 31 * result + selectionRadius.hashCode()
		result = 31 * result + pathingRadius.hashCode()
		result = 31 * result + gameplayRadius.hashCode()
		result = 31 * result + basicAtkMissileSpeed.hashCode()
		result = 31 * result + basicAtkWindup.hashCode()
		result = 31 * result + tags.contentHashCode()
		return result
	}
}
