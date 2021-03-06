package com.heig.atmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.heig.atmanager.goals.GoalTodo;
import com.heig.atmanager.tasks.Task;
import com.heig.atmanager.tasks.TaskFeedAdapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Author : Stéphane Bottin
 * Date   : 11.03.2020
 * <p>
 * Fragment for the Home view (User's Tasks and Goals of the day)
 */
public class HomeFragment extends Fragment {

    public static final String FRAG_HOME_ID = "Home_Fragment";
    private static final int MORNING_HOUR = 11;
    private static final int DAY_HOUR = 13;
    private static final int AFTERNOON_HOUR = 18;

    private ProgressBar feedProgress;
    private SwipeRefreshLayout refreshLayout;

    // Greeting message
    private TextView greetingText;

    // Goal feed
    private ArrayList<GoalTodo> goals; // user data
    private RecyclerView goalsRecyclerView;

    // Task feed
    private ArrayList<Task> tasks; // user data
    private RecyclerView tasksRecyclerView;
    private RecyclerView.Adapter adapter;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        view = v;

        feedProgress = v.findViewById(R.id.home_progress);
        refreshLayout = v.findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateHomeFragment();
            }
        });

        // Data init
        tasks = new ArrayList<>();
        goals = new ArrayList<>();

        // Greeting
        greetingText = (TextView) v.findViewById(R.id.greeting_text);
        greetingText.setText(getDayGreetings());

        // Task feed
        tasksRecyclerView = (RecyclerView) v.findViewById(R.id.tasks_rv);
        tasksRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(v.getContext());
        tasksRecyclerView.setLayoutManager(manager);
        adapter = new TaskFeedAdapter(tasks, getContext());
        tasksRecyclerView.setAdapter(adapter);

        // Set adapter for searches
        ((MainActivity) getContext()).setContentAdapter(adapter);

        // Goal feed
        goalsRecyclerView = (RecyclerView) v.findViewById(R.id.goals_rv);

        return v;
    }

    private String getDayGreetings() {
        Calendar calendar = Calendar.getInstance();
        String greeting = "";
        String user = MainActivity.getUser().getUserName();

        // Hour (0 - 23)
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        // Select proper greeting
        if (currentHour < MORNING_HOUR) {
            greeting = "Good morning ";
        } else if (currentHour < DAY_HOUR) {
            greeting = "Hello ";
        } else if (currentHour < AFTERNOON_HOUR) {
            greeting = "Good afternoon ";
        } else {
            greeting = "Good evening ";
        }

        greeting += user;

        return greeting;
    }

    /**
     * Get the welcoming sentence (dynamic with user's data)
     *
     * @return the proper greetings
     */
    private String getGreetings() {
        String user_info = "";

        // Select user info sentence (total tasks/goals for the day)
        if (tasks.isEmpty() && goals.isEmpty()) {
            user_info = "relax! You have nothing to do today.";
        } else if (!tasks.isEmpty() && goals.isEmpty()) {
            user_info = getSingleUserInfoGreeting(tasks.size()) + " for today.";
        } else if (tasks.isEmpty()) { // goals != 0 always true
            user_info = getSingleUserInfoGreeting(goals.size()) + " for today.";
        } else {
            user_info = getSingleUserInfoGreeting(tasks.size()) + " and "
                    + goals.size() + " goal" + (goals.size() > 1 ? "s" : "") + " for today.";
        }

        return getDayGreetings() + ",\n" + user_info;
    }

    /**
     * Get the total of a value in a sentence
     *
     * @param value : the value to display
     * @return the formatted sentence
     */
    private String getSingleUserInfoGreeting(int value) {
        return "You " + (value < 5 ? "just " : "") + "have " +
                value + " task" + (value > 1 ? "s" : "");
    }

    public void updateHomeFragment() {
        feedProgress.setVisibility(View.GONE);
        ArrayList<Task> allTasks;
        tasks.clear();
        allTasks = MainActivity.getUser().getHomeViewTasks();
        for (Task task : allTasks) {
            if (!task.isArchived()) {
                tasks.add(task);
            }
        }
        goals = MainActivity.getUser().getGoalTodosOfDay(Calendar.getInstance().getTime());

        TaskFeedAdapter newAdapter = new TaskFeedAdapter(tasks, getContext());
        tasksRecyclerView.swapAdapter(newAdapter, false);

        Utils.setupGoalTodosFeedBubbled(view, goalsRecyclerView, goals, getContext());
        greetingText.setText(getGreetings());
        refreshLayout.setRefreshing(false);
    }
}
