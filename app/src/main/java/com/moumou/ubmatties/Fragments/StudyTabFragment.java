package com.moumou.ubmatties.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.moumou.ubmatties.Adapters.StudyListAdapter;
import com.moumou.ubmatties.R;
import com.moumou.ubmatties.Session;
import com.moumou.ubmatties.globals.SessionType;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import static com.moumou.ubmatties.globals.SessionType.COFFEE;
import static com.moumou.ubmatties.globals.SessionType.LUNCH;
import static com.moumou.ubmatties.globals.SessionType.STUDY;

/**
 * Created by MouMou on 04-10-2016
 */

public class StudyTabFragment extends Fragment implements View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {

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

    private CalendarDatePickerDialogFragment datePicker;
    private RadialTimePickerDialogFragment timePicker;

    private ListView studyListView;
    private StudyListAdapter studyListAdapter;
    private List<Session> sessionList;

    private LocalDate newDate;
    private LocalTime newStart;
    private LocalTime newEnd;
    private SessionType newType;
    private int timePickerN;

    public StudyTabFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_tab, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timePickerN = 2;
        initListView(view);
        initFab(view);

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
                LocalTime now = LocalTime.now();

                newStart = new LocalTime(hourOfDay, minute);
                timePicker = new RadialTimePickerDialogFragment().setOnTimeSetListener(StudyTabFragment.this);
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

    private void initListView(View view) {

        sessionList = new ArrayList<>();
        studyListAdapter = new StudyListAdapter(getContext(), sessionList);
        studyListView = (ListView) view.findViewById(R.id.study_list);
        studyListView.setAdapter(studyListAdapter);


    }

    private void newSession(SessionType type) {
        newType = type;
        DateTime now = DateTime.now();
        MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth());
        datePicker = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(StudyTabFragment.this);
        datePicker.setFirstDayOfWeek(2);
        datePicker.setDateRange(minDate, null);
        datePicker.show(getFragmentManager(), DATEPICKER_TAG);


    }

    private void addSession() {
        Session session = new Session(newType, newDate, newStart, newEnd);

        if (!newEnd.isAfter(newStart)) {
            return;
            //TODO add code for dialog
        }
        sessionList.add(session);
        studyListAdapter.notifyDataSetChanged();
    }

}
