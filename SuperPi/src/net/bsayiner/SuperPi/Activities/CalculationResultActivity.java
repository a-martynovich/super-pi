package net.bsayiner.SuperPi.Activities;

import java.io.IOException;
import java.io.RandomAccessFile;

import net.bsayiner.benchmark.SuperPi.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CalculationResultActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculate_layout);
		initializeCalculationOfDigit();
		readTotalRam();
	}

	private void initializeCalculationOfDigit() {
		int selectedDigit;
		try {
			selectedDigit = getIntent().getExtras().getInt("Digit");
		} catch (Exception e) {
			selectedDigit = 16;
		}
		TextView txtDigitInfo = (TextView) findViewById(R.id.txtDigitInfo);
		txtDigitInfo.setText(String.valueOf(selectedDigit) + "K Calculation start.");
	}

	public float readTotalRam() {
		float tm = 1000;
		try {
			RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
			String load = reader.readLine();
			String[] totrm = load.split(" kB");
			String[] trm = totrm[0].split(" ");
			tm = Integer.parseInt(trm[trm.length - 1]);
			TextView txtRealMemory = (TextView) findViewById(R.id.txtRealMemory);
			txtRealMemory.setText(String.valueOf(tm) + " kB");
			TextView txtAvailableMemory = (TextView) findViewById(R.id.txtAvailableRealMemory);
			txtAvailableMemory.setText(String.valueOf(tm) + " kB");
			load = reader.readLine();
			totrm = load.split(" kB");
			trm = totrm[0].split(" ");
			tm = Integer.parseInt(trm[trm.length - 1]);
			TextView txtFreeMemory = (TextView) findViewById(R.id.txtFreeMemory);
			txtFreeMemory.setText(String.valueOf(tm) + "kB");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return tm;
	}
}
