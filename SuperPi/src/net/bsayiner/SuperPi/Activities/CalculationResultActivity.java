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
		readTotalRam();
		
	}

	public float readTotalRam() {
		float tm = 1000;
		try {
			RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
			String load = reader.readLine();
			String[] totrm = load.split(" kB");
			String[] trm = totrm[0].split(" ");
			tm = Integer.parseInt(trm[trm.length - 1]);
			tm = (tm / 1024.0f);
			TextView txtAvailableMemory = (TextView) findViewById(R.id.txtAvailableRealMemory);
			txtAvailableMemory.setText(String.valueOf(tm));
			load = reader.readLine();
			totrm = load.split(" kB");
			trm = totrm[0].split(" ");
			tm = Integer.parseInt(trm[trm.length - 1]);
			tm = (tm / 1024.0f);
			TextView txtM = (TextView) findViewById(R.id.txtAllocatedMemory);
			txtM.setText(String.valueOf(tm));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return tm;
	}
}
