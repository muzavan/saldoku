package com.example.saldoku;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private static RadioButton jenis_pengeluaran;
	private static RadioButton jenis_pemasukan;
	private static RadioButton jenis_edit;
	private static EditText keterangan_edit;
	private static EditText nominal_edit;
	private static TextView kondisi;
	private static TextView table_id;
	private static TextView table_waktu;
	private static TextView table_jenis;
	private static TextView table_nominal;
	private static TextView table_keterangan;
	private static SQLiteDatabase db = null;
	private static Cursor dbCursor = null;
	private static DBAdapter saldo = null;
	private static Button refresh;
	private static Button update;
	private static Button bukukan;
	private static Button help;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView;
			rootView = null;

			if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
				rootView = inflater.inflate(R.layout.fragment_main_dummy,
						container, false);
				help = (Button) rootView.findViewById(R.id.help);

				help.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent i = new Intent(getActivity(), HelpActivity.class);
						startActivity(i);
					}
				});
				// TextView dummyTextView = (TextView) rootView
				// .findViewById(R.id.section_label);
				// dummyTextView.setText(Integer.toString(getArguments().getInt(
				// ARG_SECTION_NUMBER)));

			} else {
				if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
					rootView = inflater.inflate(R.layout.update_doku,
							container, false);
					saldo = new DBAdapter(this.getActivity());
					db = saldo.getWritableDatabase();
					saldo.createTable(db);

					jenis_pemasukan = (RadioButton) rootView
							.findViewById(R.id.jenis_pemasukan);
					jenis_pengeluaran = (RadioButton) rootView
							.findViewById(R.id.jenis_pengeluaran);
					keterangan_edit = (EditText) rootView
							.findViewById(R.id.keterangan);
					nominal_edit = (EditText) rootView
							.findViewById(R.id.nominal);
					update = (Button) rootView.findViewById(R.id.update);
					jenis_edit = jenis_pemasukan;
					jenis_pemasukan
							.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								public void onCheckedChanged(
										CompoundButton arg0, boolean arg1) {
									// TODO Auto-generated method stub
									jenis_pengeluaran.setChecked(!arg1);
									jenis_edit = jenis_pemasukan;

								}
							});
					jenis_pengeluaran
							.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								public void onCheckedChanged(
										CompoundButton arg0, boolean arg1) {
									// TODO Auto-generated method stub
									jenis_pemasukan.setChecked(!arg1);
									jenis_edit = jenis_pengeluaran;

								}
							});
					update.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							String a, b;
							int c;
							// Calendar c1 = Calendar.getInstance();
							// String date = "" +
							// c1.get(Calendar.DATE)+"-"+c1.get(Calendar.MONTH)+"-"+c1.get(Calendar.YEAR);
							SimpleDateFormat dateFormat = new SimpleDateFormat(
									"dd-MM-yyyy");
							SimpleDateFormat jamFormat = new SimpleDateFormat(
									"HH:mm:ss");
							String date = "" + dateFormat.format(new Date())
									+ "\n" + jamFormat.format(new Date());

							if (!((keterangan_edit.getText().toString()
									.length() == 0)
									|| nominal_edit.getText().toString()
											.length() == 0 || Integer
									.valueOf(nominal_edit.getText().toString()) <= 0)) {
								a = jenis_edit.getText().toString();
								b = keterangan_edit.getText().toString();
								c = Integer.valueOf(nominal_edit.getText()
										.toString());
								db.execSQL("INSERT INTO SALDO (WAKTU,JENIS,NOMINAL,KETERANGAN) VALUES  ('"
										+ date
										+ "','"
										+ a
										+ "', "
										+ c
										+ ", '"
										+ b + "');");
								Toast.makeText(
										getActivity(),
										"Update Berhasil!\nJenis : " + a
												+ "\nNominal : Rp" + c
												+ "\nKeterangan : " + b,
										Toast.LENGTH_SHORT).show();

							} else {
								if (Integer.valueOf(nominal_edit.getText()
										.toString()) <= 0) {
									Toast.makeText(
											getActivity(),
											"Nominal SALAH!\nJangan Nol (0) atau Negatif.",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(getActivity(),
											"Data yang diisi belum lengkap!",
											Toast.LENGTH_SHORT).show();
								}
							}

						}
					});

				}

				if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
					rootView = inflater.inflate(R.layout.data_doku, container,
							false);
					saldo = new DBAdapter(this.getActivity());
					db = saldo.getWritableDatabase();
					saldo.createTable(db);
					refresh = (Button) rootView.findViewById(R.id.refresh);
					table_id = (TextView) rootView.findViewById(R.id.table_id);
					table_jenis = (TextView) rootView
							.findViewById(R.id.table_jenis);
					table_nominal = (TextView) rootView
							.findViewById(R.id.table_nominal);
					table_keterangan = (TextView) rootView
							.findViewById(R.id.table_keterangan);
					table_waktu = (TextView) rootView
							.findViewById(R.id.table_waktu);
					bukukan = (Button) rootView.findViewById(R.id.bukukan);
					kondisi = (TextView) rootView.findViewById(R.id.kondisi);
					bukukan.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							int sisa = 0;
							SimpleDateFormat dateFormat = new SimpleDateFormat(
									"dd-MM-yyyy");
							SimpleDateFormat jamFormat = new SimpleDateFormat(
									"HH:mm:ss");
							String date = "" + dateFormat.format(new Date())
									+ "\n" + jamFormat.format(new Date());
							String tanggal = "" + dateFormat.format(new Date());
							if (dbCursor.moveToFirst()) {
								for (; !dbCursor.isAfterLast(); dbCursor
										.moveToNext()) {

									if (dbCursor.getString(2).equals(
											"Pemasukan")) {
										sisa = sisa
												+ Integer.valueOf(dbCursor
														.getString(3)
														.toString());
									} else {
										sisa = sisa
												- Integer.valueOf(dbCursor
														.getString(3)
														.toString());
									}
								}
								db.execSQL("DROP TABLE IF EXISTS SALDO");
								db.execSQL("CREATE  TABLE if not exists SALDO"
										+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,WAKTU TEXT,JENIS TEXT, NOMINAL INTEGER,KETERANGAN TEXT);");
								db.execSQL("INSERT INTO SALDO (WAKTU,JENIS,NOMINAL,KETERANGAN) VALUES  ('"
										+ date
										+ "','Pemasukan',"
										+ sisa
										+ ", 'Pembukuan pada tanggal : "
										+ tanggal + "');");
								Toast.makeText(
										getActivity(),
										"Pembukuan Berhasil!\nKlik 'Refresh' untuk kembali melihat data",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getActivity(), "Data belum ada",
										Toast.LENGTH_SHORT).show();
							}

						}

					});
					refresh.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dbCursor = db.rawQuery("SELECT * FROM SALDO", null);
							String tid = "ID\n\n";
							String tjenis = "JENIS\n\n";
							String twaktu = "WAKTU\n\n";
							String tnominal = "NOMINAL\n\n";
							String tket = "KETERANGAN\n\n";
							String tv_saldo = "";
							int sisa = 0;
							if (dbCursor.moveToFirst()) {
								for (; !dbCursor.isAfterLast(); dbCursor
										.moveToNext()) {
									tid = tid + dbCursor.getString(0) + "\n\n";
									twaktu = twaktu + dbCursor.getString(1)
											+ "\n";
									tjenis = tjenis + dbCursor.getString(2)
											+ "\n\n";
									tnominal = tnominal + dbCursor.getString(3)
											+ "\n\n";
									tket = tket + dbCursor.getString(4)
											+ "\n\n";
									if (dbCursor.getString(2).equals(
											"Pemasukan")) {
										sisa = sisa
												+ Integer.valueOf(dbCursor
														.getString(3)
														.toString());
									} else {
										sisa = sisa
												- Integer.valueOf(dbCursor
														.getString(3)
														.toString());
									}
								}
							}
							table_waktu.setText(twaktu);
							table_id.setText(tid);
							table_jenis.setText(tjenis);
							table_nominal.setText(tnominal);
							table_keterangan.setText(tket);
							kondisi.setText("Saldo Anda : Rp" + sisa);
							tv_saldo = kondisi.getText().toString();
							if (sisa < 300000) {
								kondisi.setBackgroundColor(Color.rgb(0xFF,
										0x88, 0x88));
								kondisi.setText(tv_saldo + "\nSEKARAT");
							} else {
								if (sisa > 1000000) {
									kondisi.setBackgroundColor(Color.rgb(0x88,
											0xFF, 0x88));
									kondisi.setText(tv_saldo + "\nMAKMUR");
								} else {
									kondisi.setBackgroundColor(Color.rgb(0x88,
											0x88, 0xFF));
									kondisi.setText(tv_saldo + "\nNORMAL");
								}
							}
						}
					});

				}
			}

			return rootView;
		}
	}

}
