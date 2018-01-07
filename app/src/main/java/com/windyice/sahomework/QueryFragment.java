package com.windyice.sahomework;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QueryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QueryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Spinner spinner_paper_chosen;
    private Spinner spinner_subtheme;
    private int selectedSubthemeOffset =-1;
    private Button button_paper_chosen;
    private TextView textView_paper;

    private int selectedPaperId=-1;

    private HashMap<String,JSONObject> id2TitleHashMap= new HashMap<>();

    private List<Integer> notContainId=new ArrayList<Integer>();


    public QueryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QueryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QueryFragment newInstance(String param1, String param2) {
        QueryFragment fragment = new QueryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_query, container, false);
        spinner_paper_chosen=view.findViewById(R.id.choose_paper_spinner);
        spinner_subtheme=view.findViewById(R.id.subtheme_spinner);
        button_paper_chosen=view.findViewById(R.id.choose_paper_button);
        textView_paper=view.findViewById(R.id.paper_textview);
        HashMap<String,String> num2TitleMap=new HashMap<>();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<String> subthemeAdapter=new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item);
        subthemeAdapter.add("应用");
        subthemeAdapter.add("方法");
        subthemeAdapter.add("概念");
        subthemeAdapter.add("综述");
        subthemeAdapter.add("定理");
        subthemeAdapter.add("理论");
        subthemeAdapter.add("实验");
        spinner_subtheme.setAdapter(subthemeAdapter);
        spinner_subtheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSubthemeOffset=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        initJsonHash();
        notContainId.add(19);
        notContainId.add(37);
        notContainId.add(73);
        notContainId.add(74);
        notContainId.add(81);
        notContainId.add(82);
        try {
            JsonReader jsonReader=new JsonReader(getResources().openRawResource(R.raw.num2paper));
            JSONObject jsonObject=jsonReader.getJsonObject();
            for(int i=1;i<=jsonObject.length();i++){
                String key=i+"";
                if(jsonObject.has(key)){
                    String title=jsonObject.getString(key);
                    adapter.add(key+": "+title);

                }
            }
            spinner_paper_chosen.setAdapter(adapter);
            spinner_paper_chosen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedPaperId=i+1;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        button_paper_chosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show(selectedPaperId);
            }
        });
        return view;
    }

    private void show(int id){
        StringBuilder output= new StringBuilder();
        String key=id+"";
        if(notContainId.contains(id)){
            output = new StringBuilder("对不起，暂时没有这篇论文的详细数据。");
        }
        else{
            try {
                JSONObject thePaper=id2TitleHashMap.get(key);
                switch (selectedSubthemeOffset){
                    case 0:
                        output.append("应用:\n");
                        JSONArray application=thePaper.getJSONArray("应用");
                        for(int i=0;i<application.length();i++){
                            JSONObject subtheme=application.getJSONObject(i);
                            String subthemeName=subtheme.getString("subtheme");
                            String feature_word=subtheme.getString("feature_word");
                            output.append(subthemeName).append(": ").append(feature_word).append("\n");
                        }
                        break;
                    case 1:
                        output.append("方法\n");
                        JSONArray method=thePaper.getJSONArray("方法");
                        for(int i=0;i<method.length();i++){
                            JSONObject subtheme=method.getJSONObject(i);
                            String subthemeName=subtheme.getString("subtheme");
                            String feature_word=subtheme.getString("feature_word");
                            output.append(subthemeName).append(": ").append(feature_word).append("\n");
                        }
                        break;
                    case 2:
                        output.append("概念\n");
                        JSONArray concept=thePaper.getJSONArray("概念");
                        for(int i=0;i<concept.length();i++){
                            JSONObject subtheme=concept.getJSONObject(i);
                            String subthemeName=subtheme.getString("subtheme");
                            String feature_word=subtheme.getString("feature_word");
                            output.append(subthemeName).append(": ").append(feature_word).append("\n");
                        }
                        break;
                    case 3:
                        output.append("综述\n");
                        JSONArray summary=thePaper.getJSONArray("综述");
                        for(int i=0;i<summary.length();i++){
                            JSONObject subtheme=summary.getJSONObject(i);
                            String subthemeName=subtheme.getString("subtheme");
                            String feature_word=subtheme.getString("feature_word");
                            output.append(subthemeName).append(": ").append(feature_word).append("\n");
                        }
                        break;
                    case 4:
                        output.append("定理\n");
                        JSONArray theory=thePaper.getJSONArray("定理");
                        for(int i=0;i<theory.length();i++){
                            JSONObject subtheme=theory.getJSONObject(i);
                            String subthemeName=subtheme.getString("subtheme");
                            String feature_word=subtheme.getString("feature_word");
                            output.append(subthemeName).append(": ").append(feature_word).append("\n");
                        }
                        break;
                    case 5:
                        output.append("理论\n");
                        JSONArray physic=thePaper.getJSONArray("理论");
                        for(int i=0;i<physic.length();i++){
                            JSONObject subtheme=physic.getJSONObject(i);
                            String subthemeName=subtheme.getString("subtheme");
                            String feature_word=subtheme.getString("feature_word");
                            output.append(subthemeName).append(": ").append(feature_word).append("\n");
                        }
                        break;
                    case 6:
                        output.append("实验\n");
                        JSONArray experiment=thePaper.getJSONArray("实验");
                        for(int i=0;i<experiment.length();i++){
                            JSONObject subtheme=experiment.getJSONObject(i);
                            String subthemeName=subtheme.getString("subtheme");
                            String feature_word=subtheme.getString("feature_word");
                            output.append(subthemeName).append(": ").append(feature_word).append("\n");
                        }
                        break;
                    default:
                        output.append("Error: 看不懂你的输入");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        textView_paper.setText(output.toString());
    }

    private void initJsonHash(){
        try {
            JsonReader jsonReader=new JsonReader(getResources().openRawResource(R.raw.a1));
            JSONObject jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("1",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a2));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("2",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a3));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("3",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a4));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("4",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a5));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("5",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a6));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("6",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a7));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("7",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a8));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("8",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a9));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("9",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a10));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("10",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a11));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("11",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a12));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("12",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a13));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("13",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a14));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("14",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a15));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("15",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a16));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("16",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a17));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("17",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a18));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("18",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a20));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("20",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a21));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("21",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a22));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("22",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a23));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("23",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a24));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("24",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a25));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("25",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a26));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("26",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a27));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("27",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a28));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("28",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a29));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("29",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a30));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("30",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a31));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("31",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a32));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("32",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a33));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("33",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a34));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("34",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a35));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("35",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a36));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("36",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a38));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("38",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a39));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("39",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a40));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("40",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a41));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("41",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a42));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("42",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a43));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("43",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a44));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("44",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a45));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("45",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a46));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("46",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a47));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("47",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a48));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("48",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a49));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("49",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a50));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("50",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a51));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("51",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a52));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("52",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a53));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("53",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a54));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("54",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a55));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("55",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a56));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("56",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a57));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("57",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a58));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("58",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a59));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("59",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a60));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("60",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a61));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("61",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a62));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("62",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a63));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("63",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a64));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("64",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a65));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("65",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a66));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("66",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a67));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("67",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a68));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("68",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a69));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("69",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a70));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("70",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a71));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("71",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a72));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("72",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a75));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("75",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a76));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("76",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a77));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("77",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a78));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("78",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a79));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("79",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a80));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("80",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a83));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("83",jsonObject);
            jsonReader=new JsonReader(getResources().openRawResource(R.raw.a84));
            jsonObject=jsonReader.getJsonObject();
            id2TitleHashMap.put("84",jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
