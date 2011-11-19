package net.bsayiner.SuperPi.Activities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import org.apfloat.ApfloatRuntimeException;

import net.bsayiner.SuperPi.Utilies.Pi;
import net.bsayiner.benchmark.SuperPi.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class CalculationResultActivity extends Activity implements Runnable {

	private int selectedDigit;
	private int selectedIndex;
	private Pi pi;
	private ProgressDialog progressDialog;
	private ArrayList<String> recordList;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		readRecordFromFile();
		setContentView(R.layout.calculate_layout);
		initializeCalculationOfDigit();
		pi = new Pi(this);
		initSystemValues();
		showDialog(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.show_pi_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about_menu:
			PackageInfo packageInfo = null;
			try {
				packageInfo = getPackageManager().getPackageInfo("net.bsayiner.benchmark.SuperPi", 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("SuperPi v" + packageInfo.versionName);
			alert.setMessage("Copyright (C) 2011 Bora SAYINER\nEmail : bsayiner@bsayiner.net");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			alert.show();
			return true;
		case R.id.show_pi:
			final AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
			alert1.setTitle("Calculated digit of Pi");
			alert1.setMessage(pi.getPiResult());
			alert1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			alert1.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initSystemValues() {
		TextView txtMemoryBlockSize = (TextView) findViewById(R.id.txtMemoryBlockSize);
		txtMemoryBlockSize.setText(String.valueOf(": " + pi.getApfloatContext().getMaxMemoryBlockSize()) + " kB");
		TextView txtCacheL1Size = (TextView) findViewById(R.id.txtCacheL1Size);
		txtCacheL1Size.setText(String.valueOf(": " + pi.getApfloatContext().getCacheL1Size() + " kB"));
		TextView txtCacheL2Size = (TextView) findViewById(R.id.txtCacheL2Size);
		txtCacheL2Size.setText(String.valueOf(": " + pi.getApfloatContext().getCacheL2Size() + " kB"));
		TextView txtCacheBurst = (TextView) findViewById(R.id.txtCacheBurst);
		txtCacheBurst.setText(String.valueOf(": " + pi.getApfloatContext().getCacheBurst() + " kB"));
		TextView txtBlockSize = (TextView) findViewById(R.id.txtBlockSize);
		txtBlockSize.setText(String.valueOf(": " + pi.getApfloatContext().getBlockSize()) + " kB");
		TextView txtNumberOfProcessor = (TextView) findViewById(R.id.txtNumberOfProcessor);
		txtNumberOfProcessor.setText(String.valueOf(": " + pi.getApfloatContext().getNumberOfProcessors()));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Start");
		builder.setMessage("Now start calculate " + String.valueOf(selectedDigit) + "K digit of Pi.");
		builder.setPositiveButton("Ok", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				new Thread(CalculationResultActivity.this).start();
				progressDialog = ProgressDialog.show(CalculationResultActivity.this, "", "Calculating Pi digits ...");
			}
		});
		return builder.create();
	}

	private void initializeCalculationOfDigit() {
		try {
			selectedDigit = getIntent().getExtras().getInt("Digit");
			selectedIndex = getIntent().getExtras().getInt("Index");
		} catch (Exception e) {
			selectedDigit = 2;
			selectedIndex = 0;
		}
	}

	public void run() {
		try {
			pi.calculatePiPrecision(new String[] { String.valueOf(selectedDigit * 1000), "2" });
		} catch (ApfloatRuntimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		progressDialog.dismiss();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			int message = msg.what;
			switch (message) {
			case 0:
				setInfoHeader(pi.getInfoHeader());
				break;
			case 1:
				setCalculationAlgorithm(pi.getCalculationAlgorithm());
				break;
			case 2:
				setIterationInfo(pi.getIterationInfo());
				break;
			case 3:
				setIterationTime();
				break;
			case 4:
				setTotalElapsedTime();
				break;
			}
		}

	};

	private void setTotalElapsedTime() {
		TableLayout tableLayout = (TableLayout) findViewById(R.id.layout_main);
		TableRow tableRow = new TableRow(this);
		tableRow.setPadding(0, 0, 0, 1);
		TextView txtTotalIterationTime = new TextView(this);
		txtTotalIterationTime.setBackgroundColor(Color.BLACK);
		txtTotalIterationTime.setTextColor(getResources().getColor(R.color.layout_text_color));
		txtTotalIterationTime.setTypeface(Typeface.MONOSPACE);
		txtTotalIterationTime.setText(pi.getTotalElapsedTime());
		tableRow.addView(txtTotalIterationTime);
		TextView txtIterationTimeStamp = new TextView(this);
		txtIterationTimeStamp.setBackgroundColor(Color.BLACK);
		txtIterationTimeStamp.setTextColor(getResources().getColor(R.color.layout_text_color));
		txtIterationTimeStamp.setTypeface(Typeface.MONOSPACE);
		txtIterationTimeStamp.setText(": " + pi.getTotalElapsedTimeStamp());
		tableRow.addView(txtIterationTimeStamp);
		tableLayout.addView(tableRow);
		Toast.makeText(this, pi.getInfoHeader() + " at " + pi.getTotalElapsedTime() + " " + pi.getTotalElapsedTimeStamp(), Toast.LENGTH_LONG).show();
		recordList.set(selectedIndex, pi.getTotalElapsedTimeStamp());
		// Write record to file
		WriteSettings();
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

	@SuppressWarnings("unchecked")
	private void readRecordFromFile() {
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			fileInputStream = openFileInput("records");
			objectInputStream = new ObjectInputStream(fileInputStream);
			recordList = (ArrayList<String>) objectInputStream.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void setIterationTime() {
		TableLayout tableLayout = (TableLayout) findViewById(R.id.layout_main);
		TableRow tableRow = new TableRow(this);
		tableRow.setPadding(0, 0, 0, 1);
		TextView txtIterationTime = new TextView(this);
		txtIterationTime.setBackgroundColor(Color.BLACK);
		txtIterationTime.setTextColor(getResources().getColor(R.color.layout_text_color));
		txtIterationTime.setTypeface(Typeface.MONOSPACE);
		txtIterationTime.setText(pi.getIterationTime());
		tableRow.addView(txtIterationTime);
		TextView txtIterationTimeStamp = new TextView(this);
		txtIterationTimeStamp.setBackgroundColor(Color.BLACK);
		txtIterationTimeStamp.setTextColor(getResources().getColor(R.color.layout_text_color));
		txtIterationTimeStamp.setTypeface(Typeface.MONOSPACE);
		txtIterationTimeStamp.setText(": " + pi.getIterationTimeStamp());
		tableRow.addView(txtIterationTimeStamp);
		tableLayout.addView(tableRow);
	}

	private void setIterationInfo(String iterationInfo) {
		TextView txtIterationInfo = (TextView) findViewById(R.id.txtIterationInfo);
		txtIterationInfo.setText(iterationInfo);
	}

	private void setCalculationAlgorithm(String calculationAlgorithm) {
		TextView txtCalculationAlgorithm = (TextView) findViewById(R.id.txtCalculationAlgorithm);
		txtCalculationAlgorithm.setText(calculationAlgorithm);
	}

	public Handler getHandler() {
		return handler;
	}

	private void setInfoHeader(String infoHeader) {
		TextView txtInfoHeader = (TextView) findViewById(R.id.txtInfoHeader);
		txtInfoHeader.setText(infoHeader);
	}

}
