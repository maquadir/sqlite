/*
 * Copyright (c) 2021 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.sqlitetodo.controller

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.raywenderlich.android.sqlitetodo.model.*
import com.raywenderlich.android.sqlitetodo.model.TodoDbSchema.DATABASE_NAME
import com.raywenderlich.android.sqlitetodo.model.TodoDbSchema.DATABASE_VERSION
import com.raywenderlich.android.sqlitetodo.model.TodoDbSchema.ToDoTable.Columns.KEY_TODO_ID
import com.raywenderlich.android.sqlitetodo.model.TodoDbSchema.ToDoTable.Columns.KEY_TODO_IS_COMPLETED
import com.raywenderlich.android.sqlitetodo.model.TodoDbSchema.ToDoTable.Columns.KEY_TODO_NAME
import com.raywenderlich.android.sqlitetodo.model.TodoDbSchema.ToDoTable.TABLE_NAME

class ToDoDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //Create a row in the table to store a joke
    fun createToDo(toDo: ToDo) {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues()
        values.put(KEY_TODO_NAME, toDo.toDoName)
        values.put(KEY_TODO_IS_COMPLETED, toDo.isCompleted)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun readToDos(): ArrayList<ToDo> {
        val db: SQLiteDatabase = readableDatabase
        val list = ArrayList<ToDo>()
        val selectAll = "SELECT * FROM $TABLE_NAME"
        val cursor: Cursor = db.rawQuery(selectAll, null)
        if (cursor.moveToFirst()) {
            do {
                val toDo = ToDo().apply {
                    toDoId = cursor.getLong(cursor.getColumnIndex(KEY_TODO_ID))
                    toDoName = cursor.getString(cursor.getColumnIndex(KEY_TODO_NAME))
                    isCompleted = cursor.getInt(cursor.getColumnIndex(KEY_TODO_IS_COMPLETED)) == 1
                }
                list.add(toDo)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun updateToDo(toDo: ToDo): Int {
        val todoId = toDo.toDoId.toString()
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues()
        values.put(KEY_TODO_NAME, toDo.toDoName)
        values.put(KEY_TODO_IS_COMPLETED, toDo.isCompleted)
        return db.update(TABLE_NAME, values, "$KEY_TODO_ID=?", arrayOf(todoId))
    }

    fun deleteToDo(id: Long) {
        val db: SQLiteDatabase = writableDatabase
        db.delete(TABLE_NAME, "$KEY_TODO_ID=?", arrayOf(id.toString()))
        db.close()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createToDoTable = """
  	CREATE TABLE $TABLE_NAME  (
    	$KEY_TODO_ID INTEGER PRIMARY KEY,
    	$KEY_TODO_NAME  TEXT,
    	$KEY_TODO_IS_COMPLETED  LONG );
	"""
        // 2
        db?.execSQL(createToDoTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}