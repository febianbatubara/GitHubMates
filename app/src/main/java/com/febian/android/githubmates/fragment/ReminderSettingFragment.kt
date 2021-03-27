package com.febian.android.githubmates.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.febian.android.githubmates.databinding.FragmentReminderSettingBinding

private const val ARG_PARAM1 = "param1"

class ReminderSettingFragment : DialogFragment() {

    private lateinit var binding: FragmentReminderSettingBinding
    private var optionDialogListener: OnOptionDialogListener? = null

    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
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

        binding.switchReminder.setOnClickListener {

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (fragment is DetailCategoryFragment) {
//            val detailCategoryFragment = fragment
//            this.optionDialogListener = detailCategoryFragment.optionDialogListener
//        }
    }

    override fun onDetach() {
        super.onDetach()
        this.optionDialogListener = null
    }

    interface OnOptionDialogListener {
        fun onOptionChosen(text: String?)
    }

}