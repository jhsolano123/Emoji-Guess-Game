package com.example.emojiguess.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.emojiguess.databinding.FragmentChatBinding
import com.google.android.material.snackbar.Snackbar

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val args: ChatFragmentArgs by lazy {
        ChatFragmentArgs(
            roomId = requireArguments().getString(ARG_ROOM_ID).orEmpty(),
            playerId = requireArguments().getString(ARG_PLAYER_ID).orEmpty(),
            playerName = requireArguments().getString(ARG_PLAYER_NAME).orEmpty()
        )
    }

    private val viewModel: ChatViewModel by viewModels {
        ChatViewModel.Factory(args.roomId, args.playerId, args.playerName)
    }

    private val chatAdapter: ChatAdapter by lazy { ChatAdapter(args.playerId) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupInput()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerMessages.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
        }
    }

    private fun setupInput() {
        binding.buttonSend.setOnClickListener {
            val message = binding.inputMessage.text?.toString().orEmpty()
            if (message.isBlank()) {
                Snackbar.make(binding.root, com.example.emojiguess.R.string.error_empty_message, Snackbar.LENGTH_SHORT).show()
            } else {
                viewModel.sendMessage(message)
                binding.inputMessage.text?.clear()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            binding.textEmptyChat.isVisible = messages.isEmpty()
            chatAdapter.submitList(messages)
            if (messages.isNotEmpty()) {
                binding.recyclerMessages.scrollToPosition(messages.lastIndex)
            }
        }

        viewModel.inputEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.inputMessage.isEnabled = enabled
            binding.buttonSend.isEnabled = enabled
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class ChatFragmentArgs(val roomId: String, val playerId: String, val playerName: String)

    companion object {
        private const val ARG_ROOM_ID = "arg_room_id"
        private const val ARG_PLAYER_ID = "arg_player_id"
        private const val ARG_PLAYER_NAME = "arg_player_name"

        fun newInstance(roomId: String, playerId: String, playerName: String): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ROOM_ID, roomId)
                    putString(ARG_PLAYER_ID, playerId)
                    putString(ARG_PLAYER_NAME, playerName)
                }
            }
        }
    }
}