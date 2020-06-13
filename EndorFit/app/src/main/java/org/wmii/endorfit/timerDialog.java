package org.wmii.endorfit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class timerDialog extends AppCompatDialogFragment {
    private EditText time;
    private timerDialogListener timerListener;
    private Button buttonSubmit;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            timerListener = (timerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement timer listener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.set_timer, null);
        time = view.findViewById(R.id.editTimer);
        buttonSubmit = view.findViewById(R.id.buttonSubmitTimer);

        builder.setView(view);
        builder.setTitle("Set timer");
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seconds = time.getText().toString();
                if (seconds == "") {
                    seconds = "60";
                }
                timerListener.applyText(seconds);
                dismiss();
            }
        });
        String seconds = time.getText().toString();
        timerListener.applyText(seconds);
        return builder.create();
    }

    public interface timerDialogListener {
        void applyText(String seconds);
    }
}
