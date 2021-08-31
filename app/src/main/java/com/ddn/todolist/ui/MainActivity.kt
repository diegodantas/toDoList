package com.ddn.todolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ddn.todolist.databinding.ActivityMainBinding
import com.ddn.todolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        upDateList()

        insertListeners()

    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                upDateList()
                binding.rvTasks.adapter = adapter
            } else {
                binding.rvTasks.adapter = adapter
            }
        }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            resultLauncher.launch(intent)
        }
        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            resultLauncher.launch(intent)
        }
        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            resultLauncher.launch(intent)
            upDateList()
        }
    }

    private fun upDateList() {
        val list = TaskDataSource.getList()
        binding.includeEmpty.empytState.visibility = if (list.isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
        adapter.submitList(list)
    }
}