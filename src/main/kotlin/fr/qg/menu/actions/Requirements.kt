package fr.qg.menu.actions

import com.ezylang.evalex.Expression
import com.ezylang.evalex.data.EvaluationValue
import org.bukkit.entity.Player


interface MenuRequirement {
    fun test(player: Player) : Boolean
}

class EvalExRequirement(val placeholder: String) :  MenuRequirement {

    override fun test(player: Player): Boolean {
        val expression = Expression(placeholder)
        val result: EvaluationValue = expression.evaluate()
        return result.booleanValue
    }
}

class NotRequirement(private val inner: MenuRequirement) : MenuRequirement {
    override fun test(player: Player): Boolean = !inner.test(player)
}

class PermissionRequirement(val permission: String) : MenuRequirement {
    override fun test(player: Player): Boolean = player.hasPermission(permission)
}

