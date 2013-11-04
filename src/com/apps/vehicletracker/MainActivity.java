package com.apps.vehicletracker;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.*;


public class MainActivity extends Activity {

	//GPSTracker class
	GPSTracker gps;
	int buffer=0;
	
	private Handler handler=new Handler();	
	
	private Runnable runnable=new Runnable() {
		@Override
		public void run() {
			//do what i need to do
			
			TextView txtStatus=(TextView) findViewById(R.id.txtStatus);
			gps=new GPSTracker(MainActivity.this);
			
			try
			{
					//check if GPS enabled
					if(gps.canGetLocation())							
					{
						double latitude=gps.getLatitude();
						double longitude=gps.getLongitude();
						EditText txtLog=(EditText) findViewById(R.id.txtLog);	
						//Toast.makeText(getApplicationContext(),"Your location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
						
						buffer+=1;
						if(buffer>500)
						{
							buffer=0;
							txtLog.setText("[" + latitude + ", " + longitude + "]  ");
						}
						else
						{
							txtLog.append("[" + latitude + ", " + longitude + "]  ");	
						}						
												
						txtStatus.setText("");
					}
					else
					{
						//can't get location
						//GPS or Network is not enabled
						//Ask user to enable GPS/network in settings
						gps.showSettingsAlert();
					}
			}
			catch(Exception e)
			{
				EditText txtLog=(EditText) findViewById(R.id.txtLog);
				txtLog.setText(e.getMessage());
			}
			
			handler.postDelayed(this, 2000);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnStartService=(Button) findViewById(R.id.btnStart);
		Button btnStopService=(Button) findViewById(R.id.btnStop);
		
		btnStartService.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Button btnStopService=(Button) findViewById(R.id.btnStop);
				v.setEnabled(false);
				btnStopService.setEnabled(true);
				TextView txtStatus=(TextView) findViewById(R.id.txtStatus);
				txtStatus.setText("Running...");
				handler.postDelayed(runnable, 2000);				
			}				
		});
		
		
		btnStopService.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				v.setEnabled(false);
				Button btnStartService=(Button) findViewById(R.id.btnStart);
				btnStartService.setEnabled(true);
				TextView txtStatus=(TextView) findViewById(R.id.txtStatus);
				txtStatus.setText("Stopped");
				handler.removeCallbacks(runnable);				
			}
				
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
