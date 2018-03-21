package com.sayson.narl.pkmnsearch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.google.gson.GsonBuilder
import com.sayson.narl.pkmnsearch.R.id.*


import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var pokemonName: String? = null
    private var mpokemonName: String? = null
    private val stats = ArrayList<Stats>()
    private val abilities = ArrayList<Abilities>()
    private val types = ArrayList<Types>()

    private val pokemon_sprites = "sprites"
    private val pokemon_key = "front_default"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val recyclerView2 = findViewById<RecyclerView>(R.id.recyclerView2)
        val recyclerView3 = findViewById<RecyclerView>(R.id.recyclerView3)


        recyclerView2.layoutManager = LinearLayoutManager(this)
        recyclerView3.layoutManager = LinearLayoutManager(this)

        button.setOnClickListener({
            pokemonName = pokemon_name.text.toString().toLowerCase()
            mpokemonName = pokemonName

            progressBar.visibility = View.VISIBLE



            val url = "https://pokeapi.co/api/v2/pokemon/$mpokemonName/"
            val request = Request.Builder().url(url).build(     )
            val client = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build()


            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    Log.e("Poke API", "Failed to get pokemon", e)
                }

                override fun onResponse(call: Call?, response: Response?) {
                    if (response != null && response.isSuccessful) {
                        val json = response.body()?.string()
                        displayResult(json)
                    }
                }
            })
        })
    }

    private fun displayResult(json: String?) {

        runOnUiThread {
            Log.e("Poke API", "ENTRY")
            val gson = GsonBuilder().create()
            val pokemon = gson.fromJson(json, Pokemon::class.java)

            if (mpokemonName == pokemon.name) {
                progressBar.visibility = View.GONE
                constraintLayout.visibility = View.VISIBLE

                val pokemonImage = pkmn_img
                val jsonObject = JSONObject(json)
                val pokeSprites = jsonObject.getJSONObject(pokemon_sprites).getString(pokemon_key)

                Picasso.with(this@MainActivity).load(pokeSprites).into(pokemonImage)
                poke_name.text = "Name: " + pokemon.name.toUpperCase()





                }

                abilities.clear()
                val abilities_length = JSONObject(json).getJSONArray("abilities").length()
                var abilities_counter = 0
                for (i in 1..abilities_length) {
                    var pokemon_abilities = JSONObject(json).getJSONArray("abilities").getJSONObject(abilities_counter).getJSONObject("ability").getString("name")
                    abilities.add(Abilities(pokemon_abilities))
                    recyclerView2.adapter = AdapterAbilities(abilities)
                    abilities_counter++
                }

                types.clear()
                val types_length = JSONObject(json).getJSONArray("types").length()
                var types_counter = 0
                for (i in 1..types_length) {
                    var pokemon_types = JSONObject(json).getJSONArray("types").getJSONObject(types_counter).getJSONObject("type").getString("name")
                    types.add(Types(pokemon_types))
                    recyclerView3.adapter = AdapterTypes(types)
                    types_counter++
                }

            }
        }





    }





