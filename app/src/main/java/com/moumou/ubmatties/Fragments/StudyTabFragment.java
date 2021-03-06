package com.moumou.ubmatties.Fragments;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.moumou.ubmatties.Adapters.StudyListAdapter;
import com.moumou.ubmatties.Dialog.AddUserDialog;
import com.moumou.ubmatties.Loaders.AddSessionLoader;
import com.moumou.ubmatties.Loaders.OwnSessionsLoader;
import com.moumou.ubmatties.MainActivity;
import com.moumou.ubmatties.R;
import com.moumou.ubmatties.Session;
import com.moumou.ubmatties.globals.Globals;
import com.moumou.ubmatties.globals.SessionType;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.moumou.ubmatties.globals.SessionType.COFFEE;
import static com.moumou.ubmatties.globals.SessionType.LUNCH;
import static com.moumou.ubmatties.globals.SessionType.STUDY;

/**
 * Created by MouMou on 04-10-2016
 */

public class StudyTabFragment extends Fragment implements View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener, LoaderManager.LoaderCallbacks {

    private static final String DATEPICKER_TAG = "NEW_DATE_PICKER";
    private static final String TIMEPICKER_TAG = "NEW_TIME_PICKER";
    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private boolean isFabOpen = false;
    private Animation fab_open;
    private Animation fab_close;
    private Animation rotate_forward;
    private Animation rotate_backward;


    private HttpURLConnection urlConnection;
    private URL dataBaseUrl;

    private RadialTimePickerDialogFragment timePicker;

    private CalendarDatePickerDialogFragment datePickerModify;
    private RadialTimePickerDialogFragment timePickerStartModify;
    private RadialTimePickerDialogFragment timePickerEndModify;

    private ListView studyListView;
    private StudyListAdapter studyListAdapter;
    private List<Session> sessionList;

    private LocalDate newDate;
    private LocalTime newStart;
    private LocalTime newEnd;
    private SessionType newType;
    private int timePickerN;

    private SwipeRefreshLayout swipeContainer;

    public StudyTabFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.study_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        studyListView = (ListView) view.findViewById(R.id.study_list);
        initListView();
        initFab(view);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.study_swipe_container);
        initSwipeToRefresh();

        getLoaderManager().initLoader(Globals.LOADER_GET_OWN_SESSIONS, null, this).forceLoad();


        if (MainActivity.getSelf() != null) {
            //getSessionsFromDB();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        closeFAB();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab_study_tab:
                animateFAB();
                break;
            case R.id.fab_coffee:
                animateFAB();
                newSession(COFFEE);
                break;
            case R.id.fab_lunch:
                animateFAB();
                newSession(LUNCH);
                break;
            case R.id.fab_study:
                animateFAB();
                newSession(STUDY);
                break;
        }
    }

    private void initSwipeToRefresh() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSessionsFromDB();
            }
        });
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        newDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);

        timePickerN = 0;
        timePicker = new RadialTimePickerDialogFragment().setOnTimeSetListener(StudyTabFragment.this);
        timePicker.setForced24hFormat();
        timePicker.setStartTime(LocalTime.now().getHourOfDay(), LocalTime.now().getMinuteOfHour());
        timePicker.setTitleText("Start Time");
        timePicker.show(getFragmentManager(), TIMEPICKER_TAG);
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        switch (timePickerN) {
            case 0:

                newStart = new LocalTime(hourOfDay, minute);
                LocalTime now = newStart;
                timePicker = new RadialTimePickerDialogFragment().setOnTimeSetListener(
                        StudyTabFragment.this);
                timePicker.setForced24hFormat();
                timePicker.setTitleText("End Time");
                switch (newType) {
                    case LUNCH:
                        now = now.plusMinutes(30);
                        timePicker.setStartTime(now.getHourOfDay(), now.getMinuteOfHour());
                        break;
                    case COFFEE:
                        now = now.plusMinutes(15);
                        timePicker.setStartTime(now.getHourOfDay(), now.getMinuteOfHour());
                        break;
                    case STUDY:
                        now = now.plusHours(1);
                        timePicker.setStartTime(now.getHourOfDay(), now.getMinuteOfHour());
                        break;
                }
                timePicker.show(getFragmentManager(), TIMEPICKER_TAG);
                timePickerN++;
                break;
            case 1:
                newEnd = new LocalTime(hourOfDay, minute);
                addSession();
                break;
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (args == null) {
            return new OwnSessionsLoader(getContext());
        }
        if (args.get(Globals.TAG_SESSION) != null) {
            return new AddSessionLoader(getContext(), (Session) args.get(Globals.TAG_SESSION));
        } else {
            return new OwnSessionsLoader(getContext());
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (data == null) {
            return;
        }

        if (data instanceof ArrayList) {
            ArrayList list = (ArrayList) data;

            studyListAdapter.clear();
            for (int i = 0; i < list.size(); i++) {
                sessionList.add((Session) list.get(i));
            }
        } else if (data instanceof Boolean) {
            if (!(Boolean) data) {
                MainActivity.showAlertDialog("Can't insert session into Database");
            }
            getSessionsFromDB();
        }

        studyListAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private void initListView() {
        if (sessionList == null) {
            sessionList = new ArrayList<>();
        }
        sessionList.add(new Session(COFFEE,
                                    LocalDate.now(),
                                    LocalTime.now(),
                                    LocalTime.now().plusHours(1)));
        studyListAdapter = new StudyListAdapter(getContext(), sessionList);
        studyListView.setAdapter(studyListAdapter);

        studyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                modifySession((Session) studyListView.getAdapter().getItem(position));
                return true;
            }
        });

        studyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addUsers((Session) studyListView.getAdapter().getItem(position));
            }
        });
    }

    private void addUsers(Session session) {
        AddUserDialog d = new AddUserDialog(getContext(), session.getSessionUsers());
        d.show();
    }

    private void newSession(SessionType type) {
        newType = type;
        DateTime now = DateTime.now();
        MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(now.getYear(),
                                                                        now.getMonthOfYear() - 1,
                                                                        now.getDayOfMonth());
        CalendarDatePickerDialogFragment datePicker = new CalendarDatePickerDialogFragment().setOnDateSetListener(
                StudyTabFragment.this);
        datePicker.setFirstDayOfWeek(2);
        datePicker.setDateRange(minDate, null);
        datePicker.show(getFragmentManager(), DATEPICKER_TAG);
    }

    private void addSession() {
        Session session = new Session(0, newType, newDate, newStart, newEnd);
        if (!newEnd.isAfter(newStart)) {
            MainActivity.showAlertDialog("End time is earlier than the start time");
            return;
        }
        addSessionToDB(session);
    }

    private void modifySession(final Session session) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.modify_session);

        final Button type = (Button) dialog.findViewById(R.id.modify_session_type);
        final Button date = (Button) dialog.findViewById(R.id.modify_session_date);
        final Button startTime = (Button) dialog.findViewById(R.id.modify_session_start);
        final Button endTime = (Button) dialog.findViewById(R.id.modify_session_end);

        type.setText(session.getType().toString());
        date.setText(session.getDate().toString("dd MMM"));
        startTime.setText(session.getStartTime().toString("HH:mm"));
        endTime.setText(session.getEndTime().toString("HH:mm"));

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog typeDialog = new Dialog(v.getContext());
                typeDialog.setContentView(R.layout.modify_type);

                Button study = (Button) typeDialog.findViewById(R.id.set_type_study);
                Button lunch = (Button) typeDialog.findViewById(R.id.set_type_lunch);
                Button coffee = (Button) typeDialog.findViewById(R.id.set_type_coffee);

                study.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        session.setType(STUDY);
                        typeDialog.dismiss();
                        studyListAdapter.notifyDataSetChanged();
                        type.setText(getString(R.string.type_study));
                        updateSession(session);
                    }
                });

                lunch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        session.setType(LUNCH);
                        typeDialog.dismiss();
                        studyListAdapter.notifyDataSetChanged();
                        type.setText(getString(R.string.type_lunch));
                        updateSession(session);
                    }
                });

                coffee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        session.setType(COFFEE);
                        typeDialog.dismiss();
                        studyListAdapter.notifyDataSetChanged();
                        type.setText(getString(R.string.type_coffee));
                        updateSession(session);
                    }
                });
                typeDialog.show();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime now = DateTime.now();
                MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(now.getYear(),
                                                                                now.getMonthOfYear() -
                                                                                1,
                                                                                now.getDayOfMonth());
                datePickerModify = new CalendarDatePickerDialogFragment().setOnDateSetListener(
                        StudyTabFragment.this);
                datePickerModify.setFirstDayOfWeek(2);
                datePickerModify.setDateRange(minDate, null);
                datePickerModify.setPreselectedDate(session.getDate().getYear(),
                                                    session.getDate().getMonthOfYear() - 1,
                                                    session.getDate().getDayOfMonth());
                datePickerModify.show(getFragmentManager(), DATEPICKER_TAG);
                datePickerModify.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        session.setDate(new LocalDate(year, monthOfYear + 1, dayOfMonth));
                        studyListAdapter.notifyDataSetChanged();
                        date.setText(session.getDate().toString("dd MMM"));
                        updateSession(session);
                    }
                });
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerStartModify = new RadialTimePickerDialogFragment().setOnTimeSetListener(
                        StudyTabFragment.this);
                timePickerStartModify.setForced24hFormat();
                timePickerStartModify.setTitleText("New Start Time");
                timePickerStartModify.show(getFragmentManager(), "");
                timePickerStartModify.setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
                        if (!new LocalTime(hourOfDay, minute).isAfter(session.getEndTime())) {
                            MainActivity.showAlertDialog("Start time is later than end time");
                            return;
                        }
                        session.setStartTime(new LocalTime(hourOfDay, minute));
                        studyListAdapter.notifyDataSetChanged();
                        startTime.setText(session.getStartTime().toString("HH:mm"));
                        updateSession(session);
                    }
                });
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerEndModify = new RadialTimePickerDialogFragment().setOnTimeSetListener(
                        StudyTabFragment.this);
                timePickerEndModify.setForced24hFormat();
                timePickerEndModify.setTitleText("New Start Time");
                timePickerEndModify.show(getFragmentManager(), "");
                timePickerEndModify.setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
                        if (!new LocalTime(hourOfDay, minute).isAfter(session.getEndTime())) {
                            MainActivity.showAlertDialog("End time is earlier than the start time");
                            return;
                        }
                        session.setEndTime(new LocalTime(hourOfDay, minute));
                        studyListAdapter.notifyDataSetChanged();
                        endTime.setText(session.getEndTime().toString("HH:mm"));
                        updateSession(session);
                    }
                });
            }
        });
        dialog.show();
    }

    public void animateFAB() {

        if (isFabOpen) {
            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;
        }
    }

    private void closeFAB() {
        if (isFabOpen) {
            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
        }
    }

    private void initFab(View view) {
        fab = (FloatingActionButton) view.findViewById(R.id.fab_study_tab);
        fab.setOnClickListener(this);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab_coffee);
        fab1.setOnClickListener(this);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab_lunch);
        fab2.setOnClickListener(this);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab_study);
        fab3.setOnClickListener(this);

        fab_open = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(view.getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(view.getContext(), R.anim.rotate_backward);
    }

    private void getSessionsFromDB() {
        getLoaderManager().getLoader(Globals.LOADER_GET_OWN_SESSIONS).forceLoad();
    }



    private void addSessionToDB(final Session s) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("session", s);
        getLoaderManager().initLoader(Globals.LOADER_ADD_SESSION, bundle, this).forceLoad();
    }

    private void updateSession(final Session s) {
        class GetJSONData extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String result = null;

                try {
                    dataBaseUrl = new URL(Globals.DB_UPDATE_SESSION + s.getId() + "&hostid=" +
                                          s.getHost().getId() + "&type=" +
                                          SessionType.toInt(s.getType()) + "&date=" +
                                          s.getDate().toString("YYYY-MM-dd") + "&start=" +
                                          s.getStartTime().toString("HH:mm:ss") + "&end=" +
                                          s.getEndTime().toString("HH:mm:ss"));
                    urlConnection = (HttpURLConnection) dataBaseUrl.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    result = sb.toString();
                } catch (NullPointerException e) {
                    MainActivity.showAlertDialog("Can't modify session");
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (!s.equals("success\n")) {
                    MainActivity.showAlertDialog(s);
                }
                getSessionsFromDB();
            }
        }
        GetJSONData g = new GetJSONData();
        g.execute();
    }
}
