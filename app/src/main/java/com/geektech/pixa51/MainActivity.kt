package com.geektech.pixa51

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.geektech.pixa51.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var page = 1
    private var loading = true
    var array = arrayListOf<Hit>()
    var adapter = PixaAdapter(arrayListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClickers()
    }

    private fun initClickers() {
        with(binding) {
            imageRecycler.addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        val visibleItemCount = recyclerView.layoutManager?.childCount
                        val totalItemCount = recyclerView.layoutManager?.itemCount
                        val pastItemsVisible =
                            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        if (loading) {
                            if (visibleItemCount != null) {
                                if ((visibleItemCount + pastItemsVisible) >= totalItemCount!!) {
                                    loading = false
                                    Log.v("ololo", "Last Item !")

                                    // Pagination i.e. fetch new data
                                    page++
                                    nextPage()
                                    loading = true
                                }
                            }
                        }
                    }
                }
            })
            goBtn.setOnClickListener {
                page = 1
                doRequest()
            }
            changePageBtn.setOnClickListener {
                page++
                nextPage()
            }
        }
    }

    private fun ActivityMainBinding.nextPage() {
        PixaService().api.getImages(keyWord = searchEt.text.toString(), page = page)
            .enqueue(object : Callback<PixaModel> {
                override fun onResponse(
                    call: Call<PixaModel>,
                    response: Response<PixaModel>
                ) {
                    if (response.isSuccessful) {
                        val other = response.body()?.hits!!
                        array.addAll(other)
                        adapterInitialization()
                    }
                }

                override fun onFailure(call: Call<PixaModel>, t: Throwable) {
                    Log.e("ololo", "onFailure: ${t.message}")
                }
            })
    }


    private fun ActivityMainBinding.doRequest() {
        PixaService().api.getImages(keyWord = searchEt.text.toString(), page = page)
            .enqueue(object : Callback<PixaModel> {
                override fun onResponse(call: Call<PixaModel>, response: Response<PixaModel>) {
                    if (response.isSuccessful) {
                        array = response.body()?.hits!!
                        adapterInitialization()
                    }
                }

                override fun onFailure(call: Call<PixaModel>, t: Throwable) {
                    Log.e("ololo", "onFailure: ${t.message}")
                }
            })
    }

    private fun ActivityMainBinding.adapterInitialization() {
        adapter = PixaAdapter(array)
        imageRecycler.adapter = adapter
    }
}