package com.example.kountapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kountapp.databinding.FragmentFirstBinding
import com.kount.api.analytics.AnalyticsApplication
import com.kount.api.analytics.AnalyticsCollector
import com.kount.api.analytics.enums.EventEnums

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        this.activity?.let {
            AnalyticsApplication.registerKountEvents(EventEnums.EVENT_STARTED,
                it
            )
        }
        AnalyticsCollector.getCollectionStatus()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.activity?.let {
            AnalyticsApplication.registerKountEvents(EventEnums.EVENT_STOPPED,
                it
            )
        }
        AnalyticsCollector.getCollectionStatus()
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}