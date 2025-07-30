package com.ataglance.walletglance.budget.domain.model

import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.core.utils.toTimestampRange

data class GroupedBudgets(
    val daily: List<BudgetWithIds>,
    val weekly: List<BudgetWithIds>,
    val monthly: List<BudgetWithIds>,
    val yearly: List<BudgetWithIds>
) {

    companion object {

        fun fromBudgets(budgets: List<BudgetWithIds>): GroupedBudgets {
            val groupedBudgets = budgets.groupBy { it.repeatingPeriod }

            return GroupedBudgets(
                daily = groupedBudgets[RepeatingPeriod.Daily].orEmpty(),
                weekly = groupedBudgets[RepeatingPeriod.Weekly].orEmpty(),
                monthly = groupedBudgets[RepeatingPeriod.Monthly].orEmpty(),
                yearly = groupedBudgets[RepeatingPeriod.Yearly].orEmpty()
            )
        }

    }


    fun getMaxDateRange(): TimestampRange? {
        val repeatingPeriod = yearly.firstOrNull()?.repeatingPeriod
            ?: monthly.firstOrNull()?.repeatingPeriod
            ?: weekly.firstOrNull()?.repeatingPeriod
            ?: daily.firstOrNull()?.repeatingPeriod

        return repeatingPeriod?.toTimestampRange()
    }

    fun getByType(type: RepeatingPeriod): List<BudgetWithIds> {
        return when (type) {
            RepeatingPeriod.Daily -> daily
            RepeatingPeriod.Weekly -> weekly
            RepeatingPeriod.Monthly -> monthly
            RepeatingPeriod.Yearly -> yearly
        }
    }

}
