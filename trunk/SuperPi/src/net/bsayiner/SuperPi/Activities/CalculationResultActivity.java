package net.bsayiner.SuperPi.Activities;

import java.io.IOException;
import org.apfloat.ApfloatRuntimeException;

import net.bsayiner.SuperPi.Utilies.Pi;
import net.bsayiner.benchmark.SuperPi.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class CalculationResultActivity extends Activity implements Runnable {

	private int selectedDigit;
	private Pi pi;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculate_layout);
		initializeCalculationOfDigit();
		pi = new Pi(this);
		initSystemValues();
		showDialog(0);
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
				// TODO Auto-generated method stub
				new Thread(CalculationResultActivity.this).start();
			}
		});
		return builder.create();
	}

	private void initializeCalculationOfDigit() {
		try {
			selectedDigit = getIntent().getExtras().getInt("Digit");
		} catch (Exception e) {
			selectedDigit = 16;
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
		txtTotalIterationTime.setText(pi.getTotalElapsedTime());
		tableRow.addView(txtTotalIterationTime);
		TextView txtIterationTimeStamp = new TextView(this);
		txtIterationTimeStamp.setBackgroundColor(Color.BLACK);
		txtIterationTimeStamp.setText(": " + pi.getTotalElapsedTimeStamp());
		tableRow.addView(txtIterationTimeStamp);
		tableLayout.addView(tableRow);
		Toast.makeText(this, pi.getTotalElapsedTime() + " " + pi.getTotalElapsedTimeStamp(), Toast.LENGTH_LONG).show();
	}

	private void setIterationTime() {
		TableLayout tableLayout = (TableLayout) findViewById(R.id.layout_main);
		TableRow tableRow = new TableRow(this);
		tableRow.setPadding(0, 0, 0, 1);
		TextView txtIterationTime = new TextView(this);
		txtIterationTime.setBackgroundColor(Color.BLACK);
		txtIterationTime.setText(pi.getIterationTime());
		tableRow.addView(txtIterationTime);
		TextView txtIterationTimeStamp = new TextView(this);
		txtIterationTimeStamp.setBackgroundColor(Color.BLACK);
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

	private void setInfoHeader(String inforHeader) {
		TextView txtInfoHeader = (TextView) findViewById(R.id.txtInfoHeader);
		txtInfoHeader.setText(inforHeader);
	}
}
