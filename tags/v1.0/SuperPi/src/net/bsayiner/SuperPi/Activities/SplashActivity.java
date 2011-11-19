package net.bsayiner.SuperPi.Activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import net.bsayiner.benchmark.SuperPi.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends Activity {

	private ArrayList<String> recordList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkRecordFileExists();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_layout);
		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (waited < 3000) {
						sleep(100);
						waited += 100;
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					finish();
					Intent i = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(i);
				}
			}
		};
		splashThread.start();
	}

	private void checkRecordFileExists() {
		File file = getFileStreamPath("records");
		System.err.println("Is record file exist : " + file.exists());
		if (!file.exists()) {
			recordList = new ArrayList<String>();
			recordList.add(0, "Not calculated");
			recordList.add(1, "Not calculated");
			recordList.add(2, "Not calculated");
			recordList.add(3, "Not calculated");
			recordList.add(4, "Not calculated");
			recordList.add(5, "Not calculated");
			recordList.add(6, "Not calculated");
			recordList.add(7, "Not calculated");
			recordList.add(8, "Not calculated");
			WriteSettings();
		}
	}

	// Save record
	public void WriteSettings() {
		FileOutputStream fOut = null;
		ObjectOutputStream osw = null;
		try {
			fOut = openFileOutput("records", MODE_PRIVATE);
			osw = new ObjectOutputStream(fOut);
			osw.writeObject(recordList);
			osw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				osw.close();
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
