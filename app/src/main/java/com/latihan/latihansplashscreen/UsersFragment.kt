package com.latihan.latihansplashscreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersFragment : Fragment() {

    private lateinit var rvUsers: RecyclerView
    private lateinit var btnNextPage: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var userAdapter: UserAdapter
    
    private val userList = mutableListOf<User>()
    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        rvUsers = view.findViewById(R.id.rv_users)
        btnNextPage = view.findViewById(R.id.btn_next_page)
        progressBar = view.findViewById(R.id.progress_bar)

        // Setup RecyclerView
        setupRecyclerView()
        
        // Animations
        val tvTitle = view.findViewById<View>(R.id.tv_title)
        val tvSubtitle = view.findViewById<View>(R.id.tv_subtitle)
        
        tvTitle.alpha = 0f
        tvTitle.translationY = -30f
        tvTitle.animate().alpha(1f).translationY(0f).setDuration(500).start()
        
        tvSubtitle.alpha = 0f
        tvSubtitle.animate().alpha(1f).setDuration(500).setStartDelay(100).start()
        
        rvUsers.alpha = 0f
        rvUsers.translationY = 50f
        rvUsers.animate().alpha(1f).translationY(0f).setDuration(600).setStartDelay(200).start()

        // Load initial data
        fetchUsers(currentPage)

        // Next Page button
        btnNextPage.setOnClickListener {
            currentPage++
            fetchUsers(currentPage)
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(userList) { user ->
            // Navigate to detail activity
            val intent = Intent(requireContext(), UserDetailActivity::class.java).apply {
                putExtra("USER_ID", user.id)
                putExtra("USER_FIRST_NAME", user.firstName)
                putExtra("USER_LAST_NAME", user.lastName)
                putExtra("USER_EMAIL", user.email)
                putExtra("USER_AVATAR", user.avatar)
            }
            startActivity(intent)
        }
        
        rvUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    private fun fetchUsers(page: Int) {
        showLoading(true)
        
        val call = ReqResClient.userApiService.getUsers(page)
        
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                showLoading(false)
                
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    userResponse?.let {
                        userAdapter.addUsers(it.data)
                        Toast.makeText(
                            requireContext(),
                            "Loaded page $page",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to load data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(
                    requireContext(),
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            btnNextPage.isEnabled = false
        } else {
            progressBar.visibility = View.GONE
            btnNextPage.isEnabled = true
        }
    }
}
