package com.example.userrecord.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userrecord.R
import com.example.userrecord.adapter.ContactsAdapter
import com.example.userrecord.databinding.ActivityMainBinding
import com.example.userrecord.deleteAll.DeleteAllContactsFragment
import com.example.userrecord.ui.addfragment.AddContactFragment
import com.example.userrecord.utils.DataStatus
import com.example.userrecord.viewmodel.DatabaseViewModel
import com.example.userrecord.viewmodel.isVisible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject

    lateinit var contactsAdapter: ContactsAdapter
    private val viewModel: DatabaseViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    var selectedItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            //setSupportActionBar(mainToolbar)

            btnShowDialog.setOnClickListener {
                AddContactFragment().show(supportFragmentManager,AddContactFragment().tag)
            }

            viewModel.getAllContacts()
            viewModel.contactList.observe(this@MainActivity){
                when(it.status){
                    DataStatus.Status.LOADING ->{
                        loading.isVisible(true,rvContacts)
                        emptyBody.isVisible(false,rvContacts)
                    }
                    DataStatus.Status.SUCCESS ->{
                        it.isEmpty?.let {
                            showEmpty(it)
                        }
                        loading.isVisible(false,rvContacts)
                        contactsAdapter.differ.submitList(it.data)
                        rvContacts.apply {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            adapter = contactsAdapter
                        }
                    }
                    DataStatus.Status.ERROR ->{
                        loading.isVisible(false,rvContacts)
                        Toast.makeText(this@MainActivity,it.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }

            mainToolbar.setOnMenuItemClickListener{
                when(it.itemId){
                    R.id.actionDelete -> {
                        DeleteAllContactsFragment().show(supportFragmentManager,DeleteAllContactsFragment().tag)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.actionSort -> {
                        filter()
                        return@setOnMenuItemClickListener true
                    }
//                    R.id.actionSearch -> {
//
//                        return@setOnMenuItemClickListener true
//                    }

                    else -> {
                        return@setOnMenuItemClickListener false
                    }
                }
            }
        }
    }

    fun showEmpty(isShown : Boolean){
        binding.apply {
            if (isShown){
                emptyBody.isVisible(true,listBody)
            }else{
                emptyBody.isVisible(false,listBody)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        val search = menu?.findItem(R.id.actionSearch)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchContact(newText.orEmpty())
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun filter(){
        val builder = AlertDialog.Builder(this)
        val sortItem = arrayOf("Newer(Default","Name : A-Z","Name : Z-A")
        builder.setSingleChoiceItems(sortItem,selectedItem){dialog,item ->
            when(item){
                0-> viewModel.getAllContacts()
                1-> viewModel.sortedASC()
                2-> viewModel.sortedDESC()
            }
            selectedItem = item
            dialog.dismiss()
        }
        val alertDialog : AlertDialog = builder.create()
        alertDialog.show()
    }
}