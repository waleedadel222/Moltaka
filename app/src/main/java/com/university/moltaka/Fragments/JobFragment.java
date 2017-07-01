package com.university.moltaka.Fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.university.moltaka.Drawer.Adv_Adapter;
import com.university.moltaka.JSON.JsonReader;
import com.university.moltaka.Login.Login_ResponseHandler;
import com.university.moltaka.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JobFragment extends Fragment {

    JSONArray Jobs ;
     String job_url = "http://10.0.2.2:80/moltaka/job.php";

    ArrayList<HashMap<String, String>> Jobs_Data= new ArrayList<HashMap<String, String>>();
    Adv_Adapter adv_adapter;
    ListView job_list;
	
	public JobFragment(){}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_job, container, false);

        job_list= (ListView) rootView.findViewById(R.id.adv_lv);

        new GetJobs().execute();

        return rootView;
    }

    // GET Jobs
    class GetJobs extends AsyncTask<Void, Boolean, Boolean> {

        String national_id = Login_ResponseHandler.national_id;
        String  result,  message;
        int success;
        JSONObject jsonObject;

        // ProgressDialog
        private android.app.ProgressDialog ProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressDialog = ProgressDialog.show(getActivity(),
                           "Processing...", "Getting The Jobs", false, false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("national_id", national_id));

            JsonReader j = new JsonReader(job_url,pairs);
            result = j.sendRequest();

            try {
                jsonObject = new JSONObject(result);
                success= jsonObject.getInt("success");
                message =jsonObject.getString("message");
            } catch (Exception e) {
                e.printStackTrace();
            }

            switch (success) {
                case 0: if (message== null){
                    message = "error in connection " ;
                 }
                    return false;

                case 1:

                    try {
                        Jobs = jsonObject.getJSONArray("jobs");

                        for (int i = 0; i < Jobs.length(); i++) {
                            JSONObject c = Jobs.getJSONObject(i);

                            String company_name = c.getString("company_name");
                            String job_name = c.getString("job_name");
                            String contacting = c.getString("contacts");
                            String description = c.getString("description");

                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("company_name", company_name);
                            map.put("job_name", job_name);
                            map.put("contacts", contacting);
                            map.put("description", description);
                            Jobs_Data.add(map);

                        }

                    }catch(JSONException e){ e.printStackTrace();  }

                    return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        ProgressDialog.dismiss();

            if(aBoolean) {
                adv_adapter = new Adv_Adapter(getActivity(), R.layout.adv_row, Jobs_Data);
                job_list.setAdapter(adv_adapter);
            }
            else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();}
        }

    }
}
