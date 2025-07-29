package com.ataglance.walletglance.categoryCollection.domain.model

enum class CategoryCollectionType {
    Expense, Income, Mixed;


    fun asChar(): Char {
        return when (this) {
            Expense -> '-'
            Income -> '+'
            Mixed -> 'm'
        }
    }

    fun toggle(): CategoryCollectionType {
        return when (this) {
            Expense -> Income
            Income -> Mixed
            Mixed -> Expense
        }
    }

    fun toggleExpenseIncome(): CategoryCollectionType {
        return when (this) {
            Expense -> Income
            else -> Expense
        }
    }


    companion object {

        fun fromChar(char: Char): CategoryCollectionType {
            return when (char) {
                '-' -> Expense
                '+' -> Income
                else -> Mixed
            }
        }

    }

}