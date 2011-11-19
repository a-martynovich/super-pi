package net.bsayiner.SuperPi.Activities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import net.bsayiner.benchmark.SuperPi.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Intent calculationIntent;
	private ArrayList<String> recordList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		calculationIntent = new Intent(this, CalculationResultActivity.class);
	}

	@Override
	protected void onResume() {
		super.onResume();
		readRecordFromFile();
		initializeComponents();
	}

	private void initializeComponents() {
		TextView txt = (TextView) findViewById(R.id.txt2K);
		String recordValue = recordList.get(0);
		txt.setText(recordValue);
		if (!recordValue.equals("Not calculated")) {
			ImageView imageView = (ImageView) findViewById(R.id.img2k);
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.ok));
		}
		txt = (TextView) findViewById(R.id.txt4K);
		recordValue = recordList.get(1);
		txt.setText(recordValue);
		if (!recordValue.equals("Not calculated")) {
			ImageView imageView = (ImageView) findViewById(R.id.img4k);
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.ok));
		}
		txt = (TextView) findViewById(R.id.txt8K);
		recordValue = recordList.get(2);
		txt.setText(recordValue);
		if (!recordValue.equals("Not calculated")) {
			ImageView imageView = (ImageView) findViewById(R.id.img8k);
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.ok));
		}
		txt = (TextView) findViewById(R.id.txt16K);
		recordValue = recordList.get(3);
		txt.setText(recordValue);
		if (!recordValue.equals("Not calculated")) {
			ImageView imageView = (ImageView) findViewById(R.id.img16k);
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.ok));
		}
		txt = (TextView) findViewById(R.id.txt32K);
		recordValue = recordList.get(4);
		txt.setText(recordValue);
		if (!recordValue.equals("Not calculated")) {
			ImageView imageView = (ImageView) findViewById(R.id.img32k);
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.ok));
		}
		txt = (TextView) findViewById(R.id.txt64K);
		recordValue = recordList.get(5);
		txt.setText(recordValue);
		if (!recordValue.equals("Not calculated")) {
			ImageView imageView = (ImageView) findViewById(R.id.img64k);
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.ok));
		}
		txt = (TextView) findViewById(R.id.txt128K);
		recordValue = recordList.get(6);
		txt.setText(recordValue);
		if (!recordValue.equals("Not calculated")) {
			ImageView imageView = (ImageView) findViewById(R.id.img128k);
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.ok));
		}
		txt = (TextView) findViewById(R.id.txt256K);
		recordValue = recordList.get(7);
		txt.setText(recordValue);
		if (!recordValue.equals("Not calculated")) {
			ImageView imageView = (ImageView) findViewById(R.id.img256k);
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.ok));
		}
		txt = (TextView) findViewById(R.id.txt512K);
		recordValue = recordList.get(8);
		txt.setText(recordValue);
		if (!recordValue.equals("Not calculated")) {
			ImageView imageView = (ImageView) findViewById(R.id.img512k);
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.ok));
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
			System.err.println(recordList.get(0));
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

	public void btnCalculateClicked(View view) {
		Log.e("Log Message", "btnCalculate Clicked");
		showDialog(0);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select digits of Pi to be calculated.");
		builder.setSingleChoiceItems(getResources().getStringArray(R.array.unit_names), 0, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				int selectedDigit = (int) Math.pow(2, which + 1);
				Log.i("Digit Selected", String.valueOf(selectedDigit));
				calculationIntent.putExtra("Digit", selectedDigit);
				calculationIntent.putExtra("Index", which);
			}
		});
		builder.setPositiveButton("Ok", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startActivity(calculationIntent);
			}
		});
		return builder.create();
	}

}