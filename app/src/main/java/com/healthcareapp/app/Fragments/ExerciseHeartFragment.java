package com.healthcareapp.app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.healthcareapp.app.Adapter.HeartCardAdapter;
import com.healthcareapp.app.Model.Card;
import com.healthcareapp.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExerciseHeartFragment extends Fragment {

    private RecyclerView recyclerView;
    private HeartCardAdapter cardAdapter;
    private List<Card> mCards;
    Card card;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_heart, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCards = new ArrayList<>();
        readCards();

        cardAdapter = new HeartCardAdapter(getContext(), mCards);
        recyclerView.setAdapter(cardAdapter);

        return view;
    }

    private void readCards() {


        String url = "https://health.gov/myhealthfinder/api/v3/topicsearch.json?lang=en&categoryId=exercise&keyword=heart%20disease";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    mCards.clear();
                    JSONObject resultT = response.getJSONObject("Result");
                    int totalR = resultT.getInt("Total");
                    System.out.println(totalR);

                    for (int i = 0; i < totalR; i++){

                        JSONObject result = response.getJSONObject("Result");
                        JSONObject resources = result.getJSONObject("Resources");
                        JSONArray resource = resources.getJSONArray("Resource");

                        String cardTitle = resource.getJSONObject(i).getString("Title");
                        String cardURL = resource.getJSONObject(i).getString("AccessibleVersion");
                        String cardImageURL = resource.getJSONObject(i).getString("ImageUrl");

                        card = new Card(cardTitle, cardURL, cardImageURL);
                        mCards.add(card);
                        cardAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("failed");
            }
        });

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this.getContext());
        requestQueue.add(jsonObjectRequest);
    }
}