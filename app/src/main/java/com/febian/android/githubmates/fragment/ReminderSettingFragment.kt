package com.febian.android.githubmates.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.febian.android.githubmates.R
import com.febian.android.githubmates.databinding.FragmentReminderSettingBinding
import com.febian.android.githubmates.utils.ReminderReceiver

class ReminderSettingFragment : DialogFragment() {

    private lateinit var binding: FragmentReminderSettingBinding
    private lateinit var reminderReceiver: ReminderReceiver

    companion object {
        private const val PREFS_NAME = "reminder_preference"
        private const val IS_REMINDER_ACTIVATED = "is_reminder_activated"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReminderSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reminderReceiver = ReminderReceiver()

        val reminderSharedPreferences: SharedPreferences = activity!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        binding.switchReminder.isChecked =
            reminderSharedPreferences.getBoolean(IS_REMINDER_ACTIVATED, false)

        binding.switchReminder.setOnClickListener {
            if (binding.switchReminder.isChecked) {
                val editor = reminderSharedPreferences.edit()
                editor.putBoolean(IS_REMINDER_ACTIVATED, true)
                editor.apply()
                binding.switchReminder.isChecked = true

                val message = context!!.getString(R.string.find_out_awesome_developers)
                reminderReceiver.setRepeatingReminder(context!!, message)
            } else {
                val editor = reminderSharedPreferences.edit()
                editor.putBoolean(IS_REMINDER_ACTIVATED, false)
                editor.apply()
                binding.switchReminder.isChecked = false

                reminderReceiver.cancelRepeatingReminder(context!!)
            }
        }
    }

}