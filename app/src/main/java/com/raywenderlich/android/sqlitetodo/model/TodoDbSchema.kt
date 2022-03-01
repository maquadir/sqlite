package com.raywenderlich.android.sqlitetodo.model

object TodoDbSchema {

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "todoitems.db"

    object ToDoTable {
        const val TABLE_NAME = "todoitems"

        object Columns {
            const val KEY_TODO_ID = "todoid"
            const val KEY_TODO_NAME = "todoname"
            const val KEY_TODO_IS_COMPLETED = "iscompleted"
        }
    }
}