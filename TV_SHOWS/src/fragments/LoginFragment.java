package fragments;



import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import engine.Login;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {
	
	public LoginFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        
        final EditText user = (EditText) rootView.findViewById(R.id.user);
        final EditText pass = (EditText) rootView.findViewById(R.id.passwd);
        Button button = (Button)rootView.findViewById(R.id.login);
        final Context context = this.getActivity().getApplicationContext();
             
        button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Login login;
				try {
					 login = new Login(user.getText().toString(),pass.getText().toString(),context);
					 
					 if (!login.isSuccess()){
							
							new MyDialogFragment().show(getFragmentManager(), "MyDialog");
						
						}
						
						else{
							
						Log.e("","GIUSTO");
						}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
			}
        	
        	
        	
        	
        	
        });
        
        
        
      
			
			
				
				
				
				
	
        
        
        
 
        
        this.getActivity().setTitle("LOGIN"); 
        
        
        return rootView;
    }
	
	
	protected Dialog onCreateDialog(int id) {
		  switch (id) {
		    case 1:
		    Builder builder = new AlertDialog.Builder(this.getActivity().getApplicationContext());
		    builder.setMessage("This will end the activity");
		    builder.setCancelable(true);
		    builder.setPositiveButton("I agree",null);
		    builder.setNegativeButton("No, no", null);
		    AlertDialog dialog = builder.create();
		    dialog.show();
		  }
		  
		return this.onCreateDialog(id);
		//  return super.onCreateDialog(id);
		}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
