package ma.wanam.youtubeadaway;

import ma.wanam.youtubeadaway.R.id;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends Activity {
	private final String DONATE_URL = "https://www.paypal.me/Wanam";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final SharedPreferences sharedPreferences = getPreferences(Context.MODE_WORLD_READABLE);

		setContentView(R.layout.activity_main);

		String pInfo = "";
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		Resources res = getResources();
		String status = res.getString(R.string.app_name)
				+ " v"
				+ pInfo
				+ " "
				+ (XChecker.isEnabled() ? res.getString(R.string.module_active) : res
						.getString(R.string.module_inactive));
		TextView tvStatus = ((TextView) findViewById(R.id.moduleStatus));
		tvStatus.setText(status);
		tvStatus.setTextColor((XChecker.isEnabled() ? Color.GREEN : Color.RED));

		final CheckBox cbLogs = (CheckBox) findViewById(id.enableLogs);
		if (VERSION.SDK_INT > VERSION_CODES.M) {
			// Hide on on N+ builds
			// cannot access prefs from xposed processes
			cbLogs.setVisibility(View.GONE);
		} else {
			cbLogs.setChecked(sharedPreferences.getBoolean("enableLogs", true));
			cbLogs.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					sharedPreferences.edit().putBoolean("enableLogs", cbLogs.isChecked()).commit();
				}
			});
		}

		findViewById(id.btnOK).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		findViewById(id.btnDonate).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DONATE_URL));
					startActivity(browserIntent);
				} catch (Throwable e) {
					e.printStackTrace();
				} finally {
					finish();
				}
			}
		});
	}
}
