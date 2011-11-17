package net.bsayiner.SuperPi.Activities;

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

public class MainActivity extends Activity {

	private Intent calculationIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		calculationIntent = new Intent(this, CalculationResultActivity.class);
		System.err.println(getFilesDir());
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