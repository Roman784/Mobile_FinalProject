package com.example.mobile_finalproject.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.mobile_finalproject.R
import com.example.mobile_finalproject.models.Card

class EditCardDialog(val card: Card) : DialogFragment() {

    interface EditCardDialogListener
    {
        fun onReturnValue(card: Card)
        fun onDelete(card: Card)
    }

    private  lateinit var edtTerm: EditText
    private  lateinit var edtDefinition: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity(), R.style.DialogTheme)
        val inflater = requireActivity().layoutInflater
        val layout = inflater.inflate(R.layout.edit_card_dialog, null)

        edtTerm = layout.findViewById(R.id.edtTerm)
        edtDefinition = layout.findViewById(R.id.edtDefinition)

        edtTerm.setText(card.term)
        edtDefinition.setText(card.definition)

        builder.setView(layout)
            .setPositiveButton(R.string.confirm) { dialog, id ->
                updateCard()
            }
            .setNegativeButton(R.string.cancel) { dialog, id ->
                if (edtTerm.text.isEmpty() && edtDefinition.text.isEmpty())
                    deleteCard()
                else
                    dialog.cancel()
            }
            .setNeutralButton(R.string.delete) { dialog, id ->
                deleteCard()
            }

        return builder.create()
    }

    private fun updateCard() {
        var newTerm = edtTerm.text.toString()
        var newDefinition = edtDefinition.text.toString()

        if (newTerm.isEmpty()) newTerm = card.term
        if (newDefinition.isEmpty()) newDefinition = card.definition

        val newCard = Card(
            id = card.id,
            deckId = card.deckId,
            term = newTerm,
            definition = newDefinition
        )

        val activity = activity as EditCardDialogListener?
        activity!!.onReturnValue(newCard)
    }

    private fun deleteCard() {
        val activity = activity as EditCardDialogListener?
        activity!!.onDelete(card)
    }
}